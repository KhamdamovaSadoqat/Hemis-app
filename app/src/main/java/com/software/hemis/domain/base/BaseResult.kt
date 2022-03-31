package com.software.hemis.domain.base

sealed class BaseResult <out T : Any, out U : Any> {
    data class Success <T: Any>(val data : T) : BaseResult<T, Nothing>()
    data class Error <U : Any>(val rawResponse: U) : BaseResult<Nothing, U>()
}

sealed class BaseResultList <out T: Any, out U: Any, out K: Any>{
    data class Success <T: Any, U: Any>(val data: T, val data2: U): BaseResultList<T, U, Nothing>()
    data class Error <K : Any>(val rawResponse: K) : BaseResultList<Nothing, Nothing, K>()
}