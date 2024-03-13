package com.kropotov.lovehate.ui.fragments


import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.OpinionSortType
import com.kropotov.lovehate.databinding.FragmentOpinionsBinding
import com.kropotov.lovehate.ui.adapters.OpinionListItemAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.SpaceItemDecoration
import com.kropotov.lovehate.ui.utilities.withArgs
import com.kropotov.lovehate.ui.viewmodels.feed.OpinionsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OpinionsFragment : BaseFragment<OpinionsViewModel, FragmentOpinionsBinding>(
    R.layout.fragment_opinions
) {

    override val vmClass = OpinionsViewModel::class.java

    private val adapter = OpinionListItemAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.list.apply {
            adapter = this@OpinionsFragment.adapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(SpaceItemDecoration(context))
        }

        lifecycleScope.launch {
            viewModel.searchOpinions("").collectLatest {
                adapter.submitData(it)
            }
        }
    }


    companion object {

        const val OPINION_TYPE_TAB_ARG = "arg_opinion_type_tab"

        fun newInstance(type: OpinionSortType) =
            OpinionsFragment().withArgs {
                putSerializable(OPINION_TYPE_TAB_ARG, type)
            }
    }
}