package com.example.carapp.uistate

sealed class NetworkResult<T>(val data: T? = null, val error: String? = null) {
    class Success<T>(data: T): NetworkResult<T>(data = data)
    class Failure<T>(error: String): NetworkResult<T>(error = error)
    class Loading<T>(): NetworkResult<T>()
}