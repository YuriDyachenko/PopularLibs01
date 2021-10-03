package yuri.dyachenko.popularlibs01.domain

interface LoginRepo {
    fun register(data: LoginData): Int
    fun login(data: LoginData): Int
}