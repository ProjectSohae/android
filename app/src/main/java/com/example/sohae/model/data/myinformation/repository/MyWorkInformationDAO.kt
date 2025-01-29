package com.example.sohae.model.data.myinformation.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sohae.model.data.myinformation.MyWorkInformation

@Dao
interface MyWorkInformationDAO {

    @Query("select * from my_work_information")
    suspend fun selectAll(): MyWorkInformation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(inputMyWorkInformation: MyWorkInformation)

    @Query("delete from my_work_information")
    suspend fun deleteAll()
}