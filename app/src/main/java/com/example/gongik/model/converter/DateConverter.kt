package com.example.gongik.model.converter

import androidx.room.TypeConverter
import java.sql.Timestamp

class DateConverter {
    @TypeConverter
    fun longToTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(value) }
    }

    @TypeConverter
    fun timestampToLong(timestamp: Timestamp?): Long? {
        return timestamp?.time
    }
}