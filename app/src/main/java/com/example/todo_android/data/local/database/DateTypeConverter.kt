package com.example.todo_android.data.local.database

import androidx.room.TypeConverter
import java.util.Date

/**
 * Room에서 Date 타입을 변환하기 위한 타입 컨버터
 */
class DateTypeConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
