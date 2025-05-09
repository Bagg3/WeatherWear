package com.mad.weatherwear.authentication

data class Email(val value: String) {
    init {
        if (!validate(value)) {
            throw IllegalArgumentException("Illegal password")
        }
    }

    companion object {
        private val regexPattern = Regex(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        )

        fun validate(email: String): Boolean {
            return regexPattern.matches(email)
        }
    }
}