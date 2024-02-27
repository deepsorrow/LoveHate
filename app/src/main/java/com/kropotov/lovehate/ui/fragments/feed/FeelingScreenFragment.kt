package com.kropotov.lovehate.ui.fragments.feed


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.data.OpinionType.HATE
import com.kropotov.lovehate.data.OpinionType.LOVE
import com.kropotov.lovehate.data.OpinionType.NEUTRAL
import com.kropotov.lovehate.data.OpinionType.UNION
import com.kropotov.lovehate.databinding.FragmentFeelingScreenBinding
import com.kropotov.lovehate.databinding.ListItemPostBinding
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utils.SpaceItemDecoration
import com.kropotov.lovehate.ui.utils.serializable
import com.kropotov.lovehate.ui.utils.withArgs
import com.kropotov.lovehate.ui.vm.feed.FeelingScreenVm
import com.kropotov.lovehate.ui.vm.feed.PostVm

class FeelingScreenFragment : BaseFragment<FeelingScreenVm, FragmentFeelingScreenBinding>(
    R.layout.fragment_feeling_screen
) {

    override val vmClass = FeelingScreenVm::class.java

    val opinionType: OpinionType by lazy {
        arguments?.serializable(FEELING_TYPE_ARG, OpinionType::class.java) ?: UNION
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListDelegationAdapter(adapterDelegate())
        adapter.items = listOf(
            PostVm("LanBoy", "03/02/2023 Вс 21:00:10", NEUTRAL, "любви", "Действительно мерзкое чувство, несущее в себе массу неприятностей, проблем, слёз...etc. Но иногда любовь - это богатейшая почва для сублимации, мощного жизненного двигателя.", "безразличен к"),
            PostVm("Jane Lost", "03/02/2023 Суб 14:55:11", LOVE, "спать", "Кто же не любит спать? Я лично уже в 22.00 могу отключиться, а спать я просто обожаю. Спишь себе и видишь сны с розовыми единорогами и ведьмами. Замечательно.",  "любит"),
            PostVm("asyaq", "03/02/2023 Ср 23:54:41", HATE, "кошки", "О, эта ненависть идёт еще с детства. За всю мою жизнь до данного момента у нас было 4 кошки и ДА! я так и не полюбила этих животных. Причём все кошки у нас были не ласковыми. Мне хотелось бы собаку, а при одном только упоминании о ней меня сразу же отшивали. Вот только из-за того, что мы заводим кошку, а не собаку, я их и ненавижу :[", "ненавидит")
        ).filter {
            opinionType == it.feeling || opinionType == UNION
        }
        binding.feelingsRecyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(SpaceItemDecoration(context))
        }

    }

    private fun adapterDelegate() =
        adapterDelegateViewBinding<PostVm, PostVm, ListItemPostBinding>(
            { layoutInflater, parent ->
                ListItemPostBinding.inflate(layoutInflater, parent, false)
            }
        ) {
            bind {
                binding.viewModel = item
            }
        }


    companion object {

        const val FEELING_TYPE_ARG = "arg_feeling_type"

        fun newInstance(type: OpinionType) =
            FeelingScreenFragment().withArgs {
                putSerializable(FEELING_TYPE_ARG, type)
            }
    }
}