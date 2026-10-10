package dev.h4ze.leaveflow

import android.app.Application
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray

private class LeaveRepository(application: Application) {
    private val preferences = application.getSharedPreferences("leave_records", 0)

    fun load(): List<LeaveRecord> {
        val raw = preferences.getString("records", null) ?: return listOf(LeaveRecord())
        return runCatching {
            val array = JSONArray(raw)
            buildList {
                for (index in 0 until array.length()) add(LeaveRecord.fromJson(array.getJSONObject(index)))
            }
        }.getOrElse { listOf(LeaveRecord()) }
    }

    fun save(records: List<LeaveRecord>) {
        val array = JSONArray().apply { records.forEach { put(it.toJson()) } }
        preferences.edit { putString("records", array.toString()) }
    }
}

class LeaveViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LeaveRepository(application)
    private val _records = MutableStateFlow(repository.load())
    val records: StateFlow<List<LeaveRecord>> = _records.asStateFlow()

    fun find(id: String): LeaveRecord? = _records.value.firstOrNull { it.id == id }

    fun save(record: LeaveRecord) {
        val current = _records.value.toMutableList()
        val index = current.indexOfFirst { it.id == record.id }
        if (index >= 0) current[index] = record else current.add(0, record)
        _records.value = current
        repository.save(current)
    }

    fun delete(id: String) {
        _records.value = _records.value.filterNot { it.id == id }
        repository.save(_records.value)
    }
}
