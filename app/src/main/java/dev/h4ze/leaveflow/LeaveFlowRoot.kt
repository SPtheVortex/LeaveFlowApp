package dev.h4ze.leaveflow

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.tween
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private sealed interface AppScreen {
    data object Home : AppScreen
    data object ListPage : AppScreen
    data class Detail(val id: String) : AppScreen
    data class ForwardForm(val id: String) : AppScreen
    data class Edit(val id: String?, val secretMode: Boolean) : AppScreen
    data object SecretManager : AppScreen
}

@Composable
fun LeaveFlowRoot(viewModel: LeaveViewModel = viewModel()) {
    LeaveFlowTheme {
        val records by viewModel.records.collectAsStateWithLifecycle()
        val stack = remember { mutableStateListOf<AppScreen>(AppScreen.Home) }
        fun push(screen: AppScreen) { stack.add(screen) }
        fun back() { if (stack.size > 1) stack.removeAt(stack.lastIndex) }
        BackHandler(enabled = stack.size > 1, onBack = ::back)

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            when (val screen = stack.last()) {
                AppScreen.Home -> HomeScreen(onLeaveClick = { push(AppScreen.ListPage) })
                AppScreen.ListPage -> LeaveListScreen(
                    records = records,
                    onBack = ::back,
                    onOpen = { push(AppScreen.Detail(it)) },
                    onAdd = { push(AppScreen.Edit(id = null, secretMode = false)) },
                )
                is AppScreen.Detail -> {
                    val record = viewModel.find(screen.id)
                    if (record != null) {
                        DetailScreen(
                            record = record,
                            onBack = ::back,
                            onSecretEntry = { push(AppScreen.SecretManager) },
                            onForward = { push(AppScreen.ForwardForm(screen.id)) },
                        )
                    } else {
                        LaunchedEffect(screen.id) { back() }
                    }
                }
                is AppScreen.ForwardForm -> {
                    val record = viewModel.find(screen.id)
                    if (record != null) {
                        ForwardFormScreen(record = record, onBack = ::back)
                    } else {
                        LaunchedEffect(screen.id) { back() }
                    }
                }
                is AppScreen.Edit -> RecordEditorScreen(
                    initial = screen.id?.let(viewModel::find),
                    secretMode = screen.secretMode,
                    onBack = ::back,
                    onSave = {
                        viewModel.save(it)
                        back()
                    },
                )
                AppScreen.SecretManager -> SecretManagerScreen(
                    records = records,
                    onBack = ::back,
                    onAdd = { push(AppScreen.Edit(null, secretMode = true)) },
                    onEdit = { push(AppScreen.Edit(it, secretMode = true)) },
                    onDelete = viewModel::delete,
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(onLeaveClick: () -> Unit) {
    Box(Modifier.fillMaxSize().background(LeaveColors.PageBackground)) {
        Column(Modifier.fillMaxSize()) {
            BrowserHeader(
                title = "c.uyiban.com/#/",
                subtitle = "c.uyiban.com",
                onBack = null,
                showClose = false,
            )
            Row(
                modifier = Modifier.fillMaxWidth().height(68.dp).background(Color.White),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(R.drawable.campus_message),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(58.dp),
                    )
                    Text("消息", modifier = Modifier.padding(start = 1.dp), fontSize = 15.sp, color = LeaveColors.TextPrimary)
                }
                HorizontalDivider(Modifier.height(48.dp).width(1.dp), color = Color(0xFFD9D9D9))
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(R.drawable.campus_workbench),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(58.dp),
                    )
                    Text("工作台", modifier = Modifier.padding(start = 1.dp), fontSize = 15.sp, color = LeaveColors.TextPrimary)
                }
            }
            HorizontalDivider(thickness = 6.dp, color = LeaveColors.PageBackground)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(top = 20.dp, bottom = 32.dp),
                ) {
                    HomeSection("人员管理") {
                        Row(Modifier.fillMaxWidth()) {
                            CampusGridItem("信息完善", R.drawable.campus_info_complete, Modifier.weight(1f))
                            Spacer(Modifier.weight(3f))
                        }
                    }
                    HomeSection("师生事务") {
                        Row(Modifier.fillMaxWidth()) {
                            CampusGridItem("办事大厅", R.drawable.campus_service_hall, Modifier.weight(1f))
                            CampusGridItem("请销假", R.drawable.campus_leave, Modifier.weight(1f), onClick = onLeaveClick)
                            CampusGridItem("超级流程", R.drawable.campus_workflow, Modifier.weight(1f))
                            CampusGridItem("自定义签到", R.drawable.campus_checkin, Modifier.weight(1f))
                        }
                    }
                    HomeSection("校园活动") {
                        Row(Modifier.fillMaxWidth()) {
                            CampusGridItem("校园答题", R.drawable.campus_quiz, Modifier.weight(1f))
                            CampusGridItem("校园活动", R.drawable.campus_activity, Modifier.weight(1f))
                            Spacer(Modifier.weight(2f))
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(24.dp)
                .size(52.dp)
                .clickable(onClick = {}),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.campus_personal_center),
                contentDescription = "个人中心",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(51.dp),
            )
        }
    }
}

