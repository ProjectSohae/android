package com.example.gongik.model.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gongik.model.data.myinformation.MyUsedLeave

@Dao
interface MyUsedLeaveDAO {

    @Query("select * from my_used_leave where uid = :targetUid")
    suspend fun selectById(targetUid: Int): MyUsedLeave?

    @Query("select * from my_used_leave where leave_kind_idx = :leaveKindIdx")
    suspend fun selectByLeaveKindIdx(leaveKindIdx: Int): List<MyUsedLeave>?

    @Query("select * from my_used_leave")
    suspend fun selectAll(): List<MyUsedLeave>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(inputMyUsedLeave: MyUsedLeave)

    @Query("delete from my_used_leave where uid = :targetUid")
    suspend fun deleteById(targetUid: Int)

    @Query("delete from my_used_leave")
    suspend fun deleteAll()
}