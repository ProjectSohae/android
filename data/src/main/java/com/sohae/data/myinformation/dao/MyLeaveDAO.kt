package com.sohae.data.myinformation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sohae.data.myinformation.dto.MyLeaveDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface MyLeaveDAO {

    @Query("select * from my_leave where id = 0")
    fun selectAll(): Flow<MyLeaveDTO?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(inputMyLeaveDTO: MyLeaveDTO)

    @Query("delete from my_leave")
    fun deleteAll()
}