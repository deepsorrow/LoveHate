package com.kropotov.lovehate.ui.fragments.feed

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.google.android.material.color.MaterialColors
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kropotov.lovehate.R
import com.google.android.material.R as RMaterial
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.databinding.FragmentFeedBinding
import com.kropotov.lovehate.ui.MainScreenActivity.Companion.CHANGE_TOOLBAR_COLOR_EVENT
import com.kropotov.lovehate.ui.MainScreenActivity.Companion.NEW_FEELING_TYPE
import com.kropotov.lovehate.ui.adapters.FeelingsViewPagerAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.vm.feed.FeedVm
import com.kropotov.lovehate.ui.vm.ToolbarVm

class FeedFragment : BaseFragment<FeedVm, FragmentFeedBinding>(R.layout.fragment_feed) {

    override val vmClass = FeedVm::class.java

    val toolbarVm: ToolbarVm by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.feelingsPagerContainer.apply {
            adapter = FeelingsViewPagerAdapter(this@FeedFragment)
            offscreenPageLimit = 4 // To load all fragments in advance
        }

        setTabLayout()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun setTabLayout() {
        TabLayoutMediator(binding.feelingsTabLayout, binding.feelingsPagerContainer) { tab, position ->
            val tabView = LayoutInflater.from(requireContext()).inflate(R.layout.list_item_tab, null, false)
            val textView = tabView.findViewById<TextView>(R.id.tab_text)

            textView.text = resources.getString(OpinionType.entries[position].title)
            if (position == 0) { // UNION
                val iconsFont = ResourcesCompat.getFont(requireContext(), R.font.icons)
                textView.typeface = iconsFont
                tab.view.background = ResourcesCompat.getDrawable(resources, R.drawable.tab_layout_background, null)
            }

            tab.setCustomView(tabView)
        }.attach()

        binding.feelingsTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val textColor = MaterialColors.getColor(
                    requireContext(),
                    RMaterial.attr.colorOnPrimary,
                    Color.WHITE
                )

                tab?.run {
                    val layoutColorAttr = OpinionType.entries[tab.position].color
                    val tabLayoutColor = MaterialColors.getColor(requireContext(), layoutColorAttr, Color.WHITE)
                    view.findViewById<TextView>(R.id.tab_text)?.setTextColor(textColor)

                    DrawableCompat.setTint(
                        DrawableCompat.wrap(binding.feelingsTabLayout.background),
                        tabLayoutColor
                    )

                    parentFragmentManager.setFragmentResult(
                        CHANGE_TOOLBAR_COLOR_EVENT,
                        bundleOf(NEW_FEELING_TYPE to tab.position)
                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val color = MaterialColors.getColor(requireContext(), R.attr.unaccented_light_text_color, Color.GRAY)
                tab?.view?.findViewById<TextView>(R.id.tab_text)?.setTextColor(color)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        binding.feelingsTabLayout.setNeutralTabWidth()
        binding.feelingsTabLayout.setTabWidthAsWrapContent(0)
    }

    private fun TabLayout.setNeutralTabWidth() {
        val layout = (getChildAt(0) as LinearLayout).getChildAt(2) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 1.2f
        layout.layoutParams = layoutParams
    }

    private fun TabLayout.setTabWidthAsWrapContent(tabPosition: Int) {
        val layout = (this.getChildAt(0) as LinearLayout).getChildAt(tabPosition) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0f
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        layout.layoutParams = layoutParams
    }

    companion object {
        val tag: String = FeedFragment::class.java.simpleName

        fun newInstance() = FeedFragment()
    }
}