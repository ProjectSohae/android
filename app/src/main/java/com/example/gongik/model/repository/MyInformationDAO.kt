package com.example.gongik.model.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gongik.model.data.myinformation.MyInformation

@Dao
interface MyInformationDAO {

    @Query("select * from my_information where uid = 0")
    suspend fun selectAll(): MyInformation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(inputMyInformation: MyInformation)

    @Query("delete from my_information")
    suspend fun deleteAll()
}