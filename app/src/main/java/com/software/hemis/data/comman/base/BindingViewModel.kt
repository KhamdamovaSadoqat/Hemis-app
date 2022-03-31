package com.software.hemis.data.comman.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.domain.base.BaseResult
import com.software.hemis.domain.auth.login.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class BindingViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val refreshState = MutableStateFlow<RefreshState>(RefreshState.Init)
    val mRefreshState: StateFlow<RefreshState> get() = refreshState

    fun refresh(){
        viewModelScope.async {
            loginUseCase.refresh()
                .onStart {
                    refreshState.value = RefreshState.IsLoading(true)
                }.catch { exception ->
                    Log.d("-------------", "refresh: catch: ${exception.toString()}")
                    refreshState.value = RefreshState.IsLoading(false)
                    refreshState.value = RefreshState.ShowToast(exception.cause?.message.toString())
                }
                .collect { result ->
                    refreshState.value = RefreshState.IsLoading(false)
                    when (result) {
                        is BaseResult.Success -> {
                            refreshState.value =
                                RefreshState.SuccessLogin("Success")
                        }
                        is BaseResult.Error -> {
                            Log.d("-------------", "refresh: error")
                            refreshState.value =
                                RefreshState.ErrorLogin(result.rawResponse)
                        }
                    }
                }
        }.onAwait
    }
}

sealed class RefreshState {
    object Init : RefreshState()
    data class IsLoading(val isLoading: Boolean) : RefreshState()
    data class ShowToast(val message: String) : RefreshState()
    data class SuccessLogin(val loginEntity: String) : RefreshState()
    data class ErrorLogin(val rawResponse: WrappedResponse<String>) : RefreshState()
}