@Composable
private fun HomeSection(title: String, content: @Composable () -> Unit) {
    Column {
        Row(
            modifier = Modifier.padding(start = 20.dp, end = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.width(4.dp).height(12.dp).background(Color(0xFFCCCCCC), RoundedCornerShape(2.dp)))
            Text(
                title,
                modifier = Modifier.padding(start = 5.dp).semantics { heading() },
                fontSize = 15.sp,
                color = Color(0xFF666666),
                fontWeight = FontWeight.Bold,
            )
        }
        Box(Modifier.padding(top = 20.dp)) { content() }
    }
}

@Composable
private fun CampusGridItem(
    label: String,
    drawableId: Int,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .height(94.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(drawableId),
            contentDescription = label,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(49.dp),
        )
        Spacer(Modifier.height(5.dp))
        Text(label, color = LeaveColors.TextPrimary, fontSize = 13.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun LeaveListScreen(
    records: List<LeaveRecord>,
    onBack: () -> Unit,
    onOpen: (String) -> Unit,
    onAdd: () -> Unit,
) {
    Column(Modifier.fillMaxSize().background(LeaveColors.PageBackground)) {
        BrowserHeader(title = "请销假", subtitle = "app.uyiban.com", onBack = onBack)
        Row(
            modifier = Modifier.fillMaxWidth().height(54.dp).background(Color.White).clickable(onClick = {}),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ReferenceIconArtwork(
                ReferenceIcon.APPLICATION,
                Color(0xFF9EA1A6),
                Modifier.padding(start = 16.dp).size(29.dp),
            )
            Text("我的申请", modifier = Modifier.padding(start = 14.dp).weight(1f), fontSize = 19.sp)
            ChevronIndicator(Modifier.padding(end = 16.dp).size(21.dp), color = Color(0xFFB6B8BB))
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 18.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(records, key = { it.id }) { record ->
                LeaveSummaryCard(record = record, onClick = { onOpen(record.id) })
            }
            if (records.isEmpty()) {
                item {
                    Text(
                        "暂无请假记录",
                        modifier = Modifier.fillMaxWidth().padding(top = 64.dp),
                        color = LeaveColors.TextSecondary,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            item {
                Text(
                    "我是有底线的",
                    modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
                    color = LeaveColors.TextSecondary,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
        Button(
            onClick = onAdd,
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().height(52.dp),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LeaveColors.ActionBlue),
        ) {
            Text("请假申请", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
private fun LeaveSummaryCard(record: LeaveRecord, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("我的请假", color = LeaveColors.TextSecondary, fontSize = 17.sp)
                Text("跨天", modifier = Modifier.padding(start = 8.dp).background(Color(0xFFE8F4FD)).padding(horizontal = 4.dp, vertical = 2.dp), color = LeaveColors.IconBlue, fontSize = 12.sp)
                Spacer(Modifier.weight(1f))
                Text(record.submittedDate, color = LeaveColors.TextSecondary, fontSize = 14.sp)
            }
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    SummaryLine("开始时间：", record.startTime)
                    SummaryLine("结束时间：", record.endTime)
                }
                ChevronIndicator(Modifier.size(19.dp), color = Color(0xFFC2C4C7))
            }
            Text(
                record.status.displayName,
                modifier = Modifier.padding(top = 13.dp, bottom = 8.dp),
                color = if (record.status == LeaveStatus.APPROVED) LeaveColors.Approved else LeaveColors.Ended,
                fontSize = 17.sp,
            )
            HorizontalDivider(color = LeaveColors.Divider)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(record.proxyName, color = LeaveColors.IconBlue, fontSize = 16.sp)
                Text(" 于 ", color = LeaveColors.TextPrimary, fontSize = 16.sp)
                Text(record.proxyTime, color = LeaveColors.IconBlue, fontSize = 16.sp)
                Text(" 代为销假", color = LeaveColors.TextPrimary, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun SummaryLine(label: String, value: String) {
    Row {
        Text(label, color = LeaveColors.TextSecondary, fontSize = 16.sp)
        Text(value, color = LeaveColors.TextSecondary, fontSize = 16.sp)
    }
}

@Composable
private fun DetailScreenShare(
    record: LeaveRecord,
    onBack: () -> Unit,
    onSecretEntry: () -> Unit,
) {
    var titleTaps by remember(record.id) { mutableIntStateOf(0) }
    var lastTitleTap by remember(record.id) { mutableLongStateOf(0L) }
    // The public share page starts collapsed; the row remains tappable to reveal every field.
    var applicantExpanded by rememberSaveable(record.id) { mutableStateOf(false) }
    val displayedStudentId = record.studentId.ifBlank { "暂无学号" }
    Box(Modifier.fillMaxSize().background(LeaveColors.PageBackground)) {
        Column(Modifier.fillMaxSize()) {
            Text(
                text = record.detailTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp)
                    .background(Color.White)
                    .clickable {
                        val now = android.os.SystemClock.elapsedRealtime()
                        titleTaps = if (now - lastTitleTap <= 1_500L) titleTaps + 1 else 1
                        lastTitleTap = now
                        if (titleTaps >= 5) { titleTaps = 0; onSecretEntry() }
                    }
                    .wrapContentHeight(Alignment.CenterVertically)
                    .semantics { heading() },
                color = LeaveColors.TextPrimary,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
            HorizontalDivider(thickness = 1.dp, color = LeaveColors.Divider)
            LazyColumn(Modifier.weight(1f).fillMaxWidth()) {
                item { ShareSectionHeader(record.applicantSectionTitle) }
                item {
                    Column(
                        Modifier.fillMaxWidth().background(Color.White)
                            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 15.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                                .clickable { applicantExpanded = !applicantExpanded },
                            verticalAlignment = Alignment.Top,
                        ) {
                            Text("姓名", Modifier.weight(.22f), Color(0xFF888888), fontSize = 15.sp, lineHeight = 20.sp, textAlign = TextAlign.End)
                            Row(Modifier.weight(.78f).padding(start = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    if (applicantExpanded) record.applicantName
                                    else "${record.applicantName}($displayedStudentId)",
                                    Modifier.weight(1f),
                                    Color.Black,
                                    fontSize = 16.sp,
                                    lineHeight = 22.sp,
                                )
                                ChevronIndicator(Modifier.size(18.dp), pointsDown = !applicantExpanded, pointsUp = applicantExpanded)
                            }
                        }
                        if (applicantExpanded) {
                            ShareDetailRow("学号", record.studentId)
                            ShareDetailRow("性别", record.gender)
                            ShareDetailRow("学院", record.college)
                            ShareDetailRow("系", record.department)
                            ShareDetailRow("专业", record.major)
                            ShareDetailRow("专业方向", record.majorDirection)
                            ShareDetailRow("班级", record.className)
                            ShareDetailRow("年级", record.grade)
                            ShareDetailRow("教育层级", record.educationLevel)
                            ShareDetailRow("学制", record.schoolingLength)
                        }
                    }
                }
                item { ShareSectionHeader(record.contentSectionTitle) }
                item {
                    Column(
                        Modifier.fillMaxWidth().background(Color.White)
                            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 15.dp),
                    ) {
                        ShareDetailRow("审批编号", record.approvalNo)
                        ShareDetailRow("是否离校", record.awayMode)
                        ShareDetailRow("请假类型", record.leaveType)
                        ShareDetailRow("请假原因\n(真实原因)", record.reason)
                        ShareDetailRow("请假去向", record.destination)
                        ShareDetailRow("详细地址\n(具体到乡\n镇街道村\n组)", record.address)
                        ShareDetailRow("本人联系电话", record.phone)
                        ShareDetailRow("开始时间", record.startTime)
                        ShareDetailRow("结束时间", record.endTime)
                        ShareDetailRow("时长(天)", record.duration)
                        ShareDetailRow("班主任/导师姓名", record.teacherName)
                        ShareDetailRow("班主任/导师联系方式", record.teacherPhone)
                        ShareDetailRow("家长（家属）姓名", record.parentName)
                        ShareDetailRow("家长（家属）联系电话", record.parentPhone)
                        ShareDetailRow("紧急联系人", record.emergencyName)
                        ShareDetailRow("紧急联系电话", record.emergencyPhone)
                        AttachmentDetailRow("本人申请书\n(本科生上传)，导师意见（研究生上传)", record.applicationFileName, record.applicationFileUri, chatPlaceholder = true, shareStyle = true)
                        AttachmentDetailRow("医疗诊断单等相关证明材料", record.medicalFileName, record.medicalFileUri, shareStyle = true)
                        SignatureDetailRow(record.signatureStrokes, record.signature, shareStyle = true)
                    }
                }
                item { ShareSectionHeader(record.progressSectionTitle) }
                item { ShareApprovalTimeline(record) }
            }
            // This app-specific action remains fixed as previously requested.
            Column(Modifier.fillMaxWidth().background(Color.White)) {
                HorizontalDivider(thickness = 1.dp, color = LeaveColors.Divider)
                Text(
                    "转发审批表单",
                    Modifier.fillMaxWidth().height(50.dp).clickable(onClick = {})
                        .wrapContentHeight(Alignment.CenterVertically),
                    color = Color(0xFF108EE9), fontSize = 18.sp, textAlign = TextAlign.Center,
                )
            }
        }
        StatusStamp(
            record.status,
            Modifier.align(Alignment.TopEnd).padding(top = 10.dp, end = 15.dp).zIndex(3f),
        )
    }
}

@Composable
private fun ShareSectionHeader(text: String) {
    Row(
        Modifier.fillMaxWidth().height(45.dp).background(Color(0xFFEBF0F3)).padding(start = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.width(3.dp).height(14.dp).background(LeaveColors.BrandBlue))
        Text(text, Modifier.padding(start = 6.dp).semantics { heading() }, LeaveColors.TextPrimary, fontSize = 16.sp)
    }
}

@Composable
private fun DetailScreen(
    record: LeaveRecord,
    onBack: () -> Unit,
    onSecretEntry: () -> Unit,
    onForward: () -> Unit,
) {
    var titleTaps by remember(record.id) { mutableIntStateOf(0) }
    var lastTitleTap by remember(record.id) { mutableLongStateOf(0L) }
    // The original H5 page starts closed. The student number is shown beside the
    // name only while closed, then becomes the first row of the expanded card.
    var applicantExpanded by rememberSaveable(record.id) { mutableStateOf(false) }
    val displayedStudentId = record.studentId.ifBlank { "暂无学号" }
    val applicantInteraction = remember { MutableInteractionSource() }
    Column(Modifier.fillMaxSize().background(Color.White)) {
        BrowserHeader(
            title = record.detailTitle,
            subtitle = "app.uyiban.com",
            onBack = onBack,
            titleClick = {
                val now = android.os.SystemClock.elapsedRealtime()
                titleTaps = if (now - lastTitleTap <= 1_500L) titleTaps + 1 else 1
                lastTitleTap = now
                if (titleTaps >= 5) {
                    titleTaps = 0
                    onSecretEntry()
                }
            },
        )
        Box(Modifier.weight(1f)) {
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    Box(Modifier.fillMaxWidth().height(49.dp)) {
                        Text(
                            record.detailTitle,
                            modifier = Modifier.align(Alignment.Center).semantics { heading() },
                            color = LeaveColors.TextPrimary,
                            fontSize = 18.sp,
                        )
                    }
                }
                item { SectionHeader(record.applicantSectionTitle) }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(53.dp)
                            .padding(end = 22.dp)
                            .clickable(
                                interactionSource = applicantInteraction,
                                indication = null,
                            ) { applicantExpanded = !applicantExpanded },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        DetailRow(
                            "姓名",
                            if (applicantExpanded) record.applicantName
                            else "${record.applicantName}($displayedStudentId)",
                            modifier = Modifier.weight(1f).padding(vertical = 7.dp),
                        )
                        ChevronIndicator(
                            modifier = Modifier.size(22.dp),
                            pointsDown = !applicantExpanded,
                            pointsUp = applicantExpanded,
                        )
                    }
                }
                item {
                    // The original person card opens as a single continuous panel.
                    // Keeping the item mounted lets LazyColumn animate its height instead of jumping.
                    Column {
                        AnimatedVisibility(
                            visible = applicantExpanded,
                            enter = expandVertically(animationSpec = tween(220)) + fadeIn(animationSpec = tween(150)),
                            exit = shrinkVertically(animationSpec = tween(180)) + fadeOut(animationSpec = tween(120)),
                        ) {
                            Column(Modifier.padding(bottom = 8.dp)) {
                                DetailRow("学号", record.studentId)
                                DetailRow("性别", record.gender)
                                DetailRow("学院", record.college)
                                DetailRow("系", record.department)
                                DetailRow("专业", record.major)
                                DetailRow("专业方向", record.majorDirection)
                                DetailRow("班级", record.className)
                                DetailRow("年级", record.grade)
                                DetailRow("教育层级", record.educationLevel)
                                DetailRow("学制", record.schoolingLength)
                            }
                        }
                    }
                }
                item { SectionHeader(record.contentSectionTitle) }
                item {
                    Column(Modifier.padding(vertical = 9.dp)) {
                        DetailRow("审批编号", record.approvalNo)
                        DetailRow("是否离校", record.awayMode)
                        DetailRow("请假类型", record.leaveType)
                        DetailRow("请假原因\n(真实原因)", record.reason)
                        DetailRow("请假去向", record.destination)
                        DetailRow("详细地址\n(具体到乡\n镇街道村\n组)", record.address)
                        DetailRow("本人联系电话", record.phone)
                        DetailRow("开始时间", record.startTime)
                        DetailRow("结束时间", record.endTime)
                        DetailRow("时长(天)", record.duration)
                        DetailRow("班主任/导师姓名", record.teacherName)
                        DetailRow("班主任/导师联系方式", record.teacherPhone)
                        DetailRow("家长（家属）姓名", record.parentName)
                        DetailRow("家长（家属）联系电话", record.parentPhone)
                        DetailRow("紧急联系人", record.emergencyName)
                        DetailRow("紧急联系电话", record.emergencyPhone)
                        AttachmentDetailRow(
                            label = "本人申请书\n(本科生上传)，导师意见（研究生上传)",
                            fileName = record.applicationFileName,
                            uriString = record.applicationFileUri,
                            chatPlaceholder = true,
                        )
                        AttachmentDetailRow(
                            label = "医疗诊断单等相关证明材料",
                            fileName = record.medicalFileName,
                            uriString = record.medicalFileUri,
                        )
                        SignatureDetailRow(record.signatureStrokes, record.signature)
                    }
                }
                item { SectionHeader(record.progressSectionTitle) }
                item { ApprovalTimeline(record) }
            }
            StatusStamp(
                record.status,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    // Fixed overlay: it sits above the scrolling form content.
                    .padding(top = 10.dp, end = 13.dp)
                    .zIndex(3f),
            )
        }
        // The original H5 keeps this action out of the scroll container.
        // It remains at the bottom while both the form and status overlay scroll.
        Column(Modifier.fillMaxWidth().background(Color.White)) {
            HorizontalDivider(thickness = 1.dp, color = LeaveColors.Divider)
            Text(
                text = "转发审批表单",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable(onClick = onForward)
                    .wrapContentHeight(Alignment.CenterVertically),
                color = Color(0xFF108EE9),
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ForwardFormScreen(record: LeaveRecord, onBack: () -> Unit) {
    val start = remember { LocalDateTime.now() }
    val formatter = remember { DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss") }
    val startText = remember(start) { start.format(formatter) }
    val endText = remember(start) { start.plusDays(7).format(formatter) }
    val dividerBrush = remember {
        Brush.horizontalGradient(listOf(Color.Transparent, Color(0xFFFF9E4F), Color.Transparent))
    }
    Column(Modifier.fillMaxSize().background(Color(0xFFF0F5F8))) {
        BrowserHeader(
            title = record.detailTitle,
            subtitle = "app.uyiban.com",
            onBack = onBack,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFEBF0F3))
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("链接有效期为", color = Color(0xFF666666), fontSize = 20.sp)
            Spacer(Modifier.height(40.dp))
            Box(Modifier.width(200.dp).height(1.dp).background(dividerBrush))
            Spacer(Modifier.height(10.dp))
            Text(startText, color = Color(0xFFFF9E4F), fontSize = 18.sp)
            Spacer(Modifier.height(5.dp))
            Text("~", color = Color(0xFFFF9E4F), fontSize = 18.sp)
            Spacer(Modifier.height(5.dp))
            Text(endText, color = Color(0xFFFF9E4F), fontSize = 18.sp)
            Spacer(Modifier.height(10.dp))
            Box(Modifier.width(200.dp).height(1.dp).background(dividerBrush))
            Spacer(Modifier.height(40.dp))
            Image(
                painter = painterResource(R.drawable.forward_qr_placeholder),
                contentDescription = "分享二维码",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(128.dp),
            )
            Spacer(Modifier.height(40.dp))
            Text(
                text = "https://app.uyiban.com/workflow/client/\n#/share?Key=待生成",
                modifier = Modifier.fillMaxWidth(.7f),
                color = Color(0xFF8F8F8F),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = {},
                modifier = Modifier.padding(top = 10.dp).height(48.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF57AAEF)),
            ) {
                Image(
                    painter = painterResource(R.drawable.copy_link_original),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Text("复制链接", modifier = Modifier.padding(start = 5.dp), fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun ShareApprovalTimeline(record: LeaveRecord) {
    Column(
        Modifier.fillMaxWidth().background(Color.White).padding(15.dp),
    ) {
        ShareTimelineStep(1, record.reviewerOne, record.reviewerOneTime)
        ShareTimelineStep(2, record.reviewerTwo, record.reviewerTwoTime)
        // The final H5 flow node contains only this short, pale horizontal cap.
        Box(Modifier.width(24.dp).height(1.dp).background(Color(0xFFDDDDDD)))
    }
}

@Composable
private fun ShareTimelineStep(number: Int, reviewer: String, time: String) {
    Box(Modifier.fillMaxWidth().height(91.dp)) {
        // .flownode:after — green line from below the circle to the end cap.
        Box(
            Modifier.offset(x = 11.dp, y = 26.dp).width(1.dp).height(63.dp)
                .background(LeaveColors.Progress),
        )
        Row(Modifier.fillMaxSize()) {
            Box(Modifier.weight(.10f)) {
                Box(
                    Modifier.size(24.dp).background(LeaveColors.Progress, CircleShape),
                    contentAlignment = Alignment.Center,
                ) { Text(number.toString(), color = Color.White, fontSize = 14.sp) }
                Text(
                    "通过",
                    Modifier.offset(x = 15.dp, y = 67.dp),
                    color = LeaveColors.Progress,
                    fontSize = 12.sp,
                )
            }
            Column(Modifier.weight(.90f).padding(top = 2.dp)) {
                Row(Modifier.fillMaxWidth()) {
                    Text(reviewer, Modifier.weight(1f), color = Color.Black, fontSize = 16.sp)
                    Text(time, color = LeaveColors.TextSecondary, fontSize = 12.sp, modifier = Modifier.padding(top = 3.dp))
                }
                Text("已同意", Modifier.padding(top = 5.dp), color = LeaveColors.Progress, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun ApprovalTimeline(record: LeaveRecord) {
    // This is the measured original H5 flow: two 91px nodes followed by a
    // stand-alone 24px #ddd end cap, aligned to the node's left edge.
    ShareApprovalTimeline(record)
}

@Composable
private fun TimelineStep(
    number: Int,
    reviewer: String,
    time: String,
    showLine: Boolean,
    showEndCap: Boolean = false,
) {
    Row(Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(29.dp).background(LeaveColors.Progress, CircleShape), contentAlignment = Alignment.Center) {
                Text(number.toString(), color = Color.White, fontSize = 14.sp)
            }
            if (showLine) {
                Box(Modifier.width(1.dp).height(82.dp).background(LeaveColors.Progress))
            }
            if (showEndCap) {
                Box(Modifier.width(1.dp).height(42.dp).background(LeaveColors.Progress))
                // Exact tail cap from the original workflow: .nodeend { width:24px; border-top:#ddd }
                Box(Modifier.width(24.dp).height(1.dp).background(Color(0xFFDDDDDD)))
            }
        }
        Column(Modifier.weight(1f).padding(start = 12.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Text(reviewer, modifier = Modifier.weight(1f), fontSize = 15.sp, fontWeight = FontWeight.Medium)
                Text(time, color = LeaveColors.TextSecondary, fontSize = 13.sp)
            }
            Text("已同意", modifier = Modifier.padding(top = 7.dp), color = LeaveColors.Progress, fontSize = 14.sp)
            Text("通过", modifier = Modifier.padding(top = 10.dp), color = LeaveColors.Progress, fontSize = 13.sp)
        }
    }
}

@Composable
private fun SecretManagerScreen(
    records: List<LeaveRecord>,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    var pendingDelete by remember { mutableStateOf<LeaveRecord?>(null) }
    Column(Modifier.fillMaxSize().background(LeaveColors.PageBackground)) {
        BrowserHeader(title = "自定义管理", subtitle = "隐藏入口", onBack = onBack)
        Text(
            "这里的修改只保存在本机。可新增记录，也可修改详情页所有文字和状态。",
            modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp),
            color = LeaveColors.TextSecondary,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        )
        LazyColumn(Modifier.weight(1f), contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(records, key = { it.id }) { record ->
                Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(8.dp)) {
                    Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(record.detailTitle, fontWeight = FontWeight.Medium, fontSize = 17.sp)
                            Text("${record.applicantName} · ${record.status.displayName}", modifier = Modifier.padding(top = 5.dp), color = LeaveColors.TextSecondary, fontSize = 14.sp)
                        }
                        IconButton(onClick = { onEdit(record.id) }) {
                            Icon(Icons.Rounded.Edit, contentDescription = "编辑${record.detailTitle}", tint = LeaveColors.IconBlue)
                        }
                        IconButton(onClick = { pendingDelete = record }) {
                            Icon(Icons.Rounded.Delete, contentDescription = "删除${record.detailTitle}", tint = Color(0xFFE06B65))
                        }
                    }
                }
            }
        }
        Button(
            onClick = onAdd,
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().height(52.dp),
            shape = RoundedCornerShape(0.dp),
        ) {
            Icon(Icons.Rounded.Add, contentDescription = null)
            Text("新增请假记录", modifier = Modifier.padding(start = 8.dp), fontSize = 17.sp)
        }
    }
    pendingDelete?.let { record ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text("删除这条记录？") },
            text = { Text("“${record.detailTitle}”删除后无法恢复。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(record.id)
                        pendingDelete = null
                    },
                ) { Text("删除", color = Color(0xFFE06B65)) }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) { Text("取消") }
            },
        )
    }
}
