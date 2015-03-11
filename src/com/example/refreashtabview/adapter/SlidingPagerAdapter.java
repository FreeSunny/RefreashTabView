package com.example.refreashtabview.adapter;

import java.util.List;

import com.example.refreashtabview.fragment.ScrollTabHolder;
import com.example.refreashtabview.fragment.ScrollTabHolderFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;

public class SlidingPagerAdapter extends FragmentPagerAdapter {

	protected final ScrollTabHolderFragment[] fragments;

	protected final Context context;

	private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
	private ScrollTabHolder mListener;

	public int getCacheCount() {
		return PageAdapterTab.values().length;
	}

	public SlidingPagerAdapter(FragmentManager fm, Context context, ViewPager pager) {
		super(fm);
		fragments = new ScrollTabHolderFragment[PageAdapterTab.values().length];
		this.context = context;
		mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
		init(fm);
	}

	private void init(FragmentManager fm) {
		for (PageAdapterTab tab : PageAdapterTab.values()) {
			try {
				ScrollTabHolderFragment fragment = null;

				List<Fragment> fs = fm.getFragments();
				if (fs != null) {
					for (Fragment f : fs) {
						if (f.getClass() == tab.clazz) {
							fragment = (ScrollTabHolderFragment) f;
							break;
						}
					}
				}

				if (fragment == null) {
					fragment = (ScrollTabHolderFragment) tab.clazz.newInstance();
				}

				fragments[tab.tabIndex] = fragment;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public void setTabHolderScrollingListener(ScrollTabHolder listener) {
		mListener = listener;
	}

	@Override
	public ScrollTabHolderFragment getItem(int pos) {
		ScrollTabHolderFragment fragment = fragments[pos];
		mScrollTabHolders.put(pos, fragment);
		if (mListener != null) {
			fragment.setScrollTabHolder(mListener);
		}
		return fragment;
	}

	public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
		return mScrollTabHolders;
	}

	@Override
	public int getCount() {
		return PageAdapterTab.values().length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		PageAdapterTab tab = PageAdapterTab.fromTabIndex(position);
		int resId = tab != null ? tab.resId : 0;
		return resId != 0 ? context.getText(resId) : "";
	}

}
