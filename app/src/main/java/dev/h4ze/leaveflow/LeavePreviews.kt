package dev.h4ze.leaveflow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(name = "全部工作台图标", widthDp = 430, showBackground = true)
@Composable
private fun WorkbenchIconPreview() {
    LeaveFlowTheme {
        Row(
            Modifier.background(Color.White).padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            ReferenceIcon.entries
                .filter { it !in setOf(ReferenceIcon.MESSAGE, ReferenceIcon.WORKBENCH, ReferenceIcon.APPLICATION, ReferenceIcon.ACCOUNT) }
                .forEach { icon ->
                    ReferenceIconArtwork(icon, LeaveColors.IconBlue, Modifier.size(42.dp))
                }
        }
    }
}

@Preview(name = "动态与静态印章", widthDp = 240, heightDp = 130, showBackground = true)
@Composable
private fun StatusStampPreview() {
    LeaveFlowTheme {
        Surface(color = Color.White) {
            Row(Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatusStamp(LeaveStatus.APPROVED, Modifier.size(96.dp))
                StatusStamp(LeaveStatus.ENDED, Modifier.size(96.dp))
            }
        }
    }
}
