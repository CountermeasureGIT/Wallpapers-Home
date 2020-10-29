package ru.countermeasure.wallpapershome.presentation._system

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class StaggeredGridBorderDecoration(
    @Px private val borderSize: Int
) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val lm = parent.layoutManager as StaggeredGridLayoutManager
        val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams

        val spanCount = lm.spanCount
        val spanIndex = lp.spanIndex
        val position = parent.getChildAdapterPosition(view)

        if (spanIndex == 0)
            outRect.left = borderSize

        if (position < spanCount)
            outRect.top = borderSize

        outRect.right = borderSize
        outRect.bottom = borderSize
    }
}