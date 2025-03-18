package com.sohae.domain.jobinformation.usecase

import com.sohae.common.models.workplace.entity.ApplyHistoryEntity
import com.sohae.domain.jobinformation.repository.JobInfoRepository
import javax.inject.Inject

class JobInfoUseCase @Inject constructor(
    private val jobInfoRepository: JobInfoRepository
) {

    fun getDistrictList(
        ghjbc_cd: String,
        codegubun: String,
        callback: (Map<String, String>, String) -> Unit
    ) {
        jobInfoRepository.getDistrictList(
            ghjbc_cd,
            codegubun
        ) { response, isSucceed ->

            if (isSucceed) {
                callback(response, "관할 병무청을 불러 오는데 실패했습니다.\n잠시 후 다시 시도해 주세요.")
            } else {
                callback(response, "")
            }
        }
    }

    fun getJobApplyHistoryList(
        jeopsu_yy: String,
        jeopsu_tms: String,
        ghjbc_cd: String,
        bjdsggjuso_cd: String,
        callback: (List<ApplyHistoryEntity>, String, Boolean) -> Unit
    ) {
        val success = { response: List<ApplyHistoryEntity> ->
            callback(response, "", true)
        }
        val failure = { msg:String ->
            callback(emptyList(), msg, false)
        }

        jobInfoRepository.getJobApplyHistoryList(
            jeopsu_yy,
            jeopsu_tms,
            ghjbc_cd,
            bjdsggjuso_cd
        ) { response, isSucceed ->

            if (isSucceed) {

                if (response.isEmpty()) {
                    failure("정보가 존재하지 않습니다.")
                } else {
                    success(response)
                }
            } else {

                failure("불러오는데 실패했습니다.\n잠시 후 다시 시도해 주세요.")
            }
        }
    }
}