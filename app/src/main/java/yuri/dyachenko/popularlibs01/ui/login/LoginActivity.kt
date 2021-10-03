package yuri.dyachenko.popularlibs01.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import yuri.dyachenko.popularlibs01.R
import yuri.dyachenko.popularlibs01.databinding.ActivityLoginBinding
import yuri.dyachenko.popularlibs01.domain.*
import yuri.dyachenko.popularlibs01.ui.App
import yuri.dyachenko.popularlibs01.util.showOnly
import yuri.dyachenko.popularlibs01.util.toEditable

class LoginActivity : AppCompatActivity(), Contract.View {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val presenter = App.getLoginPresenter()

    private val messageMap = mapOf(
        MESSAGE_ERROR_UNKNOWN to R.string.error_text_unknown,
        MESSAGE_ERROR_RANDOM to R.string.error_text_random,
        VALIDATION_EMPTY_EMAIL to R.string.error_text_empty_email,
        VALIDATION_EMPTY_PASSWORD to R.string.error_text_empty_password,
        VALIDATION_EMPTY_REPEAT_PASSWORD to R.string.error_text_empty_repeat_password,
        VALIDATION_DIFFERENT_PASSWORDS to R.string.error_text_different_passwords,
        MESSAGE_OK_LOGGED_IN to R.string.ok_logged_in,
        MESSAGE_OK_REGISTERED to R.string.ok_registered,
        MESSAGE_OK_PASSWORD_SENT to R.string.ok_password_sent,
        MESSAGE_WAIT_CHECKING to R.string.wait_checking
    )

    private val clickMap by lazy {
        with(binding) {
            mapOf(
                enterButton to { presenter.onEnter(gatherData()) },
                exitButton to { presenter.onExit() },
                registrationButton to { presenter.onRegistration() },
                registerButton to { presenter.onRegister(gatherData()) },
                errorLoginButton to { presenter.onErrorLogin() },
                errorRegistrationButton to { presenter.onErrorRegistration() },
                forgotPasswordButton to { presenter.onForgotPassword() },
                returnButton to { presenter.onReturn() }
            )
        }
    }

    private val loginViews: Array<View> by lazy {
        with(binding) {
            arrayOf(
                mainLayout,
                welcomeTextView,
                enterButton,
                registrationButton,
                forgotPasswordButton,
                emailInput,
                passwordInput
            )
        }
    }

    private val registrationViews: Array<View> by lazy {
        with(binding) {
            arrayOf(
                mainLayout,
                welcomeTextView,
                registerButton,
                emailInput,
                passwordInput,
                repeatPasswordInput
            )
        }
    }

    private val errorLoginViews: Array<View> by lazy {
        with(binding) {
            arrayOf(
                errorLayout,
                errorTitleTextView,
                errorDescriptionTextView,
                errorLoginButton
            )
        }
    }

    private val errorRegistrationViews: Array<View> by lazy {
        with(binding) {
            arrayOf(
                errorLayout,
                errorTitleTextView,
                errorDescriptionTextView,
                errorRegistrationButton
            )
        }
    }

    private val loadingViews: Array<View> by lazy {
        with(binding) {
            arrayOf(
                mainLayout,
                welcomeTextView,
                okDescriptionTextView,
                progressView
            )
        }
    }

    private val successViews: Array<View> by lazy {
        with(binding) {
            arrayOf(
                mainLayout,
                welcomeTextView,
                okDescriptionTextView,
                exitButton
            )
        }
    }

    private val passwordSentViews: Array<View> by lazy {
        with(binding) {
            arrayOf(
                mainLayout,
                welcomeTextView,
                okDescriptionTextView,
                returnButton
            )
        }
    }

    private val stateViewsMap by lazy {
        mapOf(
            Contract.State.LOGIN to loginViews,
            Contract.State.REGISTRATION to registrationViews,
            Contract.State.ERROR_LOGIN to errorLoginViews,
            Contract.State.ERROR_REGISTRATION to errorRegistrationViews,
            Contract.State.LOADING to loadingViews,
            Contract.State.SUCCESS to successViews,
            Contract.State.PASSWORD_SENT to passwordSentViews
        )
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            presenter.onTextChanged(gatherData())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        presenter.onAttach(this)

        initButtons()
        initEdits()
    }

    private fun initButtons() {
        for ((button, function) in clickMap) {
            button.setOnClickListener { run(function) }
        }
    }

    private fun initEdits() = with(binding) {
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
        repeatPasswordEditText.addTextChangedListener(textWatcher)
    }

    override fun onDestroy() {
        presenter.onDetach()
        _binding = null
        super.onDestroy()
    }

    override fun setState(state: Contract.State) = with(binding) {
        container.showOnly(stateViewsMap.getOrDefault(state, loginViews))
    }

    fun gatherData(): LoginData = with(binding) {
        return LoginData(
            emailEditText.text.toString(),
            passwordEditText.text.toString(),
            repeatPasswordEditText.text.toString()
        )
    }

    override fun setData(data: LoginData) = with(binding) {
        emailEditText.text = data.email.toEditable()
        passwordEditText.text = data.password.toEditable()
        repeatPasswordEditText.text = data.repeatPassword.toEditable()
    }

    override fun setOkMessage(messageId: Int) {
        binding.okDescriptionTextView.text =
            getString(messageMap.getOrDefault(messageId, R.string.ok_text_unknown))
    }

    override fun setErrorMessage(messageId: Int) {
        binding.errorDescriptionTextView.text =
            getString(messageMap.getOrDefault(messageId, R.string.error_text_unknown))
    }
}