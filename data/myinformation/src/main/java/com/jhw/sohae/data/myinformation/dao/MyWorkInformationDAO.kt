package com.jhw.sohae.data.myinformation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jhw.sohae.data.myinformation.dto.MyWorkInformationDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface MyWorkInformationDAO {

    @Query("select * from my_work_information")
    fun selectAll(): Flow<MyWorkInformationDTO>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(inputMyWorkInformationDTO: MyWorkInformationDTO)

    @Query("delete from my_work_information")
    fun deleteAll()
}