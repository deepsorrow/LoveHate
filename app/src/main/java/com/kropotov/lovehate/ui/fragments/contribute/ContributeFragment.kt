package com.kropotov.lovehate.ui.fragments.contribute

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.OpinionSortType
import com.kropotov.lovehate.databinding.FragmentContributeBinding
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.getColorAttr
import com.kropotov.lovehate.ui.viewmodels.contribute.ContributeViewModel
import kotlinx.coroutines.launch

class ContributeFragment : BaseFragment<ContributeViewModel, FragmentContributeBinding>(
    R.layout.fragment_contribute
) {

    override val vmClass = ContributeViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToOpinionTypeState()
        subscribeToCharacterCountdown()
    }

    private fun subscribeToOpinionTypeState() {
        lifecycleScope.launch {
            viewModel.opinionType.collect { selectedOpinionType ->
                binding.loveToggleButton.isChecked = selectedOpinionType == OpinionSortType.LOVE
                binding.neutralToggleButton.isChecked = selectedOpinionType == OpinionSortType.INDIFFERENCE
                binding.hateToggleButton.isChecked = selectedOpinionType == OpinionSortType.HATE

                val colorTint = requireContext().getColorAttr(selectedOpinionType.color)
                val widthPx = resources.getDimension(R.dimen.one_dp).toInt()
                (binding.topicEditText.background as GradientDrawable).setStroke(widthPx, colorTint)
                (binding.commentEditText.background as GradientDrawable).setStroke(widthPx, colorTint)
            }
        }

        binding.loveToggleButton.preventUncheckItself()
        binding.neutralToggleButton.preventUncheckItself()
        binding.hateToggleButton.preventUncheckItself()
    }

    private fun CompoundButton.preventUncheckItself() {
        setOnCheckedChangeListener { compoundButton, _ ->
            if (!binding.loveToggleButton.isChecked && !binding.neutralToggleButton.isChecked && !binding.hateToggleButton.isChecked) {
                compoundButton.isChecked = !compoundButton.isChecked
            }
        }
    }

    private fun subscribeToCharacterCountdown() {
        val topicMaxLength = resources.getInteger(R.integer.topic_title_max_length)
        binding.titleCountdown.text = "$topicMaxLength"
        binding.topicEditText.addTextChangedListener {
            if (it != null) {
                binding.titleCountdown.text = (topicMaxLength - it.length).toString()
            }
        }

        val commentMaxLength = resources.getInteger(R.integer.comment_max_length)
        binding.commentCountdown.text = "$commentMaxLength"
        binding.commentEditText.addTextChangedListener {
            if (it != null) {
                binding.commentCountdown.text = (commentMaxLength - it.length).toString()
            }
        }
    }
    companion object {

        fun newInstance() = ContributeFragment()
    }
}