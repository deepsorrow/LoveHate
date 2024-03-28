package com.kropotov.lovehate.ui.screens.contribute.fragments

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.FragmentContributeBinding
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.screens.contribute.ContributeViewModel
import com.kropotov.lovehate.ui.utilities.getColorAttr
import com.kropotov.lovehate.ui.screens.contribute.ContributeRouter
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContributeFragment : BaseFragment<ContributeViewModel, FragmentContributeBinding>(
    R.layout.fragment_contribute
) {

    override val vmClass = ContributeViewModel::class.java

    @Inject
    lateinit var router: ContributeRouter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.router = router
        lifecycleScope.launch {
            viewModel.navigateToNewTopic.collect { id ->
                router.navigateToNewTopic(id)
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
                (binding.commentEditText.background as GradientDrawable).setStroke(widthPx, colorTint)
                binding.button.setBackgroundColor(colorTint)
            }
        }
    }

    private fun subscribeToCharacterCountdown() {
        val commentMaxLength = resources.getInteger(R.integer.opinion_max_length)

        binding.commentTextCounter.text = "$commentMaxLength"
        binding.commentEditText.addTextChangedListener {
            if (it != null) {
                binding.commentTextCounter.text = (commentMaxLength - it.length).toString()
            }
        }
    }

    companion object {

        fun newInstance() = ContributeFragment()
    }
}
