package com.jhw.sohae.data.datasource.myinformation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jhw.sohae.data.model.myinformation.MyLeaveDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface MyLeaveDAO {

    @Query("select * from my_leave where id = 0")
    suspend fun selectAll(): MyLeaveDTO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(inputMyLeaveDTO: MyLeaveDTO)

    @Query("delete from my_leave")
    fun deleteAll()
}