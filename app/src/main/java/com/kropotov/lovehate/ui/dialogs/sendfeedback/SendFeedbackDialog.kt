package com.kropotov.lovehate.ui.dialogs.sendfeedback

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.kropotov.lovehate.databinding.DialogSendFeedbackBinding
import com.kropotov.lovehate.ui.base.MessageInformer
import com.kropotov.lovehate.ui.utilities.autoCleared
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.launch
import javax.inject.Inject

class SendFeedbackDialog : DialogFragment(), MessageInformer, HasAndroidInjector {

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

        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        initMessageInformer(viewModel)
        return binding.root
    }
}
