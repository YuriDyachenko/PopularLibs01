package yuri.dyachenko.popularlibs01.ui

import android.app.Application
import yuri.dyachenko.popularlibs01.impl.LoginListRepoImpl
import yuri.dyachenko.popularlibs01.ui.login.LoginPresenter

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        private var loginPresenter: LoginPresenter? = null

        fun getLoginPresenter(): LoginPresenter {
            if (loginPresenter == null) {
                synchronized(LoginPresenter::class.java) {
                    if (loginPresenter == null) {
                        if (appInstance == null) {
                            throw IllegalStateException("Application is null while creating LoginPresenter")
                        }
                        loginPresenter = LoginPresenter(LoginListRepoImpl())
                    }
                }
            }
            return loginPresenter!!
        }
    }
}