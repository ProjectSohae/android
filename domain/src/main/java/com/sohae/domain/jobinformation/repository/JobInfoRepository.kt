package com.sohae.domain.jobinformation.repository

import com.sohae.common.models.workplace.entity.ApplyHistoryEntity

interface JobInfoRepository {

    fun getJobApplyHistoryList(
        jeopsu_yy: String,
        jeopsu_tms: String,
        ghjbc_cd: String,
        bjdsggjuso_cd: String,
        callback: (List<ApplyHistoryEntity>, Boolean) -> Unit
    )

    fun getDistrictList(
        ghjbc_cd: String,
        codegubun: String,
        callback: (Map<String, String>, Boolean) -> Unit
    )
}