package com.example.refreashtabview.fragment;

import android.support.v4.app.Fragment;
import android.widget.AbsListView;

public abstract class ScrollTabHolderFragment extends Fragment implements ScrollTabHolder {

	private int fragmentId;

	protected ScrollTabHolder scrollTabHolder;

	public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
		this.scrollTabHolder = scrollTabHolder;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount,
			int pagePosition) {
		// nothing
	}

	@Override
	public void onHeaderScroll(boolean isRefreashing, int value, int pagePosition) {

	}

	public int getFragmentId() {
		return fragmentId;
	}

	public void setFragmentId(int fragmentId) {
		this.fragmentId = fragmentId;
	}
}