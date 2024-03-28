package com.kropotov.lovehate.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.DialogPickMediaBinding
import com.kropotov.lovehate.ui.utilities.autoCleared
import com.kropotov.lovehate.ui.screens.PickMediaViewModel
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class PickMediaDialog : BottomSheetDialogFragment(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModel: PickMediaViewModel
    private var binding by autoCleared<DialogPickMediaBinding>()

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPickMediaBinding.inflate(inflater, container, false).apply {

        }

        return binding.root
    }
}
