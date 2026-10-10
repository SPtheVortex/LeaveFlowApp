package dev.h4ze.leaveflow

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.util.Locale

fun Context.displayName(uri: Uri): String {
    contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
        val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (index >= 0 && cursor.moveToFirst()) return cursor.getString(index)
    }
    return uri.lastPathSegment ?: "已选择文件"
}

fun Context.persistReadPermission(uri: Uri) {
    runCatching {
        contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
}

@Composable
fun AttachmentDetailRow(
    label: String,
    fileName: String,
    uriString: String,
    modifier: Modifier = Modifier,
    chatPlaceholder: Boolean = false,
    shareStyle: Boolean = false,
) {
    val context = LocalContext.current
    val uri = remember(uriString) { uriString.takeIf(String::isNotBlank)?.let(Uri::parse) }
    val mime = remember(uriString) {
        runCatching { uri?.let(context.contentResolver::getType) }.getOrNull().orEmpty()
    }
    var showImagePreview by remember(uriString) { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (shareStyle) Modifier.padding(top = 10.dp) else Modifier.padding(horizontal = 20.dp, vertical = 8.dp)),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = label,
            modifier = if (shareStyle) Modifier.weight(.22f) else Modifier.width(84.dp),
            color = if (shareStyle) Color(0xFF888888) else LeaveColors.TextSecondary,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.End,
        )
        Box(
            modifier = Modifier
                .weight(if (shareStyle) .78f else 1f)
                .padding(start = if (shareStyle) 10.dp else 12.dp)
                .clickable(
                    enabled = uri != null,
                    role = Role.Button,
                    onClick = {
                        if (mime.startsWith("image/")) showImagePreview = true
                        else uri?.let { openFile(context, it, mime) }
                    },
                ),
        ) {
            when {
                uri != null && mime.startsWith("image/") -> {
                    UriImage(uri, Modifier.size(if (shareStyle) 100.dp else 92.dp).clip(RoundedCornerShape(2.dp)))
                }
                uri != null -> {
                    FilePreviewCard(fileName.ifBlank { "已上传文件" })
                }
                chatPlaceholder -> {
                    SampleChatThumbnail(Modifier.size(if (shareStyle) 100.dp else 92.dp))
                }
                else -> {
                    Text(
                        text = fileName.ifBlank { "未上传" },
                        color = if (fileName.isBlank()) LeaveColors.TextSecondary else LeaveColors.IconBlue,
                        fontSize = 15.sp,
                        lineHeight = 21.sp,
                    )
                }
            }
        }
    }

    if (showImagePreview && uri != null) {
        ImagePreviewDialog(uri = uri, title = fileName, onDismiss = { showImagePreview = false })
    }
}

@Composable
private fun FilePreviewCard(fileName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, LeaveColors.Divider, RoundedCornerShape(5.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ReferenceIconArtwork(ReferenceIcon.APPLICATION, LeaveColors.IconBlue, Modifier.size(29.dp))
        Text(fileName, modifier = Modifier.weight(1f).padding(start = 9.dp), color = LeaveColors.IconBlue, fontSize = 14.sp, lineHeight = 19.sp)
    }
}

@Composable
private fun SampleChatThumbnail(modifier: Modifier = Modifier) {
    Canvas(modifier.background(Color(0xFFF7F8FA), RoundedCornerShape(2.dp))) {
        drawCircle(Color(0xFF7BCB6C), radius = size.minDimension * .08f, center = Offset(size.width * .12f, size.height * .18f))
        drawRoundRect(Color(0xFFE8E9EC), Offset(size.width * .22f, size.height * .10f), androidx.compose.ui.geometry.Size(size.width * .36f, size.height * .16f), androidx.compose.ui.geometry.CornerRadius(5f))
        drawRoundRect(Color(0xFF55B8F0), Offset(size.width * .16f, size.height * .37f), androidx.compose.ui.geometry.Size(size.width * .68f, size.height * .18f), androidx.compose.ui.geometry.CornerRadius(5f))
        drawRoundRect(Color(0xFF55B8F0), Offset(size.width * .25f, size.height * .60f), androidx.compose.ui.geometry.Size(size.width * .58f, size.height * .18f), androidx.compose.ui.geometry.CornerRadius(5f))
        drawCircle(Color(0xFFF0BE67), radius = size.minDimension * .07f, center = Offset(size.width * .9f, size.height * .7f))
    }
}

