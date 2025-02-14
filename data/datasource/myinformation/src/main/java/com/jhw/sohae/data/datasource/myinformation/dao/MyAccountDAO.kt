package com.jhw.sohae.data.datasource.myinformation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jhw.sohae.data.model.myinformation.MyAccountDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface MyAccountDAO {

    @Query("select * from my_account where id = 0")
    fun selectAll(): MyAccountDTO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(inputMyAccountDTO: MyAccountDTO)

    @Query("delete from my_account")
    fun deleteAll()
}