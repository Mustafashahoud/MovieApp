package com.mustafa.movieguideapp.view.ui.common

import android.widget.AbsListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class InfinitePager(val adapter: RecyclerView.Adapter<*>) :
    RecyclerView.OnScrollListener() {
    var isScrolling = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = when (recyclerView.layoutManager) {
            is LinearLayoutManager -> {
                recyclerView.layoutManager as LinearLayoutManager
            }
            else -> {
                recyclerView.layoutManager as GridLayoutManager
            }
        }
        val lastPosition = layoutManager.findLastVisibleItemPosition()
        if (lastPosition == adapter.itemCount - 1) {
            if (isScrolling) {
                if (loadMorecondition()) {
                    loadMore()
                    isScrolling = false
                }
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            isScrolling = true
        }
    }



    abstract fun loadMorecondition(): Boolean
    abstract fun loadMore()
}