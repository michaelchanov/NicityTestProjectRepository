package com.nicity.domain.common

sealed class ResultState<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T) : ResultState<T>(data = data)

    class Error<T>(message: String, data: T? = null) :
        ResultState<T>(data = data, message = message)

    class Loading<T>(data: T? = null) : ResultState<T>(data = data)
}
