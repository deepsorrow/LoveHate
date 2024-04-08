package com.kropotov.lovehate.ui.utilities

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kropotov.lovehate.R
import com.google.android.material.R as RMaterial
import com.kropotov.lovehate.data.OpinionType
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.base.ToolbarContract

/**
 * Inits TabLayout with 4 tabs: [OpinionType.UNION], [OpinionType.LOVE],
 * [OpinionType.INDIFFERENCE], [OpinionType.HATE], where first tab is icon, the rest are text.
 * Sets appropriate width for each tab and click listener with color-changing response.
 */
interface TabLayoutInitializer {

    fun BaseFragment<*, *>.initTabLayout(
        toolbarContract: ToolbarContract,
        tabLayout: TabLayout,
        viewPager: ViewPager2,
        container: ViewGroup
    ) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val tabView = LayoutInflater.from(requireContext()).inflate(
                R.layout.list_item_tab,
                null,
                false
            )
            val textView = tabView.findViewById<TextView>(R.id.tab_text)

            textView.text = resources.getString(OpinionType.entries[position].title)
            if (position == 0) {
                val iconsFont = ResourcesCompat.getFont(requireContext(), R.font.icons)
                val textColor = requireContext().getColorAttr(RMaterial.attr.colorOnPrimary)
                textView.typeface = iconsFont
                textView.setTextColor(textColor)
            }

            tab.setCustomView(tabView)
        }.attach()

        tabLayout.setNeutralTabWidth()
        tabLayout.setHateTabWidth()
        tabLayout.setTabWidthAsWrapContent(0)
        setOnTabSelectedListener(toolbarContract, tabLayout, container)
    }

    private fun BaseFragment<*, *>.setOnTabSelectedListener(
        toolbarContract: ToolbarContract,
        tabLayout: TabLayout,
        container: ViewGroup
    ) {
        // On select tab highlight it with new color
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val textColor =
                    requireContext().getColorAttr(RMaterial.attr.colorOnPrimary)

                tab?.run {
                    val opinionType = OpinionType.entries[position]
                    val colorAttr = opinionType.color
                    toolbarContract.toolbarColor.set(colorAttr)

                    val tabColor = requireContext().getColorAttr(colorAttr)
                    view.findViewById<TextView>(R.id.tab_text)?.setTextColor(textColor)

                    val wrappedDrawable = DrawableCompat.wrap(tabLayout.background.mutate())
                    DrawableCompat.setTint(wrappedDrawable, tabColor)

                    val containerColor = requireContext().getColorAttr(opinionType.containerColor)
                    container.setBackgroundColor(containerColor)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val color =
                    requireContext().getColorAttr(R.attr.unaccented_light_text_color_variant1)
                tab?.view?.findViewById<TextView>(R.id.tab_text)?.setTextColor(color)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun TabLayout.setNeutralTabWidth() {
        val neutralTabIndex = 2
        val layout = (getChildAt(0) as LinearLayout).getChildAt(neutralTabIndex) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = NEUTRAL_TAB_WIDTH_WEIGHT
        layout.layoutParams = layoutParams
    }

    private fun TabLayout.setHateTabWidth() {
        val hateTabIndex = 3
        val layout = (getChildAt(0) as LinearLayout).getChildAt(hateTabIndex) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = HATE_TAB_WIDTH_WEIGHT
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
        private const val NEUTRAL_TAB_WIDTH_WEIGHT = 1.4f
        private const val HATE_TAB_WIDTH_WEIGHT = 1.2f

        fun TabLayout.addOnTabCustomSelectedListener() {
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(p0: TabLayout.Tab?) {
                    p0?.updateTabTextColor(RMaterial.attr.colorOnPrimary)
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                    p0?.updateTabTextColor(R.attr.unaccented_light_text_color_variant1)
                }

                override fun onTabReselected(p0: TabLayout.Tab?) { }
            })
        }

        private fun TabLayout.Tab.updateTabTextColor(@AttrRes colorAttr: Int) {
            val textColor = view.context.getColorAttr(colorAttr)
            customView?.findViewById<TextView>(R.id.tab_text)?.setTextColor(textColor)
        }
    }
}
