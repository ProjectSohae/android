package com.sohae.data.myinformation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sohae.data.myinformation.dto.MyUsedLeaveDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface MyUsedLeaveDAO {

    @Query("select * from my_used_leave where id = :id")
    fun selectById(id: Int): Flow<MyUsedLeaveDTO?>

    @Query("select * from my_used_leave where leave_kind_idx = :leaveKindIdx")
    suspend fun selectByLeaveKindIdx(leaveKindIdx: Int): List<MyUsedLeaveDTO>?

    // 1) 당월 근무 기간 중 휴가가 시작되고 끝나는 경우
    // 2) 당월 근무 기간 중 휴가가 시작되나 익월 근무 기간에서 끝나는 경우
    // 3) 이전 근무 기간에서 시작된 휴가가 당월 근무 기간에서 끝나는 경우
    // 4) 휴가 기간이 근무 기간 전체를 포함하는 경우
    @Query("select * from my_used_leave where " +
            "(:startDate <= leave_start_date and leave_start_date <= :endDate) " +
            "or (:startDate <= leave_end_date and leave_end_date <= :endDate) " +
            "or (leave_start_date <= :startDate and :endDate <= leave_end_date)")
    suspend fun selectByDate(startDate: Long, endDate: Long): List<MyUsedLeaveDTO>?
    
    @Query("select * from my_used_leave")
    fun selectAll(): Flow<List<MyUsedLeaveDTO>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(inputMyUsedLeaveDTO: MyUsedLeaveDTO)

    @Query("delete from my_used_leave where id = :targetUid")
    fun deleteById(targetUid: Int)

    @Query("delete from my_used_leave")
    fun deleteAll()
}