package com.example.gongik.model.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gongik.model.data.myinformation.MyRank

@Dao
interface MyRankDAO {

    @Query("select * from my_rank where uid = 0")
    suspend fun selectAll(): MyRank?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(inputMyRank: MyRank)

    @Query("delete from my_rank")
    suspend fun deleteAll()
}