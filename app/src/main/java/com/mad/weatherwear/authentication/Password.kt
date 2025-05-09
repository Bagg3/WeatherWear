package com.mad.weatherwear.authentication

data class Password(val value: String) {

    init {
        if (!validate(value)) {
            throw IllegalArgumentException("Illegal password")
        }
    }

    companion object {
        private val regexPattern = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")

        fun validate(password: String): Boolean {
            return regexPattern.matches(password)
        }
    }
}