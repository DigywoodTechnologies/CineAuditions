package com.digywood.cineauditions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by prasa on 05-04-2017.
 */


public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
    public interface OnItemClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);

    }

    private GestureDetector gestureDetector;
    private OnItemClickListener myclicklistener;



    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, OnItemClickListener clickListener) {
        myclicklistener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && myclicklistener != null) {
                    myclicklistener.onLongClick(child, recyclerView.indexOfChild(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {


        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && myclicklistener != null && gestureDetector.onTouchEvent(e)) {
            myclicklistener.onClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

