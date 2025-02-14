package com.jhw.sohae.data.datasource.myinformation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jhw.sohae.data.model.myinformation.MyWorkInformationDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface MyWorkInformationDAO {

    @Query("select * from my_work_information")
    suspend fun selectAll(): MyWorkInformationDTO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(inputMyWorkInformationDTO: MyWorkInformationDTO)

    @Query("delete from my_work_information")
    fun deleteAll()
}