package com.mad.weatherwear.shared.preference

enum class ColdSensitivity(val value: Int) {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    companion object {
        fun fromValue(value: Int): ColdSensitivity {
            return when (value) {
                1 -> LOW
                3 -> HIGH
                else -> MEDIUM // Default to medium
            }
        }
    }
}

data class Preference(
    val UserId: String,
    val coldSensitivity: Int
) {
    val sensitivity: ColdSensitivity
        get() = ColdSensitivity.fromValue(coldSensitivity)
}