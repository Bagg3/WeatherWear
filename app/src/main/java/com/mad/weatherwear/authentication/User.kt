package com.mad.weatherwear.authentication

data class User(val id: String, val email: Email) {

}
enum class Status {
    ERROR,
    OK
}

data class AuthResult(
    val status: Status,
    val user: User?
) {
    fun isOk() = status == Status.OK
}