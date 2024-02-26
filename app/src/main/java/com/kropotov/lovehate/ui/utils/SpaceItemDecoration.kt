package com.kropotov.lovehate.ui.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.kropotov.lovehate.R

class SpaceItemDecoration(
    context: Context,
    @DimenRes offset: Int = R.dimen.small_offset
) : ItemDecoration() {
    private val decorationHeight: Int = context.resources.getDimensionPixelSize(offset)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = parent.getChildAdapterPosition(view)
        val totalCount = parent.adapter!!.itemCount
        if (itemPosition >= 0 && itemPosition < totalCount - 1) {
            outRect.bottom = decorationHeight
        }
    }
}