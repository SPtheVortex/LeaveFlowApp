package dev.h4ze.leaveflow

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

enum class ReferenceIcon {
    MESSAGE,
    WORKBENCH,
    PROFILE_CARD,
    SERVICE_HALL,
    LEAVE,
    FLOW,
    CHECK_IN,
    QUIZ,
    ACTIVITY,
    APPLICATION,
    ACCOUNT,
}

@Composable
fun ReferenceIconArtwork(
    type: ReferenceIcon,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier) {
        when (type) {
            ReferenceIcon.MESSAGE -> drawMessageIcon(tint)
            ReferenceIcon.WORKBENCH -> drawWorkbenchIcon(tint)
            ReferenceIcon.PROFILE_CARD -> drawProfileCardIcon(tint)
            ReferenceIcon.SERVICE_HALL -> drawServiceHallIcon(tint)
            ReferenceIcon.LEAVE -> drawLeaveIcon(tint)
            ReferenceIcon.FLOW -> drawFlowIcon(tint)
            ReferenceIcon.CHECK_IN -> drawCheckInIcon(tint)
            ReferenceIcon.QUIZ -> drawQuizIcon(tint)
            ReferenceIcon.ACTIVITY -> drawActivityIcon(tint)
            ReferenceIcon.APPLICATION -> drawApplicationIcon(tint)
            ReferenceIcon.ACCOUNT -> drawAccountIcon(tint)
        }
    }
}

private fun DrawScope.drawMessageIcon(color: Color) {
    val u = size.minDimension / 100f
    val stroke = Stroke(6f * u, cap = StrokeCap.Round, join = StrokeJoin.Round)
    val bubble = Path().apply {
        moveTo(25f * u, 18f * u)
        cubicTo(10f * u, 18f * u, 8f * u, 32f * u, 8f * u, 48f * u)
        cubicTo(8f * u, 67f * u, 22f * u, 77f * u, 39f * u, 80f * u)
        lineTo(35f * u, 94f * u)
        lineTo(54f * u, 80f * u)
        lineTo(74f * u, 80f * u)
        cubicTo(90f * u, 80f * u, 96f * u, 64f * u, 96f * u, 48f * u)
        cubicTo(96f * u, 30f * u, 84f * u, 18f * u, 68f * u, 18f * u)
        close()
    }
    drawPath(bubble, color, style = stroke)
    listOf(35f, 50f, 65f).forEach { y ->
        drawLine(color, Offset(31f * u, y * u), Offset(70f * u, y * u), 5f * u, StrokeCap.Round)
    }
}

private fun DrawScope.drawWorkbenchIcon(color: Color) {
    val u = size.minDimension / 100f
    val path = Path().apply {
        moveTo(8f * u, 31f * u)
        lineTo(8f * u, 82f * u)
        quadraticTo(8f * u, 91f * u, 18f * u, 91f * u)
        lineTo(86f * u, 91f * u)
        quadraticTo(94f * u, 91f * u, 94f * u, 82f * u)
        lineTo(94f * u, 34f * u)
        quadraticTo(94f * u, 26f * u, 84f * u, 26f * u)
        lineTo(54f * u, 26f * u)
        lineTo(45f * u, 14f * u)
        lineTo(18f * u, 14f * u)
        quadraticTo(8f * u, 14f * u, 8f * u, 24f * u)
        close()
    }
    drawPath(path, color, style = Stroke(6f * u, cap = StrokeCap.Round, join = StrokeJoin.Round))
    drawLine(color, Offset(10f * u, 42f * u), Offset(92f * u, 42f * u), 5f * u, StrokeCap.Round)
}

