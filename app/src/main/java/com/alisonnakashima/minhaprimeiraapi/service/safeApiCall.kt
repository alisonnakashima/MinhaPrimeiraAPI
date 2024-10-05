package com.alisonnakashima.minhaprimeiraapi.service

import com.alisonnakashima.minhaprimeiraapi.service.Result.Success
import retrofit2.HttpException

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val code: Int, val message: String) : Result<Nothing>()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return try {
        Success(apiCall())
    } catch (e: Exception) {
        when (e) {
            is HttpException -> {
                Result.Error(e.code(), e.message())
            }
            else -> {
                Result.Error(-1, "Unknown error")
            }
        }
    }
}