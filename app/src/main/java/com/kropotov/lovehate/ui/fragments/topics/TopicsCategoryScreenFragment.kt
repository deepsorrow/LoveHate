package com.kropotov.lovehate.ui.fragments.topics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.TopicCategory
import com.kropotov.lovehate.databinding.FragmentTopicsCategoryScreenBinding
import com.kropotov.lovehate.databinding.ListItemTopicBinding
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.fragments.topic.TopicPageFragment
import com.kropotov.lovehate.ui.utils.SpaceItemDecoration
import com.kropotov.lovehate.ui.utils.withArgs
import com.kropotov.lovehate.ui.vm.topics.TopicListItemVm
import com.kropotov.lovehate.ui.vm.topics.TopicCategoryVm

class TopicsCategoryScreenFragment : BaseFragment<TopicCategoryVm, FragmentTopicsCategoryScreenBinding>(
    R.layout.fragment_topics_category_screen
) {

    override val vmClass = TopicCategoryVm::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListDelegationAdapter(adapterDelegate())
        adapter.items = listOf(
            TopicListItemVm("Длинные волосы у мужчин", true, "13", "60").apply {
                onClick = {
                    parentFragmentManager.commit {
                        setCustomAnimations(
                            R.anim.slide_up,
                            R.anim.slide_down,
                            0,
                            R.anim.slide_down
                        )
                        add(R.id.overlay_container, TopicPageFragment.newInstance())
                        addToBackStack(null)
                    }
                }
            },
            TopicListItemVm("кошки", false, "22", "60"),
            TopicListItemVm("любовь", false, "3", "52"),
            TopicListItemVm("спать", true, "51", "62"),
            TopicListItemVm("отдых летом на природе", false, "6", "59"),
            TopicListItemVm("Абстрактное искусство", true, "66", "71"),
            TopicListItemVm("советские плакаты", true, "13", "67"),
            TopicListItemVm("вавилон", true, "14", "74"),
        )

        binding.topicsRecyclerView.apply {
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

        private const val TOPIC_CATEGORY = "arg_topic_category"

        fun newInstance(category: TopicCategory) =
            TopicsCategoryScreenFragment().withArgs {
                putSerializable(TOPIC_CATEGORY, category)
            }
    }
}