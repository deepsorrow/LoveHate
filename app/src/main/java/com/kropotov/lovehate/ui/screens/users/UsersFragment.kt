package com.kropotov.lovehate.ui.screens.users

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.InformMessage
import com.kropotov.lovehate.data.UsersListType
import com.kropotov.lovehate.databinding.FragmentUsersBinding
import com.kropotov.lovehate.ui.adapters.lists.UsersListAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.SpaceItemDecoration
import com.kropotov.lovehate.ui.utilities.withArgs
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class UsersFragment : BaseFragment<UsersViewModel, FragmentUsersBinding>(R.layout.fragment_users) {

    override val vmClass = UsersViewModel::class.java

    @Inject
    lateinit var adapter: UsersListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.registerPagingAdapter()
        binding.list.apply {
            this.adapter = this@UsersFragment.adapter
            addItemDecoration(SpaceItemDecoration(requireContext()))
        }
        binding.refreshLayout.setOnRefreshListener { adapter.refresh() }

        subscribeToListData(adapter)
    }

    private fun subscribeToListData(adapter: UsersListAdapter) {
        viewModel.items
            .onEach {
                binding.refreshLayout.isRefreshing = false
                adapter.submitData(it)
            }
            .launchIn(lifecycleScope)
    }

    override fun onSnackbarMessageShow(message: InformMessage) {
        hideShimmer()
    }

    companion object {
        const val USERS_LIST_TYPE_ARG = "arg_users_list_type"

        fun newInstance(
            listType: UsersListType
        ) = UsersFragment().withArgs {
            putSerializable(USERS_LIST_TYPE_ARG, listType)
        }
    }
}
