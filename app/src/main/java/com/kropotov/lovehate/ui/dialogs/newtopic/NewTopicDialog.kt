package com.kropotov.lovehate.ui.dialogs.newtopic

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.items.MediaListItem
import com.kropotov.lovehate.databinding.DialogNewTopicBinding
import com.kropotov.lovehate.ui.adapters.lists.OpinionNewMediaListAdapter
import com.kropotov.lovehate.ui.base.BaseBottomSheetDialogFragment
import com.kropotov.lovehate.ui.dialogs.pickmedia.PickMediaDialog.Companion.MEDIA_PICKED_KEY
import com.kropotov.lovehate.ui.utilities.getColorAttr
import com.kropotov.lovehate.ui.utilities.parcelable
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewTopicDialog : BaseBottomSheetDialogFragment<NewTopicViewModel, DialogNewTopicBinding>(
    R.layout.dialog_new_topic
) {

    override val vmClass = NewTopicViewModel::class.java

    @Inject
    lateinit var router: NewTopicRouter

    private var adapter: OpinionNewMediaListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED

        adapter = OpinionNewMediaListAdapter(viewModel.mediaPaths, childFragmentManager)
        binding.router = router
        binding.attachmentsList.adapter = adapter
        lifecycleScope.launch {
            viewModel.navigateToNewTopic.collect { id ->
                router.navigateToTopicPage(id)
                dismiss()
            }
        }

        childFragmentManager.setFragmentResultListener(MEDIA_PICKED_KEY, this) { _, bundle ->
            bundle.parcelable<MediaListItem>(MEDIA_PICKED_KEY)?.let { item ->
                adapter?.addMedia(item)
            }
        }

        subscribeToOpinionTypeState()
        subscribeToCharacterCountdown()
    }

    private fun subscribeToOpinionTypeState() {
        lifecycleScope.launch {
            viewModel.opinionType.collect { selectedOpinionType ->
                val colorTint = requireContext().getColorAttr(selectedOpinionType.color)
                val widthPx = resources.getDimension(R.dimen.one_dp).toInt()
                (binding.opinionEditText.background as GradientDrawable).setStroke(widthPx, colorTint)
                binding.button.setBackgroundColor(colorTint)
            }
        }
    }

    private fun subscribeToCharacterCountdown() {
        val commentMaxLength = resources.getInteger(R.integer.opinion_max_length)

        binding.opinionTextCounter.text = "$commentMaxLength"
        binding.opinionEditText.addTextChangedListener {
            if (it != null) {
                binding.opinionTextCounter.text = (commentMaxLength - it.length).toString()
            }
        }
    }

    companion object {

        fun newInstance() = NewTopicDialog()
    }
}
