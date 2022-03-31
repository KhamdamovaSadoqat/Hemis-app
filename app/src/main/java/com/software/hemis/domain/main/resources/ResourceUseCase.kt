package com.software.hemis.domain.main.resources

import javax.inject.Inject

class ResourceUseCase @Inject constructor(private val resourceRepository: ResourceRepository) {

    suspend fun resource(subjectId: Int, semesterId: Int) =
        resourceRepository.resource(subjectId, semesterId)

    suspend fun insertResource(resource: ResourceEntity) = resourceRepository.insertResource(resource)

    suspend fun getResource(subjectId: Int, semesterId: Int, typeId: Int) = resourceRepository.getResource(subjectId, semesterId, typeId)
}