package yuri.dyachenko.popularlibs01.impl

import yuri.dyachenko.popularlibs01.domain.LoginData
import yuri.dyachenko.popularlibs01.domain.LoginRepo
import yuri.dyachenko.popularlibs01.domain.VALIDATION_OK

class LoginListRepoImpl : LoginRepo {
    private val list = mutableListOf<LoginData>()

    override fun register(data: LoginData): Int {
        return VALIDATION_OK
    }

    override fun login(data: LoginData): Int {
        return VALIDATION_OK
    }
}