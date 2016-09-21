package com.pacoworks.dereference.core.reactive

sealed class ActivityResult(val requestCode: Int) {
    data class SuccessWithData(private val reqCode: Int, val data : Map<String, Any>) : ActivityResult(reqCode)
    data class FailureWithData(private val reqCode: Int, val data : Map<String, Any>) : ActivityResult(reqCode)
    data class Success(private val reqCode: Int) : ActivityResult(reqCode)
    data class Failure(private val reqCode: Int) : ActivityResult(reqCode)
}