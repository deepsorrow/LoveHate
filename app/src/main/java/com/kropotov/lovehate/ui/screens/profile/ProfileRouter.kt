package com.kropotov.lovehate.ui.screens.profile

import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.widget.PopupWindowCompat
import com.kropotov.lovehate.R
import com.kropotov.lovehate.data.AppTheme
import com.kropotov.lovehate.data.TopicType
import com.kropotov.lovehate.type.OpinionsListType
import com.kropotov.lovehate.ui.AuthActivity
import com.kropotov.lovehate.ui.base.BaseRouter
import com.kropotov.lovehate.ui.dialogs.SendFeedbackDialog
import com.kropotov.lovehate.ui.screens.favorites.FavoritesFragment
import com.kropotov.lovehate.ui.screens.feed.fragments.OpinionsFragment
import com.kropotov.lovehate.ui.screens.profile.fragments.ProfileFragment
import com.kropotov.lovehate.ui.screens.topics.fragments.TopicsFragment
import com.kropotov.lovehate.ui.utilities.SharedPreferencesHelper
import javax.inject.Inject

class ProfileRouter @Inject constructor(
    private val fragment: ProfileFragment,
    private val sharedPrefs: SharedPreferencesHelper
) : BaseRouter(fragment.childFragmentManager) {

    fun navigateToMyTopics() =
        navigateWithSlideUpTransition(TopicsFragment.newInstance(TopicType.BY_CURRENT_USER))

    fun navigateToMyOpinions() =
        navigateWithSlideUpTransition(OpinionsFragment.newInstance(OpinionsListType.BY_CURRENT_USER))

    fun navigateToMyComments() {
        TODO("Comments feature")
    }

    fun navigateToMyAchievements() {

    }

    fun navigateToFavorites() =
        navigateWithSlideUpTransition(FavoritesFragment.newInstance())

    fun showFeedbackDialog() {
        SendFeedbackDialog().show(fragment.parentFragmentManager, null)
    }

    fun showAppThemePopupDialog(view: View) {
        val anchor = view.findViewById<TextView>(R.id.current_theme)
        val popupWindow = PopupWindow(anchor.context).apply {
            isFocusable = true
        }
        val inflater = LayoutInflater.from(anchor.context)
        val contentView = inflater.inflate(R.layout.popup_app_theme_picker, null)
        contentView.findViewById<TextView>(R.id.theme).apply {
            if (sharedPrefs.getPreferredTheme() == AppTheme.LIGHT) {
                text = view.resources.getString(R.string.dark_theme)
                setOnClickListener {
                    AppTheme.DARK.onAppThemeClicked()
                    popupWindow.dismiss()
                }
            } else {
                text = view.resources.getString(R.string.light_theme)
                setOnClickListener {
                    AppTheme.LIGHT.onAppThemeClicked()
                    popupWindow.dismiss()
                }
            }
        }

        val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        contentView.measure(width, height)
        popupWindow.apply {
            this.contentView = contentView
            setBackgroundDrawable(null)
            elevation = POPUP_DROPDOWN_ELEVATION
        }

        val xOff = (anchor.measuredWidth - contentView.measuredWidth) / 2
        val yOff = 0
        PopupWindowCompat.showAsDropDown(popupWindow, anchor,  xOff, yOff, Gravity.BOTTOM)
    }

    fun logOut() {
        sharedPrefs.clearToken()
        fragment.requireContext().run {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
        fragment.requireActivity().finish()
    }

    private fun AppTheme.onAppThemeClicked() = run {
        sharedPrefs.savePreferredTheme(this)
        fragment.requireActivity().recreate()
    }

    private companion object {
        const val POPUP_DROPDOWN_ELEVATION = 10F
    }
}
