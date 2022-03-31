package com.software.hemis.domain.auth.login

import com.software.hemis.data.auth.login.LoginRequest
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {

    suspend fun execute(url:String,loginRequest: LoginRequest) =
        loginRepository.login(url,loginRequest)

    suspend fun refresh() = loginRepository.refresh()
}