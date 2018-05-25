package com.yotharit.savemeapplication

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import com.yotharit.savemeapplication.RecycleTouchListener.ClickListener
import android.view.GestureDetector
import android.R.attr.onClick
import android.text.method.Touch.onTouchEvent





class RecycleTouchListener(var context:Context,final var recyclerView : RecyclerView,
                           final var clickListener : ClickListener) : RecyclerView.OnItemTouchListener {

    var gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val child = recyclerView.findChildViewUnder(e.x, e.y)
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child))
                }
            }
        })
    }

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {

    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.getX(), e.getY())
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildAdapterPosition(child))
        }
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    public interface ClickListener {

        fun onClick(view: View, position: Int)

        fun onLongClick(view: View, position: Int)
    }
}