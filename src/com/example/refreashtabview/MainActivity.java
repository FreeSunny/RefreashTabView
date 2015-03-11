package com.example.refreashtabview;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.example.refreashtabview.adapter.SlidingPagerAdapter;
import com.example.refreashtabview.fragment.ScrollTabHolder;
import com.example.refreashtabview.sliding.PagerSlidingTabStrip;
import com.nineoldandroids.view.ViewHelper;

public class MainActivity extends ActionBarActivity implements OnPageChangeListener, ScrollTabHolder {

	private PagerSlidingTabStrip tabs;

	private ViewPager viewPager;

	private SlidingPagerAdapter adapter;

	private LinearLayout header;

	private int headerHeight;
	private int headerTranslationDis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		getHeaderHeight();
		findViews();
		setupPager();
		setupTabs();
	}

	private void findViews() {
		tabs = (PagerSlidingTabStrip) findViewById(R.id.show_tabs);
		viewPager = (ViewPager) findViewById(R.id.pager);
		header = (LinearLayout) findViewById(R.id.header);
	}

	private void getHeaderHeight() {
		headerHeight = getResources().getDimensionPixelSize(R.dimen.max_header_height);
		headerTranslationDis = -getResources().getDimensionPixelSize(R.dimen.header_offset_dis);
	}

	private void setupPager() {
		adapter = new SlidingPagerAdapter(getSupportFragmentManager(), this, viewPager);
		adapter.setTabHolderScrollingListener(this);//控制页面上滑
		viewPager.setOffscreenPageLimit(adapter.getCacheCount());
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
	}

	private void setupTabs() {
		tabs.setShouldExpand(true);
		tabs.setIndicatorColorResource(R.color.color_purple_bd6aff);
		tabs.setUnderlineColorResource(R.color.color_purple_bd6aff);
		tabs.setCheckedTextColorResource(R.color.color_purple_bd6aff);
		tabs.setViewPager(viewPager);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		tabs.onPageScrolled(position, positionOffset, positionOffsetPixels);
	}

	@Override
	public void onPageSelected(int position) {
		tabs.onPageSelected(position);
		reLocation = true;
		SparseArrayCompat<ScrollTabHolder> scrollTabHolders = adapter.getScrollTabHolders();
		ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);
		if (NEED_RELAYOUT) {
			currentHolder.adjustScroll((int) (header.getHeight() + headerTop));// 修正滚出去的偏移量
		} else {
			currentHolder.adjustScroll((int) (header.getHeight() + ViewHelper.getTranslationY(header)));// 修正滚出去的偏移量
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		tabs.onPageScrollStateChanged(state);
	}

	@Override
	public void adjustScroll(int scrollHeight) {

	}

	private boolean reLocation = false;

	private int headerScrollSize = 0;

	public static final boolean NEED_RELAYOUT = Integer.valueOf(Build.VERSION.SDK).intValue() < Build.VERSION_CODES.HONEYCOMB;

	private int headerTop = 0;

	// 刷新头部显示时，没有onScroll回调，只有当刷新时会有
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount,
			int pagePosition) {
		if (viewPager.getCurrentItem() != pagePosition) {
			return;
		}
		if (headerScrollSize == 0 && reLocation) {
			reLocation = false;
			return;
		}
		reLocation = false;
		int scrollY = Math.max(-getScrollY(view), headerTranslationDis);
		if (NEED_RELAYOUT) {
			headerTop = scrollY;
			header.post(new Runnable() {

				@Override
				public void run() {
					Log.e("Main", "scorry1="+ headerTop);
					header.layout(0, headerTop, header.getWidth(), headerTop + header.getHeight());
				}
			});
		} else {
			ViewHelper.setTranslationY(header, scrollY);
		}
	}

	/**
	 * 主要算这玩意，PullToRefreshListView插入了一个刷新头部，因此要根据不同的情况计算当前的偏移量</br>
	 * 
	 * 当刷新时： 刷新头部显示，因此偏移量要加上刷新头的数值 未刷新时： 偏移量不计算头部。
	 * 
	 * firstVisiblePosition >1时，listview中的项开始显示，姑且认为每一项等高来计算偏移量（其实只要显示一个项，向上偏移
	 * 量已经大于头部的最大偏移量，因此不准确也没有关系）
	 * 
	 * @param view
	 * @return
	 */
	public int getScrollY(AbsListView view) {
		View c = view.getChildAt(0);
		if (c == null) {
			return 0;
		}
		int top = c.getTop();
		int firstVisiblePosition = view.getFirstVisiblePosition();
		if (firstVisiblePosition == 0) {
			return -top + headerScrollSize;
		} else if (firstVisiblePosition == 1) {
			return -top;
		} else {
			return -top + (firstVisiblePosition - 2) * c.getHeight() + headerHeight;
		}
	}

	// 与onHeadScroll互斥，不能同时执行
	@Override
	public void onHeaderScroll(boolean isRefreashing, int value, int pagePosition) {
		if (viewPager.getCurrentItem() != pagePosition) {
			return;
		}
		headerScrollSize = value;
		if (NEED_RELAYOUT) {
			header.post(new Runnable() {

				@Override
				public void run() {
					Log.e("Main", "scorry="+ (-headerScrollSize));
					header.layout(0, -headerScrollSize, header.getWidth(), -headerScrollSize + header.getHeight());
				}
			});
		}else{
			ViewHelper.setTranslationY(header, -value);
		}
	}

}
