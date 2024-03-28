package com.kropotov.lovehate.ui.screens.ratings.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.items.RatingListItem
import com.kropotov.lovehate.databinding.FragmentRatingsBinding
import com.kropotov.lovehate.ui.adapters.lists.RatingsListAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.screens.ratings.RatingsRouter
import com.kropotov.lovehate.ui.screens.ratings.RatingsViewModel
import com.kropotov.lovehate.ui.utilities.SpaceItemDecoration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RatingsFragment : BaseFragment<RatingsViewModel, FragmentRatingsBinding>(
    R.layout.fragment_ratings
) {

    override val vmClass = RatingsViewModel::class.java

    @Inject
    lateinit var router: RatingsRouter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usersAdapter = RatingsListAdapter(viewModel.getUsersEmptyData())
        binding.usersRatingList.initList(usersAdapter)
        viewModel.usersDataStream.startCollecting(usersAdapter)

        val opinionsAdapter = RatingsListAdapter(viewModel.getOpinionsEmptyData())
        binding.opinionsRatingList.initList(opinionsAdapter)
        viewModel.opinionsDataStream.startCollecting(opinionsAdapter)

        lifecycleScope.launch {
            viewModel.navigateToOpinionsList.collect { listType ->
                router.navigateToOpinions(listType)
            }
        }
        lifecycleScope.launch {
            viewModel.navigateToUsersList.collect { listType ->
                router.navigateToUsers(listType)
            }
        }
    }

    private fun RecyclerView.initList(adapter: RatingsListAdapter) = apply {
        this.adapter = adapter
        addItemDecoration(SpaceItemDecoration(requireContext()))
        setHasFixedSize(true)
    }

    private fun <T : RatingListItem> Flow<T>.startCollecting(adapter: RatingsListAdapter) {
        lifecycleScope.launch {
            collect { newItem ->
                adapter.updateItem(newItem)
            }
        }
    }

    companion object {
        fun newInstance() = RatingsFragment()
    }
}
