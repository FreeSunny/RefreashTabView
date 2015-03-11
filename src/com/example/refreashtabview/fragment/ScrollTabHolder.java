package com.example.refreashtabview.fragment;

import android.widget.AbsListView;

    public interface ScrollTabHolder {

        void adjustScroll(int scrollHeight);

        void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition);

        void onHeaderScroll(boolean isRefreashing, int value, int pagePosition);
    }
