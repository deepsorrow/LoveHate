package com.kropotov.lovehate.ui.dialogs.newopinion

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.items.MediaListItem
import com.kropotov.lovehate.databinding.DialogNewOpinionBinding
import com.kropotov.lovehate.ui.adapters.lists.OpinionNewMediaListAdapter
import com.kropotov.lovehate.ui.base.BaseBottomSheetDialogFragment
import com.kropotov.lovehate.ui.dialogs.pickmedia.PickMediaDialog.Companion.MEDIA_PICKED_KEY
import com.kropotov.lovehate.ui.screens.topicpage.fragments.TopicPageFragment.Companion.TOPIC_PAGE_ID
import com.kropotov.lovehate.ui.utilities.withArgs
import com.kropotov.lovehate.ui.utilities.getColorAttr
import com.kropotov.lovehate.ui.utilities.parcelable
import kotlinx.coroutines.launch

class NewOpinionDialog : BaseBottomSheetDialogFragment<NewOpinionViewModel, DialogNewOpinionBinding>(
    R.layout.dialog_new_opinion
) {

    override val vmClass = NewOpinionViewModel::class.java

    private var adapter: OpinionNewMediaListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = OpinionNewMediaListAdapter(viewModel.mediaPaths, childFragmentManager)
        binding.attachmentsList.adapter = adapter

        lifecycleScope.launch {
            viewModel.dismissDialog.collect {
                dismiss()
            }
        }

        lifecycleScope.launch {
            viewModel.opinionState.collect { opinionType ->
                val colorInt = requireContext().getColorAttr(opinionType.color)
                binding.button.setBackgroundColor(colorInt)
            }
        }

        childFragmentManager.setFragmentResultListener(MEDIA_PICKED_KEY, this) { _, bundle ->
            bundle.parcelable<MediaListItem>(MEDIA_PICKED_KEY)?.let { item ->
                adapter?.addMedia(item)
            }
        }
    }

    companion object {
        fun newInstance(topicId: Int) = NewOpinionDialog().withArgs {
            putInt(TOPIC_PAGE_ID, topicId)
        }
    }
}
