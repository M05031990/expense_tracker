package com.carded.api

import androidx.lifecycle.LiveData

sealed class RequestResponse <out T: Any>{
    data class Success<T: Any>( val response: T?): RequestResponse<T>()
    data class Failure(val error: String): RequestResponse<Nothing>()
    data class Progress(val progress: Boolean): RequestResponse<Nothing>()
}

fun <T : Any> LiveData<RequestResponse<T>>.isDataLoaded(): Boolean {
    when (this.value) {
        is RequestResponse.Success -> return true
    }
    return false
}

/**
 * true if [RequestResponse] is of type [Success] and [items] holds non-null
 */
val RequestResponse<*>.succeeded: Boolean
    get() = this is RequestResponse.Success && response != null