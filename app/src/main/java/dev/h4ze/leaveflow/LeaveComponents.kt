package dev.h4ze.leaveflow

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChevronIndicator(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFB8BBC0),
    pointsDown: Boolean = false,
    pointsUp: Boolean = false,
) {
    Canvas(modifier) {
        val stroke = 2.2.dp.toPx()
        if (pointsDown) {
            drawLine(color, Offset(size.width * .14f, size.height * .3f), Offset(size.width * .5f, size.height * .68f), stroke, StrokeCap.Round)
            drawLine(color, Offset(size.width * .5f, size.height * .68f), Offset(size.width * .86f, size.height * .3f), stroke, StrokeCap.Round)
        } else if (pointsUp) {
            drawLine(color, Offset(size.width * .14f, size.height * .7f), Offset(size.width * .5f, size.height * .32f), stroke, StrokeCap.Round)
            drawLine(color, Offset(size.width * .5f, size.height * .32f), Offset(size.width * .86f, size.height * .7f), stroke, StrokeCap.Round)
        } else {
            drawLine(color, Offset(size.width * .3f, size.height * .14f), Offset(size.width * .68f, size.height * .5f), stroke, StrokeCap.Round)
            drawLine(color, Offset(size.width * .68f, size.height * .5f), Offset(size.width * .3f, size.height * .86f), stroke, StrokeCap.Round)
        }
    }
}

@Composable
fun BrowserHeader(
    title: String,
    subtitle: String,
    onBack: (() -> Unit)?,
    modifier: Modifier = Modifier,
    showClose: Boolean = true,
    titleClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(LeaveColors.BrandBlue)
            .statusBarsPadding()
            .height(48.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { onBack?.invoke() }, enabled = onBack != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = if (onBack != null) "返回" else null,
                tint = if (onBack != null) Color.White else Color.Transparent,
                modifier = Modifier.size(34.dp),
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .then(if (titleClick != null) Modifier.clickable(onClick = titleClick) else Modifier),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 20.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.92f),
                fontSize = 14.sp,
                lineHeight = 17.sp,
                textAlign = TextAlign.Center,
            )
        }
        if (showClose) {
            IconButton(onClick = { onBack?.invoke() }) {
                Icon(Icons.Rounded.Close, contentDescription = "关闭", tint = Color.White, modifier = Modifier.size(30.dp))
            }
        }
        IconButton(onClick = {}) {
            Icon(Icons.Rounded.MoreVert, contentDescription = "更多", tint = Color.White, modifier = Modifier.size(29.dp))
        }
    }
}

@Composable
fun SectionHeader(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(LeaveColors.SectionBackground)
            .height(44.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .width(4.dp)
                .height(18.dp)
                .background(LeaveColors.IconBlue, RoundedCornerShape(2.dp)),
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp),
            color = LeaveColors.TextPrimary,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = LeaveColors.TextPrimary,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = label,
            modifier = Modifier.width(84.dp),
            color = LeaveColors.TextSecondary,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.End,
        )
        Text(
            text = value,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            color = valueColor,
            fontSize = 15.sp,
            lineHeight = 22.sp,
        )
    }
}

/** Exact row geometry from the public workflow share page stylesheet. */
@Composable
fun ShareDetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Color.Black,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(.22f),
            color = Color(0xFF888888),
            fontSize = 15.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.End,
        )
        Text(
            text = value,
            modifier = Modifier
                .weight(.78f)
                .padding(start = 10.dp),
            color = valueColor,
            fontSize = 16.sp,
            lineHeight = 22.sp,
        )
    }
}

@Composable
fun StatusStamp(
    status: LeaveStatus,
    modifier: Modifier = Modifier,
) {
    val starRotation = if (status == LeaveStatus.APPROVED) {
        val transition = rememberInfiniteTransition(label = "approval_stamp")
        val angle by transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                // Extracted from the original workflow CSS: rotate 10s linear infinite.
                animation = tween(durationMillis = 10_000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "workflow_star_orbit",
        )
        angle
    } else 0f

    Box(
        modifier = Modifier
            // Measured from the original Xiaomi 15 Pro detail page.
            .size(
                width = if (status == LeaveStatus.APPROVED) 104.dp else 118.dp,
                height = if (status == LeaveStatus.APPROVED) 85.dp else 91.dp,
            )
            .then(modifier)
            .semantics { contentDescription = status.displayName },
        contentAlignment = Alignment.Center,
    ) {
        if (status == LeaveStatus.APPROVED) {
            // These two PNGs are the exact public assets used by the H5 workflow.
            Image(
                painter = painterResource(R.drawable.status_star_back),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),
            )
            Image(
                painter = painterResource(R.drawable.status_star_orbit),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationZ = starRotation },
            )
        } else {
            // Exact transparent artwork shipped by the original workflow H5.
            Image(
                painter = painterResource(R.drawable.status_ended_original),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun SquareMenuItem(
    label: String,
    icon: ReferenceIcon,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .sizeIn(minWidth = 76.dp, minHeight = 96.dp)
            .then(
                if (onClick != null) Modifier
                    .clickable(role = Role.Button, onClick = onClick)
                    .semantics { role = Role.Button }
                else Modifier,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(tint.copy(alpha = .18f), RoundedCornerShape(11.dp)),
            contentAlignment = Alignment.Center,
        ) {
            ReferenceIconArtwork(
                type = icon,
                tint = tint,
                modifier = Modifier.size(38.dp),
            )
        }
        Spacer(Modifier.height(7.dp))
        Text(text = label, color = LeaveColors.TextPrimary, fontSize = 15.sp, textAlign = TextAlign.Center)
    }
}
