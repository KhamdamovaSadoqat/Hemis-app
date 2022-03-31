package com.software.hemis.utils.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object Authorization {

    private val unAuthorized = MutableLiveData<Boolean>()

    fun setUnAuthorized(isUnAuthorized: Boolean) {
        unAuthorized.value = isUnAuthorized
    }

    fun getUnAuthorized(): LiveData<Boolean> {
        return unAuthorized
    }
}