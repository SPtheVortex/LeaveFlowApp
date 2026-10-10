package dev.h4ze.leaveflow

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecordEditorScreen(
    initial: LeaveRecord?,
    secretMode: Boolean,
    onBack: () -> Unit,
    onSave: (LeaveRecord) -> Unit,
) {
    var draft by remember(initial?.id) { mutableStateOf(initial ?: LeaveRecord.blank()) }
    val context = LocalContext.current
    val applicationFilePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            context.persistReadPermission(it)
            draft = draft.copy(applicationFileUri = it.toString(), applicationFileName = context.displayName(it))
        }
    }
    val medicalFilePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            context.persistReadPermission(it)
            draft = draft.copy(medicalFileUri = it.toString(), medicalFileName = context.displayName(it))
        }
    }
    Column(Modifier.fillMaxSize().background(LeaveColors.PageBackground)) {
        BrowserHeader(
            title = if (initial == null) "新增请假" else "编辑请假",
            subtitle = if (secretMode) "自定义管理" else "请假申请",
            onBack = onBack,
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item { EditorGroupTitle(if (secretMode) "页面文案" else "基本信息") }
            if (secretMode) {
                item { EditorField("详情页标题", draft.detailTitle) { draft = draft.copy(detailTitle = it) } }
                item { EditorField("申请人分区标题", draft.applicantSectionTitle) { draft = draft.copy(applicantSectionTitle = it) } }
                item { EditorField("申请内容分区标题", draft.contentSectionTitle) { draft = draft.copy(contentSectionTitle = it) } }
                item { EditorField("审批进度分区标题", draft.progressSectionTitle) { draft = draft.copy(progressSectionTitle = it) } }
            }
            item { EditorField("姓名", draft.applicantName) { draft = draft.copy(applicantName = it) } }
            item { EditorField("学号", draft.studentId) { draft = draft.copy(studentId = it) } }
            if (secretMode) {
                item { EditorGroupTitle("申请人展开资料") }
                item { EditorField("性别", draft.gender) { draft = draft.copy(gender = it) } }
                item { EditorField("学院", draft.college) { draft = draft.copy(college = it) } }
                item { EditorField("系", draft.department) { draft = draft.copy(department = it) } }
                item { EditorField("专业", draft.major) { draft = draft.copy(major = it) } }
                item { EditorField("专业方向", draft.majorDirection) { draft = draft.copy(majorDirection = it) } }
                item { EditorField("班级", draft.className) { draft = draft.copy(className = it) } }
                item { EditorField("年级", draft.grade) { draft = draft.copy(grade = it) } }
                item { EditorField("教育层级", draft.educationLevel) { draft = draft.copy(educationLevel = it) } }
                item { EditorField("学制", draft.schoolingLength) { draft = draft.copy(schoolingLength = it) } }
            }
            item { EditorField("申请日期", draft.submittedDate, "例如 2026-09-14") { draft = draft.copy(submittedDate = it) } }
            item { EditorField("审批编号", draft.approvalNo) { draft = draft.copy(approvalNo = it) } }

            item { EditorGroupTitle("请假内容") }
            item { EditorField("是否离校", draft.awayMode) { draft = draft.copy(awayMode = it) } }
            item { EditorField("请假类型", draft.leaveType) { draft = draft.copy(leaveType = it) } }
            item { EditorField("请假原因", draft.reason, minLines = 3) { draft = draft.copy(reason = it) } }
            item { EditorField("请假去向", draft.destination) { draft = draft.copy(destination = it) } }
            item { EditorField("详细地址", draft.address, minLines = 2) { draft = draft.copy(address = it) } }
            item { EditorField("本人联系电话", draft.phone) { draft = draft.copy(phone = it) } }
            item { EditorField("开始时间", draft.startTime, "例如 2026-09-15 12:00") { draft = draft.copy(startTime = it) } }
            item { EditorField("结束时间", draft.endTime, "例如 2026-09-16 20:00") { draft = draft.copy(endTime = it) } }
            item { EditorField("时长（天）", draft.duration) { draft = draft.copy(duration = it) } }

            item { EditorGroupTitle("联系人") }
            item { EditorField("班主任/导师姓名", draft.teacherName) { draft = draft.copy(teacherName = it) } }
            item { EditorField("班主任/导师联系方式", draft.teacherPhone) { draft = draft.copy(teacherPhone = it) } }
            item { EditorField("家长（家属）姓名", draft.parentName) { draft = draft.copy(parentName = it) } }
            item { EditorField("家长（家属）联系电话", draft.parentPhone) { draft = draft.copy(parentPhone = it) } }
            item { EditorField("紧急联系人", draft.emergencyName) { draft = draft.copy(emergencyName = it) } }
            item { EditorField("紧急联系电话", draft.emergencyPhone) { draft = draft.copy(emergencyPhone = it) } }

            item { EditorGroupTitle("附件与销假") }
            item {
                AttachmentPicker(
                    title = "本人申请书 / 导师意见",
                    fileName = draft.applicationFileName,
                    onChoose = { applicationFilePicker.launch(arrayOf("*/*")) },
                    onClear = { draft = draft.copy(applicationFileUri = "", applicationFileName = "") },
                )
            }
            item {
                AttachmentPicker(
                    title = "医疗诊断单等证明材料",
                    fileName = draft.medicalFileName,
                    onChoose = { medicalFilePicker.launch(arrayOf("*/*")) },
                    onClear = { draft = draft.copy(medicalFileUri = "", medicalFileName = "") },
                )
            }
            item {
                Column(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(8.dp)).padding(14.dp)) {
                    Text("手写签名", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    SignaturePad(
                        value = draft.signatureStrokes,
                        onValueChange = { draft = draft.copy(signatureStrokes = it) },
                        modifier = Modifier.fillMaxWidth().height(168.dp).padding(top = 10.dp),
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { draft = draft.copy(signatureStrokes = "") }) { Text("清空重写") }
                    }
                    EditorField("签名备用文字", draft.signature) { draft = draft.copy(signature = it) }
                }
            }
            item { EditorField("代销假人", draft.proxyName) { draft = draft.copy(proxyName = it) } }
            item { EditorField("代销假时间", draft.proxyTime) { draft = draft.copy(proxyTime = it) } }

            item { EditorGroupTitle("审批进度") }
            item { EditorField("审批人 1", draft.reviewerOne) { draft = draft.copy(reviewerOne = it) } }
            item { EditorField("审批时间 1", draft.reviewerOneTime) { draft = draft.copy(reviewerOneTime = it) } }
            item { EditorField("审批人 2", draft.reviewerTwo) { draft = draft.copy(reviewerTwo = it) } }
            item { EditorField("审批时间 2", draft.reviewerTwoTime) { draft = draft.copy(reviewerTwoTime = it) } }
            item {
                Column(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(8.dp)).padding(14.dp)) {
                    Text("右上角请假状态", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    Row(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilterChip(
                            selected = draft.status == LeaveStatus.APPROVED,
                            onClick = { draft = draft.copy(status = LeaveStatus.APPROVED) },
                            label = { Text("审批通过（动态）") },
                        )
                        FilterChip(
                            selected = draft.status == LeaveStatus.ENDED,
                            onClick = { draft = draft.copy(status = LeaveStatus.ENDED) },
                            label = { Text("结束请假（静态）") },
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
        Button(
            onClick = { onSave(draft) },
            enabled = draft.applicantName.isNotBlank() && draft.detailTitle.isNotBlank(),
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().height(52.dp),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LeaveColors.ActionBlue),
        ) {
            Text("保存", color = Color.White, fontSize = 18.sp)
        }
    }
}

@Composable
private fun AttachmentPicker(
    title: String,
    fileName: String,
    onChoose: () -> Unit,
    onClear: () -> Unit,
) {
    Column(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(8.dp)).padding(14.dp)) {
        Text(title, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        Text(
            fileName.ifBlank { "尚未选择文件" },
            modifier = Modifier.padding(top = 7.dp),
            color = if (fileName.isBlank()) LeaveColors.TextSecondary else LeaveColors.IconBlue,
            fontSize = 14.sp,
            lineHeight = 19.sp,
        )
        Row(Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.End) {
            if (fileName.isNotBlank()) {
                TextButton(onClick = onClear) { Text("移除") }
            }
            TextButton(onClick = onChoose) { Text(if (fileName.isBlank()) "选择图片或文件" else "替换") }
        }
    }
}

@Composable
private fun EditorGroupTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 2.dp),
        color = LeaveColors.TextPrimary,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun EditorField(
    label: String,
    value: String,
    placeholder: String = "",
    minLines: Int = 1,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(8.dp)),
        label = { Text(label) },
        placeholder = { if (placeholder.isNotEmpty()) Text(placeholder) },
        minLines = minLines,
        shape = RoundedCornerShape(8.dp),
    )
}
