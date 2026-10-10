package dev.h4ze.leaveflow

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LeaveRecordTest {
    @Test
    fun blankRecordRetainsEditablePageStructure() {
        val record = LeaveRecord.blank()
        assertEquals("申请人", record.applicantSectionTitle)
        assertEquals(LeaveStatus.APPROVED, record.status)
        assertTrue(record.id.isNotBlank())
    }
}
