package com.software.hemis.data.main.resources

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.database.room.ResourceDao
import com.software.hemis.domain.base.BaseResult
import com.software.hemis.domain.main.resources.ResourceEntity
import com.software.hemis.domain.main.resources.ResourceRepository
import com.software.hemis.utils.data.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResourceRepositoryImp @Inject constructor(
    private val resourceApi: ResourceApi,
    private val resourceDao: ResourceDao,
) : ResourceRepository {
    override suspend fun resource(
        subjectId: Int,
        semesterId: Int,
    ): Flow<BaseResult<List<ResourceEntity>, WrappedResponse<ResourceResponse>>> {
        return flow {
            val response = resourceApi.resource("${Constants.universityUrl}education/resources?subject=$subjectId&semester=$semesterId"
            )
            // checking data for success and not being null
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                // checking body data for not being null
                if (body.data != null) {
                    // as it is not null using body.data!! is accessible
                    Log.d("ResourceRepositoryImp", "------------- resource: ${body.data}")
                        val listResource = arrayListOf<ResourceEntity>()
                    body.data!!.forEach { data ->
                        val resourceFiles: HashMap<String?, String?> =
                            object : HashMap<String?, String?>() {}
                        if (data.files != null) {
                            //getting submitted files if exist
                            for (file in data.files.indices) {
                                resourceFiles[data.files[file]?.url] =
                                    data.files[file]?.name
                            }
                        }
                        val resource = ResourceEntity(
                            data.id ?: 0,
                            data.name ?: "",
                            data.comment ?: "",
                            data.trainingType?.code ?: "0",
                            data.trainingType?.name ?: "",
                            data.employee?.id ?: 0,
                            data.employee?.name ?: "",
                            resourceFiles,
                            data.updatedAt ?: 0,
                            subjectId,
                            semesterId
                        )
                        listResource.add(resource)
                    }

                    emit(BaseResult.Success(listResource))
                }
            } else {
                val type = object : TypeToken<WrappedResponse<ResourceResponse>>() {}.type
                val err: WrappedResponse<ResourceResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun insertResource(resource: ResourceEntity) {
        resourceDao.insertResource(resource)
    }

    override suspend fun getResource(subjectId: Int, semesterId: Int, typeId: Int): LiveData<List<ResourceEntity>> {
        return resourceDao.getResource(subjectId, semesterId, typeId)
    }
}