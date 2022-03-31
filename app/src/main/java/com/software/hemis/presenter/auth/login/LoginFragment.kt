package com.software.hemis.presenter.auth.login

import android.os.Build
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.hemis.R
import com.example.hemis.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.data.auth.login.LoginRequest
import com.software.hemis.presenter.auth.LoginState
import com.software.hemis.presenter.auth.LoginViewModel
import com.software.hemis.utils.data.Constants
import com.software.hemis.utils.gone
import com.software.hemis.utils.showToast
import com.software.hemis.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginFragment : BindingFragment<FragmentLoginBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLoginBinding::inflate
    private val viewModel: LoginViewModel by viewModels()

    private var passwordVisibility = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            requireActivity().window.setDecorFitsSystemWindows(false)
//        } else {
//            activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
//        }
        setUpToolBar()
        setUp()
        login()
        observe()
    }

    private fun setUp() {
        binding.etlPassword.setEndIconOnClickListener {
            if (passwordVisibility) {
                binding.etPassword.transformationMethod = PasswordTransformationMethod()
                passwordVisibility = false
            } else {
                binding.etPassword.transformationMethod = null
                passwordVisibility = true
            }
        }
    }

    private fun setUpToolBar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun check(view: TextInputLayout, text: String): Boolean {
        return if (text.isEmpty() || text.isBlank()) {
            view.error = "login is required"
            false
        } else true
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.mLoginState.collectLatest {
                handleStateChange(it)
            }
        }
    }

    private fun login() {
        binding.btnEnter.setOnClickListener {
            val password = check(binding.etlPassword, binding.etPassword.text.toString())
            val login = check(binding.etlLogin, binding.etLogin.text.toString())
            if (password && login) {
                viewModel.login("",
                    LoginRequest(
                        binding.etLogin.text.toString(),
                        binding.etPassword.text.toString()
                    )
                )
            }
        }
    }

    private fun handleStateChange(state: LoginState) {
        when (state) {
            is LoginState.Init -> Unit
            is LoginState.ErrorLogin -> {
                binding.spinKit.gone()

                binding.etlLogin.requestFocus()
                binding.etlPassword.requestFocus()

                binding.etlPassword.errorIconDrawable = null
                binding.etlLogin.errorIconDrawable = null

                binding.etlLogin.error = state.rawResponse.error
                binding.etlPassword.error = state.rawResponse.error

                binding.tvError.visible()
                binding.tvError.text = state.rawResponse.error
            }
            is LoginState.SuccessLogin -> {
                binding.spinKit.gone()
                binding.tvError.gone()
                findNavController().navigate(R.id.action_loginFragment_to_splashFragment)
            }
            is LoginState.ShowToast -> {
                binding.spinKit.gone()
                binding.root.context.showToast(state.message)
            }
            is LoginState.IsLoading -> {
                if (state.isLoading) {
                    binding.spinKit.visible()
                } else {
                    binding.spinKit.gone()
                }
            }
        }
    }

}