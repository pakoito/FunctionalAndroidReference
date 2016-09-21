package com.pacoworks.dereference.reactive

sealed class PermissionResult(val requestCode: Int, val permission: String) {
    data class Success(private val code: Int, private val perm: String) : PermissionResult(code, perm)
    data class Failure(private val code: Int, private val perm: String) : PermissionResult(code, perm)
}