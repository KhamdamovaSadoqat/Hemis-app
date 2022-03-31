package com.software.hemis.presenter.other.data.profile.data

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.hemis.R
import com.example.hemis.databinding.FragmentEditProfileBinding
import com.google.android.material.textfield.TextInputLayout
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.data.main.profile.PasswordChangeRequest
import com.software.hemis.presenter.other.data.profile.ChangePasswordState
import com.software.hemis.presenter.other.data.profile.ProfileViewModel
import com.software.hemis.utils.ImageDownloader
import com.software.hemis.utils.data.StatusCode
import com.software.hemis.utils.showToast
import com.software.hemis.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class EditProfileFragment : BindingFragment<FragmentEditProfileBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentEditProfileBinding::inflate
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        setUpView()
        setUp()
        profile()
    }

    private fun setUp(){
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpView() {
        binding.btnSave.setOnClickListener {
            val password = check(binding.etlNewPassword, binding.etNewPassword.text.toString())
            val passwordVerify =
                check(binding.etlVerifyPassword, binding.etVerifyPassword.text.toString())
            if (password && passwordVerify) {
                if (isEqual(
                        binding.etNewPassword.text.toString(),
                        binding.etVerifyPassword.text.toString(),
                        binding.etlVerifyPassword
                    )
                ) {
                    changePassword(
                        PasswordChangeRequest(
                            binding.etOldPassword.text.toString(),
                            binding.etNewPassword.text.toString(),
                            binding.etVerifyPassword.text.toString()
                        )
                    )
                }
            }
        }
    }

    private fun profile() {
        viewModel.getProfile().observe(this){ profileEntity ->
            binding.tvFullName.text = requireContext().getString(
                R.string.full_name,
                profileEntity.firstName,
                profileEntity.secondName
            )
            binding.tvCourse.text = requireContext().getString(
                R.string.group_course,
                profileEntity.groupName,
                profileEntity.levelName)
            binding.tvLogin.text = requireContext().getString(
                R.string.student_number,
                profileEntity.studentIdNumber
            )
            ImageDownloader.loadImage(requireContext(),
                profileEntity.image,
                binding.ivProfile)
        }
    }

    private fun changePassword(passwordChangeRequest: PasswordChangeRequest) {
        viewModel.changePassword(passwordChangeRequest)
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.mPasswordChangeState.collectLatest {
                handleStateChange(it)
            }
        }
    }

    private fun handleStateChange(passwordState: ChangePasswordState) {
        when (passwordState) {
            is ChangePasswordState.Init -> Unit
            is ChangePasswordState.ErrorPassword -> {
                if (passwordState.rawResponse.code == StatusCode.RESPONSE_CODE_UNAUTHORIZED) {
                    refresh()
                    observeRefresh()
                }
            }
            is ChangePasswordState.SuccessPassword -> {
                Toast.makeText(requireContext(), passwordState.message, Toast.LENGTH_SHORT).show()
            }
            is ChangePasswordState.ShowToast -> {
                binding.root.context.showToast(passwordState.message)
            }
            is ChangePasswordState.IsLoading -> {
                //remove old texts
                if (passwordState.isLoading) {
                } else {
                    binding.etNewPassword.text?.clear()
                    binding.etVerifyPassword.text?.clear()
                    binding.etOldPassword.text?.clear()
                }
            }
        }
    }

    private fun check(view: TextInputLayout, text: String): Boolean {
        return if (text.isEmpty() || text.isBlank()) {
            view.error = "password is required"
            false
        } else true
    }

    private fun isEqual(
        password: String,
        passwordVerify: String,
        passwordVerifyView: TextInputLayout
    ): Boolean {
        return if (password != passwordVerify) {
            passwordVerifyView.error = "password is not identical"
            false
        } else true
    }
}