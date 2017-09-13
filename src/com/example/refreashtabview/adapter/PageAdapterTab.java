package com.example.refreashtabview.adapter;

import android.support.v4.app.Fragment;

import com.example.refreashtabview.R;
import com.example.refreashtabview.fragment.Tab1ListFragment;
import com.example.refreashtabview.fragment.Tab2ListFragment;
import com.example.refreashtabview.fragment.Tab3ListFragment;

/**
 * @author sunyoujun
 */
public enum PageAdapterTab {
    PAGE_TAB1(0, Tab1ListFragment.class, R.string.page_tab1),

    PAGE_TAB2(1, Tab2ListFragment.class, R.string.page_tab2),

    PAGE_TAB3(2, Tab3ListFragment.class, R.string.page_tab3),;

    public final int tabIndex;

    public final Class<? extends Fragment> clazz;

    public final int resId;

    public final int fragmentId;

    private PageAdapterTab(int index, Class<? extends Fragment> clazz, int resId) {
        this.tabIndex = index;
        this.clazz = clazz;
        this.resId = resId;
        this.fragmentId = index;
    }

    public static final PageAdapterTab fromTabIndex(int tabIndex) {
        for (PageAdapterTab value : PageAdapterTab.values()) {
            if (value.tabIndex == tabIndex) {
                return value;
            }
        }

        return null;
    }
}
