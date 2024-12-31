package com.example.gongik.model.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gongik.model.data.myinformation.MyLeave

@Dao
interface MyLeaveDAO {

    @Query("select * from my_leave where uid = 0")
    suspend fun selectAll(): MyLeave?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(inputMyLeave: MyLeave)

    @Query("delete from my_leave")
    suspend fun deleteAll()
}