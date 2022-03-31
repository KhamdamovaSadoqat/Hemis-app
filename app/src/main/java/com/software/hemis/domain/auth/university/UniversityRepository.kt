package com.software.hemis.domain.auth.university

import com.software.hemis.data.auth.university.UniversityResponse
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.domain.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface UniversityRepository {

    suspend fun university(): Flow<BaseResult<List<UniversityEntity>, WrappedResponse<UniversityResponse>>>
}