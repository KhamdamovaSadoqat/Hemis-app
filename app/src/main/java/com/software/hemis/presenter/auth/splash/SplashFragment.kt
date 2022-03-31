package com.software.hemis.presenter.auth.splash


import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.hemis.R
import com.example.hemis.databinding.FragmentSplashBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.software.hemis.data.comman.base.BindingFragment
import com.software.hemis.presenter.auth.LoginViewModel
import com.software.hemis.utils.data.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SplashFragment : BindingFragment<FragmentSplashBinding>() {

    @Inject
    lateinit var pref: SharedPref
    private val viewModel: LoginViewModel by viewModels()
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSplashBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.requestAll()
        viewModel.mutableLiveData.observe(this) {
            if (it) findNavController().navigate(R.id.action_splashFragment_to_navigation_deadline)
        }
        allowPermissionForFile()
    }

    private fun requestWriteAndReadPermissions() {
        Dexter.withContext(requireContext())
            .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    Toast.makeText(requireContext(), "multiple check", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?,
                ) {

                }
            }).check()
    }

    private fun requestReadPermission() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    Toast.makeText(requireContext(), "read granted", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(requireContext(), "read denied", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken,
                ) { /* ... */
                }
            }).check()
    }

    private fun allowPermissionForFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestReadPermission()
        } else {
            requestWriteAndReadPermissions()
        }
    }
}