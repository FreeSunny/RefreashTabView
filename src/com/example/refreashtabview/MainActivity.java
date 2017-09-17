package com.example.refreashtabview;

import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.refreashtabview.adapter.SlidingPagerAdapter;
import com.example.refreashtabview.fragment.ScrollTabHolder;
import com.example.refreashtabview.fragment.ScrollTabHolderFragment;
import com.example.refreashtabview.sliding.PagerSlidingTabStrip;
import com.example.refreashtabview.wight.FixLinearLayout;
import com.example.refreashtabview.wight.WrapperTextView;
import com.nineoldandroids.view.ViewHelper;

/**
 *
 */
public class MainActivity extends ActionBarActivity implements OnPageChangeListener, ScrollTabHolder, View
        .OnClickListener {

    /**
     * click on time height add 20px
     */
    public static final int OFFSET_HEIGHT = 20;

    private PagerSlidingTabStrip tabs;

    private ViewPager viewPager;

    private SlidingPagerAdapter adapter;

    private FixLinearLayout header;

    private WrapperTextView descTextView;

    private int headerHeight;
    private int headerTranslationDis;

    private LinearLayout changeHeight;
    private TextView changBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getHeaderHeight();
        findViews();
        setViewsListener();
        initDesc();
        setupPager();
        setupTabs();
    }

    private void findViews() {
        tabs = (PagerSlidingTabStrip) findViewById(R.id.show_tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        header = (FixLinearLayout) findViewById(R.id.header);
        descTextView = (WrapperTextView) findViewById(R.id.show_event_detail_desc);
        changeHeight = (LinearLayout) findViewById(R.id.change_content);
        changBtn = (TextView) findViewById(R.id.change_height);
    }

    private void setViewsListener() {
        changBtn.setOnClickListener(this);
    }

    private void initDesc() {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(getString(R.string.head_title_desc));
        String link = getString(R.string.head_title_desc_link);
        SpannableString span = new SpannableString(link);
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_LONG).show();
            }
        }, 0, link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append(span);
        stringBuilder.append(" ");
        descTextView.setText(stringBuilder);
        descTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void getHeaderHeight() {
        headerHeight = getResources().getDimensionPixelSize(R.dimen.max_header_height);
        headerTranslationDis = -getResources().getDimensionPixelSize(R.dimen.header_offset_dis);
    }

    private void setupPager() {
        adapter = new SlidingPagerAdapter(getSupportFragmentManager(), this, viewPager);
        adapter.setTabHolderScrollingListener(this);//控制页面滑动
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
        currentHolder.adjustScroll((int) (header.getHeight() + ViewHelper.getTranslationY(header)));// 修正滚出去的偏移量
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

    private int headerTop = 0;

    // 刷新头部显示时，没有onScroll回调，只有刷新时有
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int
            pagePosition) {
        if (viewPager.getCurrentItem() != pagePosition) {
            return;
        }
        if (headerScrollSize == 0 && reLocation) {
            reLocation = false;
            return;
        }
        reLocation = false;
        int scrollY = Math.max(-getScrollY(view), headerTranslationDis);
        ViewHelper.setTranslationY(header, scrollY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_height:
                onChange();
                break;
        }
    }

    private void onChange() {
        ViewGroup.LayoutParams layoutParams = changeHeight.getLayoutParams();
        layoutParams.height += OFFSET_HEIGHT;
        changeHeight.setLayoutParams(layoutParams);
        changeHeight.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                changeHeight.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                headerHeight += OFFSET_HEIGHT;
                headerTranslationDis -= OFFSET_HEIGHT;
                notifyFragment();
            }
        });
    }

    /**
     *
     */
    private void notifyFragment() {
        ScrollTabHolderFragment[] fragments = adapter.getFragments();
        for (int i = 0; i < fragments.length; ++i) {
            ScrollTabHolderFragment fragment = fragments[i];
            if (fragment != null) {
                fragment.headerChange();
            }
        }
    }

    /**
     * 主要算这玩意，PullToRefreshListView插入了一个刷新头部，因此要根据不同的情况计算当前的偏移量</br>
     * <p/>
     * 当刷新时： 刷新头部显示，因此偏移量要加上刷新头的数值 未刷新时： 偏移量不计算头部。
     * <p/>
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

    /**
     * 与onHeadScroll互斥，不能同时执行
     *
     * @param isRefresh
     * @param value
     * @param pagePosition
     */
    @Override
    public void onHeaderScroll(boolean isRefresh, int value, int pagePosition) {
        if (viewPager.getCurrentItem() != pagePosition) {
            return;
        }
        headerScrollSize = value;
        ViewHelper.setTranslationY(header, -value);
    }

    @Override
    public int headerHeight() {
        if (headerHeight == 0) {// return default height
            return getResources().getDimensionPixelSize(R.dimen.max_header_height);
        }
        return headerHeight;
    }

}
