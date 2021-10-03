package yuri.dyachenko.popularlibs01.ui.login

import yuri.dyachenko.popularlibs01.domain.LoginData

const val MESSAGE_OK_LOGGED_IN = 100
const val MESSAGE_WAIT_CHECKING = 101
const val MESSAGE_OK_REGISTERED = 102
const val MESSAGE_OK_PASSWORD_SENT = 103
const val MESSAGE_ERROR_UNKNOWN = 104
const val MESSAGE_ERROR_RANDOM = 105

class Contract {
    enum class State {
        LOGIN, REGISTRATION, ERROR_LOGIN, ERROR_REGISTRATION, LOADING, SUCCESS, PASSWORD_SENT
    }

    interface View {
        fun setState(state: State)
        fun setData(data: LoginData)
        fun setOkMessage(messageId: Int)
        fun setErrorMessage(messageId: Int)
    }

    interface Presenter {
        fun onAttach(view: View)
        fun onDetach()
        fun onEnter(data: LoginData)
        fun onExit()
        fun onRegister(data: LoginData)
        fun onRegistration()
        fun onErrorLogin()
        fun onErrorRegistration()
        fun onReturn()
        fun onTextChanged(data: LoginData)
        fun onForgotPassword()
    }
}