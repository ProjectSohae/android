package com.jhw.sohae.data.datasource.myinformation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jhw.sohae.data.model.myinformation.MyWelfareDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface MyWelfareDAO {

    @Query("select * from my_welfare")
    suspend fun selectAll(): MyWelfareDTO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(inputMyWelfareDTO: MyWelfareDTO)

    @Query("delete from my_welfare")
    fun deleteAll()
}