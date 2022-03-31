package com.software.hemis.domain.main.resources

import androidx.lifecycle.LiveData
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.data.main.resources.ResourceResponse
import com.software.hemis.domain.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface ResourceRepository {

    suspend fun resource(subjectId: Int, semesterId: Int): Flow<BaseResult<List<ResourceEntity>, WrappedResponse<ResourceResponse>>>

    suspend fun insertResource(resource: ResourceEntity)

    suspend fun getResource(subjectId: Int, semesterId: Int, typeId: Int): LiveData<List<ResourceEntity>>
}