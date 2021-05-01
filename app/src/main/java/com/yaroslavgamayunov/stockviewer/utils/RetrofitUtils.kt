package com.yaroslavgamayunov.stockviewer.utils

sealed class CallResult<out T : Any> {
    data class Success<out T : Any>(val value: T?) : CallResult<T>()
    data class Error(val cause: Exception? = null, val msg: String? = null) : CallResult<Nothing>()
}

suspend inline fun <T : Any> safeApiCall(
    notNull: Boolean = true,
    call: suspend () -> T?
): CallResult<T> {
    return try {
        val callResult = call.invoke()
        if (notNull) {
            requireNotNull(callResult)
        }
        CallResult.Success(callResult)
    } catch (e: Exception) {
        CallResult.Error(e)
    }
}