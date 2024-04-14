package com.kropotov.lovehate.ui.screens.myrating

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.FragmentMyRatingBinding
import com.kropotov.lovehate.ui.adapters.lists.paging.MyRatingPagingListAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.SpaceItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MyRatingFragment : BaseFragment<MyRatingViewModel, FragmentMyRatingBinding>(
    R.layout.fragment_my_rating
) {

    override val vmClass = MyRatingViewModel::class.java

    private val adapter by lazy { MyRatingPagingListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.registerPagingAdapter()
        binding.list.apply {
            adapter = this@MyRatingFragment.adapter
            addItemDecoration(SpaceItemDecoration(context))
        }

        subscribeToListItems()
    }

    private fun subscribeToListItems() {
        lifecycleScope.launch {
            viewModel.searchMyRatingItems().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    companion object {
        fun newInstance() = MyRatingFragment()
    }
}