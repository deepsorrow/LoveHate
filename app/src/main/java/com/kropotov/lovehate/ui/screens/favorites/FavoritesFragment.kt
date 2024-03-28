package com.kropotov.lovehate.ui.screens.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.tabs.TabLayoutMediator
import com.kropotov.lovehate.R
import com.kropotov.lovehate.databinding.FragmentFavoritesBinding
import com.kropotov.lovehate.ui.adapters.viewpagers.FavoritesViewPagerAdapter
import com.kropotov.lovehate.ui.base.BaseFragment
import java.lang.ref.WeakReference

class FavoritesFragment : BaseFragment<FavoritesViewModel, FragmentFavoritesBinding>(
    R.layout.fragment_favorites
) {
    override val vmClass = FavoritesViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favoritesPagerContainer.apply {
            adapter = FavoritesViewPagerAdapter(this@FavoritesFragment)

            TabLayoutMediator(binding.favoritesTabLayout, this) { tab, position ->
                val tabView = LayoutInflater.from(requireContext()).inflate(
                    R.layout.list_item_tab,
                    null,
                    false
                )

                tabView.findViewById<TextView>(R.id.tab_text).apply {
                    text = if (position == 0) {
                        getString(R.string.topics)
                    } else {
                        getString(R.string.opinions)
                    }
                }
                tab.setCustomView(tabView)
            }.attach()
        }
        viewModel.toolbar.arrowBackAction.set {
            val weakThis = WeakReference(parentFragmentManager)
            weakThis.get()?.popBackStack()
        }
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}