private fun DrawScope.drawProfileCardIcon(color: Color) {
    val u = size.minDimension / 100f
    drawRoundRect(color, Offset(11f * u, 20f * u), Size(78f * u, 58f * u), CornerRadius(7f * u), style = Stroke(7f * u))
    drawCircle(color, 10f * u, Offset(34f * u, 42f * u), style = Stroke(5f * u))
    drawArc(color, 195f, 150f, false, Offset(20f * u, 47f * u), Size(28f * u, 24f * u), style = Stroke(5f * u, cap = StrokeCap.Round))
    drawLine(color, Offset(54f * u, 36f * u), Offset(78f * u, 36f * u), 5f * u, StrokeCap.Round)
    drawLine(color, Offset(54f * u, 50f * u), Offset(73f * u, 50f * u), 5f * u, StrokeCap.Round)
    drawCircle(Color.White, 13f * u, Offset(78f * u, 72f * u))
    drawCircle(color, 12f * u, Offset(78f * u, 72f * u), style = Stroke(4f * u))
    drawLine(color, Offset(72f * u, 72f * u), Offset(77f * u, 77f * u), 4f * u, StrokeCap.Round)
    drawLine(color, Offset(77f * u, 77f * u), Offset(86f * u, 65f * u), 4f * u, StrokeCap.Round)
}

