package com.jhw.sohae.data.datasource.myinformation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jhw.sohae.data.model.myinformation.MyRankDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface MyRankDAO {

    @Query("select * from my_rank where id = 0")
    suspend fun selectAll(): MyRankDTO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(inputMyRankDTO: MyRankDTO)

    @Query("delete from my_rank")
    fun deleteAll()
}