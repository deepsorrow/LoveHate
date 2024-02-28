package com.kropotov.lovehate.ui.fragments.topic

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.FragmentTopicPageBinding
import com.kropotov.lovehate.databinding.ListItemTopicBinding
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utils.SpaceItemDecoration
import com.kropotov.lovehate.ui.vm.topic.TopicPageVm
import com.kropotov.lovehate.ui.vm.topics.TopicListItemVm
import javax.inject.Inject

class TopicPageFragment @Inject constructor() : BaseFragment<TopicPageVm, FragmentTopicPageBinding>(
    R.layout.fragment_topic_page
) {

    override val vmClass = TopicPageVm::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListDelegationAdapter(adapterDelegate())
        adapter.items = listOf(
            TopicListItemVm("Цой", false, "22", "60"),
            TopicListItemVm("любовь", false, "3", "52"),
            TopicListItemVm("спать", true, "51", "62"),
        )

        binding.similarTopicsList.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(SpaceItemDecoration(context, R.dimen.one_dp))
        }
    }

    private fun adapterDelegate() =
        adapterDelegateViewBinding<TopicListItemVm, TopicListItemVm, ListItemTopicBinding>(
            { layoutInflater, parent ->
                ListItemTopicBinding.inflate(layoutInflater, parent, false)
            }
        ) {
            bind {
                binding.viewModel = item
            }
        }

    companion object {
        fun newInstance() = TopicPageFragment()
    }
}