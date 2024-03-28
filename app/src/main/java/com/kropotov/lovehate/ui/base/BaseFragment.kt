package com.kropotov.lovehate.ui.base

import android.os.Bundle
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.kropotov.lovehate.di.app.ViewModelFactory
import com.kropotov.lovehate.BR
import com.kropotov.lovehate.data.InformMessage
import com.kropotov.lovehate.data.InformType
import com.kropotov.lovehate.ui.dialogs.ProgressBarDialog
import com.kropotov.lovehate.ui.utilities.extractErrorMessage
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<VIEW_MODEL : BaseViewModel, BINDING : ViewDataBinding>(
    private val layoutId: Int
) : DaggerFragment() {

    open val isActivityViewModelOwner = false
    private var progressBarDialog: ProgressBarDialog? = null
    private val viewModelDelegate = lazy {
        ViewModelProvider(if (isActivityViewModelOwner) requireActivity() else this, viewModelFactory)[vmClass]
    }

    protected val viewModel: VIEW_MODEL by viewModelDelegate

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory<VIEW_MODEL>
    protected abstract val vmClass: Class<VIEW_MODEL>

    protected lateinit var binding: BINDING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        if (!binding.setVariable(BR.viewModel, viewModel)) {
            throw InflateException("No data binding variable `viewModel` in layout!")
        }
        initProgressBar()
        initMessageInformer()
        initBackPressedDispatcher()

        return binding.root
    }

    protected open fun showError(message: InformMessage) {
        Snackbar.make(requireView(), message.text, ERROR_SNACKBAR_DISMISS_TIMEOUT).apply {
            setTextMaxLines(ERROR_SNACKBAR_MAX_LINES)
            show()
        }
    }

    protected fun LoadState.extractAndShowError() {
        (this as? LoadState.Error)?.error?.let {
            val errorText = it.extractErrorMessage()
            val informMessage = InformMessage(InformType.ERROR, errorText)
            showError(informMessage)
        }
    }

    protected open fun initBackPressedDispatcher() {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
            } else if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            } else if ((parentFragment?.parentFragmentManager?.backStackEntryCount ?: 0) > 0) {
                parentFragment?.parentFragmentManager?.popBackStack()
            }
        }
    }

    private fun initProgressBar() {
        progressBarDialog = ProgressBarDialog(requireContext())
        lifecycleScope.launch {
            viewModel.isLoading.collect { isVisible ->
                if (isVisible) {
                    progressBarDialog?.show()
                } else {
                    progressBarDialog?.dismiss()
                }
            }
        }
    }

    private fun initMessageInformer() {
        lifecycleScope.launch {
            viewModel.informMessageStream.collect {
                showError(it)
            }
        }
    }

    companion object {
        const val ERROR_SNACKBAR_MAX_LINES = 6
        const val ERROR_SNACKBAR_DISMISS_TIMEOUT = 6000
    }
}
