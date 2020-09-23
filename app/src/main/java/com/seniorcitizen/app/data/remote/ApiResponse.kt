package com.seniorcitizen.app.data.remote

/**
 * Created by Alvin Raygon on 2019-12-31.
 */
sealed class ApiResponse<T> {

    data class Success<T>(val body: T) : ApiResponse<T>()
    data class Failure<T>(val message: String, val code: Int) : ApiResponse<T>()

}
