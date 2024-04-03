package com.kropotov.lovehate.ui.base

import android.content.Context
import android.os.Bundle
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kropotov.lovehate.BR
import com.kropotov.lovehate.R
import com.kropotov.lovehate.di.app.ViewModelFactory
import com.kropotov.lovehate.ui.dialogs.ProgressBarDialog
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseBottomSheetDialogFragment<VIEW_MODEL : BaseViewModel, BINDING : ViewDataBinding>(
    private val layoutId: Int
) : BottomSheetDialogFragment(), MessageInformer, LoadingInformer, HasAndroidInjector {

    override var progressBarDialog: ProgressBarDialog? = null

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private val viewModelDelegate = lazy {
        ViewModelProvider(this, viewModelFactory)[vmClass]
    }

    protected val viewModel: VIEW_MODEL by viewModelDelegate

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory<VIEW_MODEL>
    protected abstract val vmClass: Class<VIEW_MODEL>

    protected lateinit var binding: BINDING

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun getTheme(): Int = R.style.BottomSheetDialogThemeNoFloating

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        if (!binding.setVariable(BR.viewModel, viewModel)) {
            throw InflateException("No data binding variable `viewModel` in layout!")
        }
        initProgressBar(viewModel)
        initMessageInformer(viewModel)

        return binding.root
    }
}