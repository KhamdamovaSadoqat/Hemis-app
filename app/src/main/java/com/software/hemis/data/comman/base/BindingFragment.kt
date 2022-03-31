package com.software.hemis.data.comman.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.software.hemis.utils.data.Authorization
import com.software.hemis.utils.data.StatusCode
import com.software.hemis.utils.showToast
import kotlinx.coroutines.flow.collectLatest

abstract class BindingFragment<out T : ViewBinding> : Fragment() {

    private lateinit var viewModel: BindingViewModel
    private var _binding: ViewBinding? = null
    protected val binding: T
        get() = _binding as T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = bindingInflater(inflater)
        viewModel = ViewModelProvider(requireActivity())[BindingViewModel::class.java]
        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    protected abstract val bindingInflater: (LayoutInflater) -> ViewBinding

    fun refresh() {
        viewModel.refresh()
    }

    fun observeRefresh() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.mRefreshState.collectLatest {
                refreshStateChange(it)
            }
        }
    }

    private fun refreshStateChange(state: RefreshState) {
        when (state) {
            is RefreshState.Init -> Unit
            is RefreshState.ErrorLogin -> {
                if (state.rawResponse.code == StatusCode.RESPONSE_CODE_UNAUTHORIZED)
                    Authorization.setUnAuthorized(false)
            }
            is RefreshState.SuccessLogin -> {
                Authorization.setUnAuthorized(true)
            }
            is RefreshState.ShowToast -> {
                binding.root.context.showToast(state.message)
            }
            is RefreshState.IsLoading -> {}
        }
    }
}