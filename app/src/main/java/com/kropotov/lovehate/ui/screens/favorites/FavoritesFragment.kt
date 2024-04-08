package com.kropotov.lovehate.ui.screens.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.R as RMaterial
import com.google.android.material.tabs.TabLayoutMediator
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.FragmentFavoritesBinding
import com.kropotov.lovehate.ui.adapters.viewpagers.FavoritesViewPagerAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import com.kropotov.lovehate.ui.utilities.TabLayoutInitializer.Companion.addOnTabCustomSelectedListener
import com.kropotov.lovehate.ui.utilities.getColorAttr
import com.kropotov.lovehate.ui.utilities.reduceDragSensitivity

class FavoritesFragment : BaseFragment<FavoritesViewModel, FragmentFavoritesBinding>(
    R.layout.fragment_favorites
) {
    override val vmClass = FavoritesViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pager.apply {
            adapter = FavoritesViewPagerAdapter(this@FavoritesFragment)
            reduceDragSensitivity()

            initTabLayout()
            binding.tabLayout.addOnTabCustomSelectedListener()
        }
    }

    private fun ViewPager2.initTabLayout() {
        TabLayoutMediator(binding.tabLayout, this) { tab, position ->
            val tabView = LayoutInflater.from(requireContext()).inflate(
                R.layout.list_item_tab,
                null,
                false
            )

            tabView.findViewById<TextView>(R.id.tab_text).apply {
                text = if (position == 0) {
                    val textColor = requireContext().getColorAttr(RMaterial.attr.colorOnPrimary)
                    setTextColor(textColor)
                    getString(R.string.topics)
                } else {
                    getString(R.string.opinions)
                }
            }
            tab.setCustomView(tabView)
        }.attach()
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}
