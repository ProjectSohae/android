package com.example.sohae.model.data.myinformation.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sohae.model.data.myinformation.MyWelfare

@Dao
interface MyWelfareDAO {

    @Query("select * from my_welfare")
    suspend fun selectAll(): MyWelfare?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(inputMyWelfare: MyWelfare)

    @Query("delete from my_welfare")
    suspend fun deleteAll()
}