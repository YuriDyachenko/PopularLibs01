package yuri.dyachenko.popularlibs01.domain

const val VALIDATION_OK = 0
const val VALIDATION_EMPTY_EMAIL = 1
const val VALIDATION_EMPTY_PASSWORD = 2
const val VALIDATION_EMPTY_REPEAT_PASSWORD = 3
const val VALIDATION_DIFFERENT_PASSWORDS = 4

data class LoginData(
    val email: String,
    val password: String,
    val repeatPassword: String
) {

    fun isValidForLogin() = when {
        email.isEmpty() -> VALIDATION_EMPTY_EMAIL
        password.isEmpty() -> VALIDATION_EMPTY_PASSWORD
        else -> VALIDATION_OK
    }

    fun isValidForRegistration() = when {
        email.isEmpty() -> VALIDATION_EMPTY_EMAIL
        password.isEmpty() -> VALIDATION_EMPTY_PASSWORD
        repeatPassword.isEmpty() -> VALIDATION_EMPTY_REPEAT_PASSWORD
        password != repeatPassword -> VALIDATION_DIFFERENT_PASSWORDS
        else -> VALIDATION_OK
    }
}
