package com.jhw.sohae.data.myinformation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jhw.sohae.data.myinformation.dto.MyRankDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface MyRankDAO {

    @Query("select * from my_rank where id = 0")
    fun selectAll(): Flow<MyRankDTO>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(inputMyRankDTO: MyRankDTO)

    @Query("delete from my_rank")
    fun deleteAll()
}