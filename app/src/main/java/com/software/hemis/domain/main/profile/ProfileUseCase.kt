package com.software.hemis.domain.main.profile

import com.software.hemis.data.main.profile.PasswordChangeRequest
import javax.inject.Inject

class ProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend fun execute() =
        profileRepository.profile()

    suspend fun insertProfile(profileEntity: ProfileEntity) =
        profileRepository.insertProfile(profileEntity)

    suspend fun getProfile() = profileRepository.getProfile()

    suspend fun changePassword(passwordChangeRequest: PasswordChangeRequest) =
        profileRepository.passwordChange(passwordChangeRequest)
}