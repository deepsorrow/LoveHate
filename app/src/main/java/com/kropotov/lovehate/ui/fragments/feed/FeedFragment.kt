package com.kropotov.lovehate.ui.fragments.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kropotov.lovehate.R
import com.google.android.material.R as RMaterial
import com.kropotov.lovehate.data.OpinionSortType
import com.kropotov.lovehate.databinding.FragmentFeedBinding
import com.kropotov.lovehate.MainScreenActivity.Companion.CHANGE_CONTAINER_COLOR
import com.kropotov.lovehate.MainScreenActivity.Companion.NEW_FEELING_TYPE
import com.kropotov.lovehate.ui.adapters.OpinionsViewPagerAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.getColorAttr
import com.kropotov.lovehate.ui.viewmodels.feed.FeedViewModel

class FeedFragment : BaseFragment<FeedViewModel, FragmentFeedBinding>(R.layout.fragment_feed) {

    override val vmClass = FeedViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.opinionsPagerContainer.apply {
            adapter = OpinionsViewPagerAdapter(this@FeedFragment)
        }

        initTabLayout()
        setOnTabSelectedListener()
    }

    override fun onResume() {
        super.onResume()
        syncStatusBarColor(isResume = true)
    }

    override fun onPause() {
        super.onPause()
        syncStatusBarColor(isResume = false)
    }

    private fun initTabLayout() {
        TabLayoutMediator(binding.opinionsTabLayout, binding.opinionsPagerContainer) { tab, position ->
            val tabView = LayoutInflater.from(requireContext()).inflate(R.layout.list_item_tab, null, false)
            val textView = tabView.findViewById<TextView>(R.id.tab_text)

            textView.text = resources.getString(OpinionSortType.entries[position].title)
            if (position == 0) { // UNION
                val iconsFont = ResourcesCompat.getFont(requireContext(), R.font.icons)
                textView.typeface = iconsFont
                tab.view.background = ResourcesCompat.getDrawable(resources, R.drawable.tab_layout_background, null)
            }

            tab.setCustomView(tabView)
        }.attach()

        binding.opinionsTabLayout.setNeutralTabWidth()
        binding.opinionsTabLayout.setTabWidthAsWrapContent(0)
    }

    private fun setOnTabSelectedListener() {
        // On select tab highlight it with color and change statusBarColor
        binding.opinionsTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val textColor = requireContext().getColorAttr(RMaterial.attr.colorOnPrimary)

                tab?.run {
                    val colorAttr = OpinionSortType.entries[position].color
                    viewModel.toolbar.toolbarColor.set(colorAttr)

                    val tabColor = requireContext().getColorAttr(colorAttr)
                    view.findViewById<TextView>(R.id.tab_text)?.setTextColor(textColor)

                    val wrappedDrawable = DrawableCompat.wrap(binding.opinionsTabLayout.background)
                    DrawableCompat.setTint(wrappedDrawable, tabColor)

                    val params = bundleOf(NEW_FEELING_TYPE to tab.position)
                    parentFragmentManager.setFragmentResult(CHANGE_CONTAINER_COLOR, params)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val color = requireContext().getColorAttr(R.attr.unaccented_light_text_color)
                tab?.view?.findViewById<TextView>(R.id.tab_text)?.setTextColor(color)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
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

    private fun syncStatusBarColor(isResume: Boolean) {
        val colorAttr = viewModel.toolbar.toolbarColor.get()
        requireActivity().window.statusBarColor = if (isResume) {
            requireContext().getColorAttr(colorAttr)
        } else {
            requireContext().getColorAttr(androidx.appcompat.R.attr.colorPrimary)
        }
    }

    companion object {
        val tag: String = FeedFragment::class.java.simpleName

        fun newInstance() = FeedFragment()
    }
}