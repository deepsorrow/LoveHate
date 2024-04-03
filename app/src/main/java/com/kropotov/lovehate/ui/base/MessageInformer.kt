package com.kropotov.lovehate.ui.base

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.InformMessage
import com.kropotov.lovehate.data.InformType
import com.kropotov.lovehate.ui.utilities.getColorAttr
import kotlinx.coroutines.launch

interface MessageInformer {

    fun Fragment.showSnackbarMessage(message: InformMessage) {
        Snackbar.make(requireView(), message.text, ERROR_SNACKBAR_DISMISS_TIMEOUT).apply {
            setTextMaxLines(ERROR_SNACKBAR_MAX_LINES)
            if (this@showSnackbarMessage !is DialogFragment) {
                anchorView = requireActivity().findViewById(R.id.bottom_bar)
            }

            val color = if (message.type == InformType.SUCCESS) {
                context.getColorAttr(R.attr.love_container_color)
            } else {
                context.getColorAttr(R.attr.hate_container_color)
            }
            setBackgroundTint(color)
            show()
            onSnackbarMessageShow()
        }
    }

    fun <T: BaseViewModel> Fragment.initMessageInformer(viewModel: T) {
        lifecycleScope.launch {
            viewModel.informMessageStream.collect {
                showSnackbarMessage(it)
            }
        }
    }

    fun onSnackbarMessageShow() {

    }

    companion object {
        const val ERROR_SNACKBAR_MAX_LINES = 6
        const val ERROR_SNACKBAR_DISMISS_TIMEOUT = 5000
    }
}