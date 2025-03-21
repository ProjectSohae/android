package com.sohae.data.myinformation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sohae.data.myinformation.dto.MySearchHistoryDTO
import com.sohae.domain.myinformation.entity.MySearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MySearchHistoryDAO {

    @Query("select * from my_search_history where keyword = :input")
    suspend fun select(input: String): MySearchHistoryEntity?

    @Query("select * from my_search_history")
    fun selectAll(): Flow<List<MySearchHistoryDTO>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(input: MySearchHistoryDTO)

    @Query("delete from my_search_history")
    fun deleteAll()

    @Query("delete from my_search_history where id = :id")
    fun deleteById(id: Int)
}