@Composable
private fun ImagePreviewDialog(uri: Uri, title: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxSize(), color = Color(0xE6000000)) {
            Column(Modifier.fillMaxSize()) {
                Row(
                    Modifier.fillMaxWidth().background(Color.Black.copy(alpha = .55f)).padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(title.ifBlank { "图片预览" }, modifier = Modifier.weight(1f), color = Color.White, fontSize = 16.sp, maxLines = 1)
                    Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = .16f))) {
                        Text("关闭", color = Color.White)
                    }
                }
                UriImage(uri, Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun UriImage(uri: Uri, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                adjustViewBounds = true
            }
        },
        update = { imageView ->
            runCatching {
                imageView.setImageDrawable(null)
                imageView.setImageURI(uri)
            }
        },
        modifier = modifier.background(Color.White),
    )
}

private fun openFile(context: Context, uri: Uri, mime: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, mime.ifBlank { "*/*" })
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "手机中没有可预览该文件的应用", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun SignatureDisplay(
    strokes: String,
    fallbackText: String,
    modifier: Modifier = Modifier,
) {
    val decoded = remember(strokes) { decodeSignature(strokes) }
    Box(modifier, contentAlignment = Alignment.CenterStart) {
        if (decoded.isNotEmpty()) {
            SignatureLines(decoded, Modifier.fillMaxSize())
        } else {
            Text(
                fallbackText,
                modifier = Modifier.rotate(-8f).padding(start = 12.dp),
                color = LeaveColors.TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@Composable
fun SignatureDetailRow(
    strokes: String,
    fallbackText: String,
    modifier: Modifier = Modifier,
    shareStyle: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (shareStyle) Modifier.padding(top = 10.dp) else Modifier.padding(horizontal = 20.dp, vertical = 8.dp)),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            "手写签名",
            modifier = if (shareStyle) Modifier.weight(.22f) else Modifier.width(84.dp),
            color = if (shareStyle) Color(0xFF888888) else LeaveColors.TextSecondary,
            fontSize = 15.sp,
            textAlign = TextAlign.End,
        )
        SignatureDisplay(
            strokes = strokes,
            fallbackText = fallbackText,
            modifier = (if (shareStyle) Modifier.weight(.78f, fill = false) else Modifier)
                .padding(start = if (shareStyle) 10.dp else 12.dp)
                .size(if (shareStyle) 100.dp else 132.dp, if (shareStyle) 100.dp else 86.dp),
        )
    }
}

@Composable
fun SignaturePad(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var strokes by remember { mutableStateOf(decodeSignature(value)) }
    Box(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, LeaveColors.Divider, RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { point ->
                        val normalized = Offset(point.x / size.width, point.y / size.height)
                        strokes = strokes + listOf(listOf(normalized))
                        onValueChange(encodeSignature(strokes))
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        val normalized = Offset(
                            (change.position.x / size.width).coerceIn(0f, 1f),
                            (change.position.y / size.height).coerceIn(0f, 1f),
                        )
                        if (strokes.isNotEmpty()) {
                            strokes = strokes.dropLast(1) + listOf(strokes.last() + normalized)
                            onValueChange(encodeSignature(strokes))
                        }
                    },
                )
            },
    ) {
        SignatureLines(strokes, Modifier.fillMaxSize())
        if (strokes.isEmpty()) {
            Text("请在此处手写签名", modifier = Modifier.align(Alignment.Center), color = LeaveColors.TextSecondary, fontSize = 14.sp)
        }
    }
}

@Composable
private fun SignatureLines(strokes: List<List<Offset>>, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        strokes.forEach { points ->
            if (points.size == 1) {
                drawCircle(LeaveColors.TextPrimary, radius = 1.5.dp.toPx(), center = Offset(points[0].x * size.width, points[0].y * size.height))
            } else {
                val path = Path().apply {
                    moveTo(points.first().x * size.width, points.first().y * size.height)
                    points.drop(1).forEach { lineTo(it.x * size.width, it.y * size.height) }
                }
                drawPath(path, LeaveColors.TextPrimary, style = Stroke(2.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            }
        }
    }
}

private fun decodeSignature(encoded: String): List<List<Offset>> = encoded
    .split('|')
    .mapNotNull { stroke ->
        stroke.split(';').mapNotNull { point ->
            val parts = point.split(',')
            if (parts.size != 2) null
            else runCatching { Offset(parts[0].toFloat(), parts[1].toFloat()) }.getOrNull()
        }.takeIf { it.isNotEmpty() }
    }

private fun encodeSignature(strokes: List<List<Offset>>): String = strokes.joinToString("|") { stroke ->
    stroke.joinToString(";") { point ->
        String.format(Locale.US, "%.4f,%.4f", point.x, point.y)
    }
}
