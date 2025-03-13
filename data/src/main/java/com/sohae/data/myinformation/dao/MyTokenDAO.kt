package com.sohae.data.myinformation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sohae.data.myinformation.dto.MyTokenDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface MyTokenDAO {

    @Query("select access_token from my_token")
    fun getMyAccessToken(): Flow<String>

    @Query("select refresh_token from my_token")
    fun getMyRefreshToken(): Flow<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setMyToken(input: MyTokenDTO)

    @Query("delete from my_token")
    fun deleteAll()
}