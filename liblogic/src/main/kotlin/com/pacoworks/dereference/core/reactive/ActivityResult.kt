package com.pacoworks.dereference.core.reactive

sealed class ActivityResult(open val requestCode: Int) {
    data class SuccessWithData(override val requestCode: Int, val data : Map<String, Any>) : ActivityResult(requestCode)
    data class FailureWithData(override val requestCode: Int, val data : Map<String, Any>) : ActivityResult(requestCode)
    data class Success(override val requestCode: Int) : ActivityResult(requestCode)
    data class Failure(override val requestCode: Int) : ActivityResult(requestCode)
}