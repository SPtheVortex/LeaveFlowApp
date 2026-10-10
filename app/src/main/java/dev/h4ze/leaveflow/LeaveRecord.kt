package dev.h4ze.leaveflow

import org.json.JSONObject
import java.util.UUID

enum class LeaveStatus(val displayName: String) {
    APPROVED("审批通过"),
    ENDED("结束请假"),
}

data class LeaveRecord(
    val id: String = UUID.randomUUID().toString(),
    val detailTitle: String = "张同学的请假",
    val applicantSectionTitle: String = "申请人",
    val contentSectionTitle: String = "申请内容",
    val progressSectionTitle: String = "审批进度",
    val applicantName: String = "张同学",
    val studentId: String = "2026000001",
    val gender: String = "男",
    val college: String = "研究生院",
    val department: String = "计算机科学与技术学院",
    val major: String = "软件工程",
    val majorDirection: String = "",
    val className: String = "软件工程 2026",
    val grade: String = "2026",
    val educationLevel: String = "硕士研究生",
    val schoolingLength: String = "3.0",
    val submittedDate: String = "2026-09-14",
    val approvalNo: String = "202609140000000001",
    val awayMode: String = "离校",
    val leaveType: String = "事假",
    val reason: String = "个人事务需要处理，申请按规定请假。",
    val destination: String = "示例市示例区",
    val address: String = "示例街道 1 号",
    val phone: String = "13800000000",
    val startTime: String = "2026-09-15 12:00",
    val endTime: String = "2026-09-16 20:00",
    val duration: String = "1.33",
    val teacherName: String = "王老师",
    val teacherPhone: String = "010-00000000",
    val parentName: String = "家属",
    val parentPhone: String = "13900000000",
    val emergencyName: String = "紧急联系人",
    val emergencyPhone: String = "13700000000",
    val applicationFileName: String = "请假申请示例.jpg",
    val applicationFileUri: String = "",
    val medicalFileName: String = "证明材料示例.pdf",
    val medicalFileUri: String = "",
    val signature: String = "张同学",
    val signatureStrokes: String = "0.12,0.78;0.18,0.28;0.21,0.14;0.23,0.35;0.20,0.62;0.18,0.83|0.08,0.53;0.34,0.48|0.29,0.72;0.39,0.32;0.43,0.18;0.42,0.52;0.47,0.75|0.34,0.52;0.55,0.45;0.63,0.30|0.56,0.72;0.70,0.33;0.75,0.60;0.88,0.22",
    val proxyName: String = "管理员",
    val proxyTime: String = "2026-09-20 10:43",
    val reviewerOne: String = "审核人甲(10000001)",
    val reviewerOneTime: String = "2026-09-14 15:55",
    val reviewerTwo: String = "审核人乙(10000002)",
    val reviewerTwoTime: String = "14:19",
    val status: LeaveStatus = LeaveStatus.APPROVED,
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("detailTitle", detailTitle)
        put("applicantSectionTitle", applicantSectionTitle)
        put("contentSectionTitle", contentSectionTitle)
        put("progressSectionTitle", progressSectionTitle)
        put("applicantName", applicantName)
        put("studentId", studentId)
        put("gender", gender)
        put("college", college)
        put("department", department)
        put("major", major)
        put("majorDirection", majorDirection)
        put("className", className)
        put("grade", grade)
        put("educationLevel", educationLevel)
        put("schoolingLength", schoolingLength)
        put("submittedDate", submittedDate)
        put("approvalNo", approvalNo)
        put("awayMode", awayMode)
        put("leaveType", leaveType)
        put("reason", reason)
        put("destination", destination)
        put("address", address)
        put("phone", phone)
        put("startTime", startTime)
        put("endTime", endTime)
        put("duration", duration)
        put("teacherName", teacherName)
        put("teacherPhone", teacherPhone)
        put("parentName", parentName)
        put("parentPhone", parentPhone)
        put("emergencyName", emergencyName)
        put("emergencyPhone", emergencyPhone)
        put("applicationFileName", applicationFileName)
        put("applicationFileUri", applicationFileUri)
        put("medicalFileName", medicalFileName)
        put("medicalFileUri", medicalFileUri)
        put("signature", signature)
        put("signatureStrokes", signatureStrokes)
        put("proxyName", proxyName)
        put("proxyTime", proxyTime)
        put("reviewerOne", reviewerOne)
        put("reviewerOneTime", reviewerOneTime)
        put("reviewerTwo", reviewerTwo)
        put("reviewerTwoTime", reviewerTwoTime)
        put("status", status.name)
    }

    companion object {
        fun blank(): LeaveRecord = LeaveRecord(
            detailTitle = "我的请假",
            applicantName = "",
            studentId = "",
            gender = "",
            college = "",
            department = "",
            major = "",
            majorDirection = "",
            className = "",
            grade = "",
            educationLevel = "",
            schoolingLength = "",
            submittedDate = "",
            approvalNo = "",
            awayMode = "离校",
            leaveType = "事假",
            reason = "",
            destination = "",
            address = "",
            phone = "",
            startTime = "",
            endTime = "",
            duration = "",
            teacherName = "",
            teacherPhone = "",
            parentName = "",
            parentPhone = "",
            emergencyName = "",
            emergencyPhone = "",
            applicationFileName = "",
            applicationFileUri = "",
            medicalFileName = "",
            medicalFileUri = "",
            signature = "",
            signatureStrokes = "",
            proxyName = "",
            proxyTime = "",
            reviewerOne = "",
            reviewerOneTime = "",
            reviewerTwo = "",
            reviewerTwoTime = "",
        )

        fun fromJson(json: JSONObject): LeaveRecord = LeaveRecord(
            id = json.optString("id", UUID.randomUUID().toString()),
            detailTitle = json.optString("detailTitle", "我的请假"),
            applicantSectionTitle = json.optString("applicantSectionTitle", "申请人"),
            contentSectionTitle = json.optString("contentSectionTitle", "申请内容"),
            progressSectionTitle = json.optString("progressSectionTitle", "审批进度"),
            applicantName = json.optString("applicantName"),
            studentId = json.optString("studentId"),
            gender = json.optString("gender", "男"),
            college = json.optString("college", "研究生院"),
            department = json.optString("department", "计算机科学与技术学院"),
            major = json.optString("major", "软件工程"),
            majorDirection = json.optString("majorDirection"),
            className = json.optString("className", "软件工程 2026"),
            grade = json.optString("grade", "2026"),
            educationLevel = json.optString("educationLevel", "硕士研究生"),
            schoolingLength = json.optString("schoolingLength", "3.0"),
            submittedDate = json.optString("submittedDate"),
            approvalNo = json.optString("approvalNo"),
            awayMode = json.optString("awayMode"),
            leaveType = json.optString("leaveType"),
            reason = json.optString("reason"),
            destination = json.optString("destination"),
            address = json.optString("address"),
            phone = json.optString("phone"),
            startTime = json.optString("startTime"),
            endTime = json.optString("endTime"),
            duration = json.optString("duration"),
            teacherName = json.optString("teacherName"),
            teacherPhone = json.optString("teacherPhone"),
            parentName = json.optString("parentName"),
            parentPhone = json.optString("parentPhone"),
            emergencyName = json.optString("emergencyName"),
            emergencyPhone = json.optString("emergencyPhone"),
            applicationFileName = json.optString("applicationFileName", "本人申请书.jpg"),
            applicationFileUri = json.optString("applicationFileUri"),
            medicalFileName = json.optString("medicalFileName", json.optString("attachmentName")),
            medicalFileUri = json.optString("medicalFileUri"),
            signature = json.optString("signature"),
            signatureStrokes = json.optString("signatureStrokes"),
            proxyName = json.optString("proxyName"),
            proxyTime = json.optString("proxyTime"),
            reviewerOne = json.optString("reviewerOne"),
            reviewerOneTime = json.optString("reviewerOneTime"),
            reviewerTwo = json.optString("reviewerTwo"),
            reviewerTwoTime = json.optString("reviewerTwoTime"),
            status = runCatching { LeaveStatus.valueOf(json.optString("status")) }
                .getOrDefault(LeaveStatus.APPROVED),
        )
    }
}
