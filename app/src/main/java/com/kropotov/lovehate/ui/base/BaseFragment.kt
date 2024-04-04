package com.kropotov.lovehate.ui.base

import android.os.Bundle
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.kropotov.lovehate.di.app.ViewModelFactory
import com.kropotov.lovehate.BR
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.InformMessage
import com.kropotov.lovehate.data.InformType
import com.kropotov.lovehate.ui.dialogs.ProgressBarDialog
import com.kropotov.lovehate.ui.utilities.extractErrorMessage
import dagger.android.support.DaggerFragment
import java.lang.ref.WeakReference
import javax.inject.Inject

abstract class BaseFragment<VIEW_MODEL : BaseViewModel, BINDING : ViewDataBinding>(
    private val layoutId: Int
) : DaggerFragment(), MessageInformer, LoadingInformer {


    override var progressBarDialog: ProgressBarDialog? = null

    private val viewModelDelegate = lazy {
        ViewModelProvider(this, viewModelFactory)[vmClass]
    }

    protected val viewModel: VIEW_MODEL by viewModelDelegate

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory<VIEW_MODEL>
    protected abstract val vmClass: Class<VIEW_MODEL>

    protected lateinit var binding: BINDING

    private val shimmer: ShimmerFrameLayout? by lazy { requireView().findViewById(R.id.shimmer) }
    private val shimmerList: ViewGroup? by lazy { requireView().findViewById(R.id.shimmer_list) }

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
        initBackPressedDispatcher()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireView().setToolbarArrowBackAction()
    }

    protected fun LoadState.extractAndShowError() {
        (this as? LoadState.Error)?.error?.let {
            val errorText = it.extractErrorMessage()
            val informMessage = InformMessage(InformType.ERROR, errorText)
            showSnackbarMessage(informMessage)
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

    protected fun View.setToolbarArrowBackAction() {
        findViewById<TextView>(R.id.arrow_back)?.let {
            it.setOnClickListener {
                val weakThis = WeakReference(parentFragmentManager)
                weakThis.get()?.popBackStack()
            }
        }
    }

    protected fun PagingDataAdapter<*, *>.registerPagingAdapter() {
        addLoadStateListener { loadState ->
            loadState.refresh.extractAndShowError()
            loadState.append.extractAndShowError()
            loadState.prepend.extractAndShowError()
        }

        val noDataStub: TextView? = requireView().findViewById(R.id.no_data_stub)
        addOnPagesUpdatedListener {
            hideShimmer()

            noDataStub?.visibility = if (itemCount == 0) View.VISIBLE else View.GONE
        }

        val list: RecyclerView? = requireView().findViewById(R.id.list)
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    list?.layoutManager?.scrollToPosition(0)
                }
            }
        })

        shimmer?.showShimmer(true)
    }

    protected fun hideShimmer() {
        shimmer?.hideShimmer()
        shimmerList?.visibility = View.GONE
    }

    companion object {
        const val DEBOUNCE_TIME_MS = 500L
    }
}
