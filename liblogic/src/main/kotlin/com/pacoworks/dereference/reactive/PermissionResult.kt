package com.pacoworks.dereference.reactive

sealed class PermissionResult(open val requestCode: Int, open val permission: String) {
    data class Success(override val requestCode: Int, override val permission: String) : PermissionResult(requestCode, permission)
    data class Failure(override val requestCode: Int, override val permission: String) : PermissionResult(requestCode, permission)
}