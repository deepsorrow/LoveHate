package com.kropotov.lovehate.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kropotov.lovehate.di.vm.ViewModelFactory
import com.kropotov.lovehate.BR
import dagger.android.support.DaggerFragment
import java.lang.RuntimeException
import javax.inject.Inject

abstract class BaseFragment<VIEW_MODEL : ViewModel, BINDING : ViewDataBinding>(
    private val layoutId: Int
) : DaggerFragment() {

    private val viewModelDelegate = lazy {
        ViewModelProvider(this, viewModelFactory)[vmClass]
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
            throw RuntimeException("No data binding variable `viewModel` in layout!")
        }
        return binding.root
    }

}