package com.kropotov.lovehate.ui.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.kropotov.lovehate.databinding.DialogSendFeedbackBinding
import com.kropotov.lovehate.ui.base.BaseFragment.Companion.ERROR_SNACKBAR_DISMISS_TIMEOUT
import com.kropotov.lovehate.ui.base.BaseFragment.Companion.ERROR_SNACKBAR_MAX_LINES
import com.kropotov.lovehate.ui.utilities.autoCleared
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.launch
import javax.inject.Inject

class SendFeedbackDialog : DialogFragment(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var viewModel: SendFeedbackViewModel
    private var binding by autoCleared<DialogSendFeedbackBinding>()

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSendFeedbackBinding.inflate(requireActivity().layoutInflater).apply {
            viewModel = this@SendFeedbackDialog.viewModel
        }

        lifecycleScope.launch {
            viewModel.feedbackSentStream.collect {
                dismiss()
            }
        }
        lifecycleScope.launch {
            viewModel.informMessageStream.collect {
                Snackbar.make(requireView(), it.text, ERROR_SNACKBAR_DISMISS_TIMEOUT).apply {
                    setTextMaxLines(ERROR_SNACKBAR_MAX_LINES)
                    show()
                }
            }
        }

        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return binding.root
    }
}
