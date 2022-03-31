package com.software.hemis.domain.auth.university

import javax.inject.Inject

class UniversityUseCase @Inject constructor( private val universityRepository: UniversityRepository) {

    suspend fun university() = universityRepository.university()

}