private fun DrawScope.drawServiceHallIcon(color: Color) {
    val u = size.minDimension / 100f
    val stroke = Stroke(5f * u, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawRoundRect(color, Offset(20f * u, 10f * u), Size(55f * u, 74f * u), CornerRadius(5f * u), style = stroke)
    drawLine(color, Offset(30f * u, 27f * u), Offset(64f * u, 27f * u), 5f * u, StrokeCap.Round)
    drawLine(color, Offset(30f * u, 41f * u), Offset(58f * u, 41f * u), 5f * u, StrokeCap.Round)
    drawLine(color, Offset(30f * u, 55f * u), Offset(52f * u, 55f * u), 5f * u, StrokeCap.Round)
    val hand = Path().apply {
        moveTo(43f * u, 67f * u)
        cubicTo(55f * u, 61f * u, 59f * u, 66f * u, 65f * u, 70f * u)
        lineTo(84f * u, 73f * u)
        lineTo(90f * u, 84f * u)
        lineTo(70f * u, 91f * u)
        lineTo(47f * u, 83f * u)
    }
    drawPath(hand, color, style = stroke)
}

private fun DrawScope.drawLeaveIcon(color: Color) {
    val u = size.minDimension / 100f
    val stroke = Stroke(5f * u, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawCircle(color, 11f * u, Offset(66f * u, 29f * u), style = stroke)
    drawArc(color, 185f, 170f, false, Offset(50f * u, 41f * u), Size(32f * u, 37f * u), style = stroke)
    val megaphone = Path().apply {
        moveTo(12f * u, 40f * u)
        lineTo(39f * u, 30f * u)
        lineTo(39f * u, 68f * u)
        lineTo(12f * u, 58f * u)
        close()
    }
    drawPath(megaphone, color, style = stroke)
    drawLine(color, Offset(18f * u, 59f * u), Offset(23f * u, 79f * u), 6f * u, StrokeCap.Round)
    drawLine(color, Offset(46f * u, 36f * u), Offset(55f * u, 27f * u), 4f * u, StrokeCap.Round)
    drawLine(color, Offset(47f * u, 49f * u), Offset(57f * u, 49f * u), 4f * u, StrokeCap.Round)
}

private fun DrawScope.drawFlowIcon(color: Color) {
    val u = size.minDimension / 100f
    val stroke = Stroke(5f * u, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawRoundRect(color, Offset(18f * u, 10f * u), Size(59f * u, 78f * u), CornerRadius(6f * u), style = stroke)
    drawCircle(color, 8f * u, Offset(48f * u, 28f * u), style = Stroke(4f * u))
    drawLine(color, Offset(43f * u, 28f * u), Offset(47f * u, 32f * u), 3f * u, StrokeCap.Round)
    drawLine(color, Offset(47f * u, 32f * u), Offset(54f * u, 23f * u), 3f * u, StrokeCap.Round)
    drawLine(color, Offset(29f * u, 51f * u), Offset(64f * u, 51f * u), 5f * u, StrokeCap.Round)
    drawLine(color, Offset(29f * u, 65f * u), Offset(55f * u, 65f * u), 5f * u, StrokeCap.Round)
    drawCircle(Color.White, 13f * u, Offset(76f * u, 74f * u))
    drawCircle(color, 12f * u, Offset(76f * u, 74f * u), style = Stroke(4f * u))
    drawLine(color, Offset(70f * u, 74f * u), Offset(75f * u, 79f * u), 4f * u, StrokeCap.Round)
    drawLine(color, Offset(75f * u, 79f * u), Offset(84f * u, 67f * u), 4f * u, StrokeCap.Round)
}

private fun DrawScope.drawCheckInIcon(color: Color) {
    val u = size.minDimension / 100f
    val stroke = Stroke(6f * u, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawRoundRect(color, Offset(11f * u, 20f * u), Size(78f * u, 69f * u), CornerRadius(7f * u), style = stroke)
    drawLine(color, Offset(11f * u, 40f * u), Offset(89f * u, 40f * u), 5f * u)
    drawLine(color, Offset(29f * u, 10f * u), Offset(29f * u, 29f * u), 7f * u, StrokeCap.Round)
    drawLine(color, Offset(72f * u, 10f * u), Offset(72f * u, 29f * u), 7f * u, StrokeCap.Round)
    drawLine(color, Offset(29f * u, 65f * u), Offset(44f * u, 76f * u), 7f * u, StrokeCap.Round)
    drawLine(color, Offset(44f * u, 76f * u), Offset(73f * u, 51f * u), 7f * u, StrokeCap.Round)
}

private fun DrawScope.drawQuizIcon(color: Color) {
    val u = size.minDimension / 100f
    val stroke = Stroke(5f * u, cap = StrokeCap.Round, join = StrokeJoin.Round)
    val cap = Path().apply {
        moveTo(7f * u, 35f * u)
        lineTo(50f * u, 16f * u)
        lineTo(94f * u, 35f * u)
        lineTo(50f * u, 54f * u)
        close()
    }
    drawPath(cap, color, style = stroke)
    drawLine(color, Offset(22f * u, 42f * u), Offset(22f * u, 69f * u), 4f * u)
    drawCircle(color, 5f * u, Offset(22f * u, 75f * u))
    drawArc(color, 0f, 180f, false, Offset(28f * u, 43f * u), Size(44f * u, 35f * u), style = stroke)
}

private fun DrawScope.drawActivityIcon(color: Color) {
    val u = size.minDimension / 100f
    val stroke = Stroke(4.5f * u, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawOval(color, Offset(13f * u, 12f * u), Size(34f * u, 44f * u), style = stroke)
    drawOval(color, Offset(48f * u, 24f * u), Size(36f * u, 45f * u), style = stroke)
    drawCircle(Color.White.copy(alpha = .65f), 3f * u, Offset(24f * u, 25f * u))
    drawCircle(Color.White.copy(alpha = .65f), 3f * u, Offset(60f * u, 37f * u))
    drawLine(color, Offset(30f * u, 56f * u), Offset(48f * u, 92f * u), 4f * u, StrokeCap.Round)
    drawLine(color, Offset(66f * u, 69f * u), Offset(48f * u, 92f * u), 4f * u, StrokeCap.Round)
}

private fun DrawScope.drawApplicationIcon(color: Color) {
    val u = size.minDimension / 100f
    val stroke = Stroke(5f * u, cap = StrokeCap.Round, join = StrokeJoin.Round)
    val doc = Path().apply {
        moveTo(17f * u, 12f * u)
        lineTo(62f * u, 12f * u)
        lineTo(79f * u, 29f * u)
        lineTo(79f * u, 88f * u)
        lineTo(17f * u, 88f * u)
        close()
    }
    drawPath(doc, color, style = stroke)
    drawLine(color, Offset(31f * u, 35f * u), Offset(59f * u, 35f * u), 4f * u, StrokeCap.Round)
    drawLine(color, Offset(31f * u, 49f * u), Offset(54f * u, 49f * u), 4f * u, StrokeCap.Round)
    drawLine(color, Offset(76f * u, 74f * u), Offset(93f * u, 74f * u), 5f * u, StrokeCap.Round)
    drawLine(color, Offset(84f * u, 65f * u), Offset(84f * u, 88f * u), 5f * u, StrokeCap.Round)
}

private fun DrawScope.drawAccountIcon(color: Color) {
    val u = size.minDimension / 100f
    drawCircle(color, 15f * u, Offset(50f * u, 34f * u))
    drawArc(color, 190f, 160f, true, Offset(20f * u, 51f * u), Size(60f * u, 42f * u))
}
