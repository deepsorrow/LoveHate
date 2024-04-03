package com.kropotov.lovehate.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kropotov.lovehate.ui.dialogs.ProgressBarDialog
import kotlinx.coroutines.launch

interface LoadingInformer {

    var progressBarDialog: ProgressBarDialog?

    fun <T: BaseViewModel> Fragment.initProgressBar(viewModel: T) {
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
}