package com.software.hemis.data.auth.university

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.domain.auth.university.UniversityEntity
import com.software.hemis.domain.auth.university.UniversityRepository
import com.software.hemis.domain.base.BaseResult
import com.software.hemis.domain.base.BaseResultList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UniversityRepositoryImp @Inject constructor(
    private val universityApi: UniversityApi
) : UniversityRepository {
    override suspend fun university(): Flow<BaseResult<List<UniversityEntity>, WrappedResponse<UniversityResponse>>> {
        val response = universityApi.university()
        return flow {
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                // checking body data for not being null
                if (body.data != null) {
                    val listUniversities = arrayListOf<UniversityEntity>()
                    body.data!!.forEach { uni ->
                        val university = UniversityEntity(
                            uni.code!!.toInt() ?: 0,
                            uni.name ?: "",
                            uni.apiUrl ?: ""
                        )
                        listUniversities.add(university)
                    }
                    emit(BaseResult.Success(listUniversities))
                }
            } else {
                val type = object : TypeToken<WrappedResponse<UniversityResponse>>() {}.type
                val err: WrappedResponse<UniversityResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }

    }
}