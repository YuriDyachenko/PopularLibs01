package yuri.dyachenko.popularlibs01.ui.login

import kotlinx.coroutines.*
import yuri.dyachenko.popularlibs01.domain.LoginData
import yuri.dyachenko.popularlibs01.domain.LoginRepo
import yuri.dyachenko.popularlibs01.domain.VALIDATION_OK

const val SIMULATION_DELAY_MILLIS = 2_000L

class LoginPresenter(
    private val loginRepo: LoginRepo
) : Contract.Presenter, CoroutineScope by MainScope() {

    private var view: Contract.View? = null
    private var savedData: LoginData? = null
    private var savedState: Contract.State? = null
    private var savedErrorMessageId: Int? = null
    private var savedOkMessageId: Int? = null

    override fun onAttach(view: Contract.View) {
        this.view = view
        restoreAll()
    }

    private fun restoreAll() = view?.apply {
        viewSetState(savedState ?: Contract.State.LOGIN)
        savedData?.let { setData(it) }
        savedOkMessageId?.let { setOkMessage(it) }
        savedErrorMessageId?.let { setErrorMessage(it) }
    }

    override fun onDetach() {
        view = null
    }

    private fun viewSetState(state: Contract.State) = view?.apply {
        savedState = state
        setState(state)
    }

    private fun viewSetError(messageId: Int) = view?.apply {
        savedErrorMessageId = messageId
        setErrorMessage(messageId)
    }

    private fun viewSetOk(messageId: Int) = view?.apply {
        savedOkMessageId = messageId
        setOkMessage(messageId)
    }

    private fun isLoginValid(data: LoginData): Boolean {
        val validation = data.isValidForLogin()
        return if (validation != VALIDATION_OK) {
            viewSetErrorState(validation, Contract.State.ERROR_LOGIN)
            false
        } else true
    }

    private fun isRegistrationValid(data: LoginData): Boolean {
        val validation = data.isValidForRegistration()
        return if (validation != VALIDATION_OK) {
            viewSetErrorState(validation, Contract.State.ERROR_REGISTRATION)
            false
        } else true
    }

    private fun viewSetLoadingState() {
        viewSetOk(MESSAGE_WAIT_CHECKING)
        viewSetState(Contract.State.LOADING)
    }

    private fun viewSetErrorState(messageId: Int, errorState: Contract.State) {
        viewSetError(messageId)
        viewSetState(errorState)
    }

    private fun getStateByResult(result: Int, errorState: Contract.State): Contract.State =
        if (result == VALIDATION_OK) {
            Contract.State.SUCCESS
        } else {
            errorState
        }

    private fun repoDoDelayed(
        data: LoginData,
        action: (LoginData) -> (Int),
        errorState: Contract.State,
        okMessageId: Int
    ) {
        viewSetLoadingState()
        launch {
            var result: Int
            viewSetState(withContext(Dispatchers.IO) {
                delay(SIMULATION_DELAY_MILLIS)
                result = action(data)
                getStateByResult(result, errorState)
            })
            viewSetResultMessage(result, okMessageId)
        }
    }

    private fun viewSetResultMessage(result: Int, okMessageId: Int) {
        if (result == VALIDATION_OK) {
            viewSetOk(okMessageId)
        } else {
            viewSetError(result)
        }
    }

    override fun onEnter(data: LoginData) {
        if (isLoginValid(data)) {
            repoDoDelayed(
                data,
                { loginRepo.login(data) },
                Contract.State.ERROR_LOGIN,
                MESSAGE_OK_LOGGED_IN
            )
        }
    }

    override fun onRegister(data: LoginData) {
        if (isRegistrationValid(data)) {
            repoDoDelayed(
                data,
                { loginRepo.register(data) },
                Contract.State.ERROR_REGISTRATION,
                MESSAGE_OK_REGISTERED
            )
        }
    }

    override fun onExit() {
        viewSetState(Contract.State.LOGIN)
    }

    override fun onRegistration() {
        viewSetState(Contract.State.REGISTRATION)
    }

    override fun onErrorLogin() {
        viewSetState(Contract.State.LOGIN)
    }

    override fun onErrorRegistration() {
        viewSetState(Contract.State.REGISTRATION)
    }

    override fun onReturn() {
        viewSetState(Contract.State.LOGIN)
    }

    override fun onTextChanged(data: LoginData) {
        savedData = data
    }

    override fun onForgotPassword() {
        viewSetOk(MESSAGE_OK_PASSWORD_SENT)
        viewSetState(Contract.State.PASSWORD_SENT)
    }
}