package com.kropotov.lovehate.ui.screens.users

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.UsersListType
import com.kropotov.lovehate.databinding.FragmentUsersBinding
import com.kropotov.lovehate.ui.adapters.lists.UsersListAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.SpaceItemDecoration
import com.kropotov.lovehate.ui.utilities.withArgs
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.ref.WeakReference
import javax.inject.Inject

class UsersFragment : BaseFragment<UsersViewModel, FragmentUsersBinding>(R.layout.fragment_users) {

    override val vmClass = UsersViewModel::class.java

    @Inject
    lateinit var adapter: UsersListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.list.apply {
            this.adapter = this@UsersFragment.adapter
            addItemDecoration(SpaceItemDecoration(requireContext()))
        }
        binding.refreshLayout.setOnRefreshListener { adapter.refresh() }
        viewModel.toolbar.arrowBackAction.set {
            val weakThis = WeakReference(parentFragmentManager)
            weakThis.get()?.popBackStack()
        }

        initShimmerLayout(adapter)
        subscribeToListData(adapter)
    }

    private fun subscribeToListData(adapter: UsersListAdapter) {
        viewModel.items
            .onEach {
                binding.refreshLayout.isRefreshing = false
                adapter.submitData(it)
            }
            .launchIn(lifecycleScope)

        adapter.addLoadStateListener { loadState ->
            loadState.refresh.extractAndShowError()
            loadState.append.extractAndShowError()
            loadState.prepend.extractAndShowError()
        }
    }

    private fun initShimmerLayout(adapter: UsersListAdapter) {
        binding.shimmerLayout.showShimmer(true)
        adapter.addOnPagesUpdatedListener { hideShimmerLayout() }
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.list.layoutManager?.scrollToPosition(0)
            }
        })
    }

    private fun hideShimmerLayout() {
        binding.shimmerLayout.hideShimmer()
        binding.placeholderList.root.visibility = View.GONE
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
