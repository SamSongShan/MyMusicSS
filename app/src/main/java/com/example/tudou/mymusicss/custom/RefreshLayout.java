package com.example.tudou.mymusicss.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.tudou.mymusicss.R;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 切记用的时候要设置marginTop为负头部控件的高以隐藏头部控件
 * scrollBy()和scrollTo()只能滑动View的内容，而不能滑动View的本身。
 * 所以用设置marginTop的方法,不然底部会出现空白
 */

public class RefreshLayout extends LinearLayout {
    private boolean interrupt = false;
    private int yMove;
    private int yLast;
    private int y;
    private int i;
    private boolean isRefresh = false;
    private Scroller scroller = new Scroller(getContext());
    private ScrollView scrollView;
    private RecyclerView recyclerView;
    private ListView listView;
    private GridView gridView;


    private int rvScrolled = 0;
    private int lvScrolled = 0;
    private OnRefreshListener onRefreshListener;
    private boolean isNeedUp = true;//是否箭头向上运动
    private boolean isNeedDown = true;//是否箭头向上运动

    private ImageView imageView;
    private ProgressBar progressBar;
    SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm ");
    private String currentDate;
    private String lastDate;
    private TextView tvDate;


    public RefreshLayout(Context context) {
        super(context);
        init();
    }


    public RefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setBackgroundColor(Color.parseColor("#FFB81F"));
        addHeadRefresh();
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //Log.e("getChildAt(0).getHeight()", getChildAt(0).getHeight() + "");
        //  scrollTo(0, getChildAt(0).getHeight());
       /* LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.topMargin = -getChildAt(0).getHeight();

        requestLayout();*/
        checkScrollView();
    }

    private void checkScrollView() {
        //检测是否有scrollview
        for (int j = 0; j < getChildCount(); j++) {
            final Object v = getChildAt(j);
            if (v instanceof ScrollView) {
                scrollView = (ScrollView) v;
            } else if (v instanceof RecyclerView) {
                recyclerView = (RecyclerView) v;

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        rvScrolled += dy;
                    }
                });
                recyclerView.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);

                    }

                    @Override
                    public void onItemRangeRemoved(int positionStart, int itemCount) {
                        super.onItemRangeRemoved(positionStart, itemCount);
                    }
                });
            } else if (v instanceof ListView) {
                listView = (ListView) v;
                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    private int scrollState;
                    private int firstVisibleItem;

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                        this.scrollState = scrollState;

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        this.firstVisibleItem = firstVisibleItem;
                    }
                });

            } else if (v instanceof GridView) {
                gridView = (GridView) v;
                gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    private int scrollState;
                    private int firstVisibleItem;

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                        this.scrollState = scrollState;

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        this.firstVisibleItem = firstVisibleItem;
                    }
                });

            }
        }
    }

    private void smoothToScroll(int destaY) {
        //scroller.startScroll(0, getScrollY(), 0, destaY, 500);
        scrollTo(0, 0);
        LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();

        if (destaY > 0) {
            layoutParams.topMargin = 0;

        } else {
            layoutParams.topMargin = destaY;

        }
        //invalidate();
        requestLayout();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            //Log.e("getCurrY()", scroller.getCurrY() + "");
            //Log.e("getChildAt(0).getHeight()", getChildAt(0).getHeight() + "");
            if (isRefresh) {
                LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
                layoutParams.topMargin = 0;
                // scrollTo(0, 0);
            } else {
                LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
                layoutParams.topMargin = -getChildAt(0).getHeight();
                // scrollTo(0, getChildAt(0).getHeight());
            }
            requestLayout();
            //postInvalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        y = (int) ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                interrupt = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (y - yLast > 0) {
                    //检测下拉操作
                    if (isMoveScrollView(ev) || isMoveRecyclerView(ev) || isMoveListView(ev) || isMoveGridView(ev)) {
                        //如果是在滑动scrollview 不拦截
                        interrupt = false;
                    } else {
                        //否则拦截
                        interrupt = true;
                    }
                } else {
                    //上拉不拦截
                    interrupt = false;
                }
                break;
            case MotionEvent.ACTION_UP:

                interrupt = false;
                break;
        }
        yLast = y;
        return interrupt;
    }

    private boolean isMoveRecyclerView(MotionEvent ev) {
        if (recyclerView == null) {
            return false;
        }
        View head = getChildAt(0);
        //检测点击区域是不是在scrollview
       // Log.e("ev.getY()", "ev.getY(): " + ev.getY());
       // Log.e("recyclerView.getTop()", "recyclerView.getTop(): " + recyclerView.getTop());
       // Log.e("head.getHeight()", "head.getHeight(): " + head.getHeight());
       // Log.e("recyclerView.getBottom()", "recyclerView.getBottom(): " + recyclerView.getBottom());
       // Log.e("rvScrolled", "rvScrolled: " + rvScrolled);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();


        if (ev.getY() > (recyclerView.getTop() - head.getHeight()) && ev.getY() <=
                (recyclerView.getBottom() - head.getHeight()) && pastVisiblesItems != 0) {
            return true;
        }

        return false;
    }


    private boolean isMoveListView(MotionEvent ev) {
        if (listView == null) {
            return false;
        }
        View head = getChildAt(0);
        //检测点击区域是不是在scrollview

       // Log.e("ev.getY()", "ev.getY(): " + ev.getY());
       // Log.e("listView.getTop()", "listView.getTop(): " + listView.getTop());
       // Log.e("head.getHeight()", "head.getHeight(): " + head.getHeight());
       // Log.e("listView.getBottom()", "listView.getBottom(): " + listView.getBottom());


        if (ev.getY() > (listView.getTop() - head.getHeight()) && ev.getY() <= (listView.getBottom() - head.getHeight()) && listView.getFirstVisiblePosition() != 0) {
            return true;
        }

        return false;
    }

    private boolean isMoveGridView(MotionEvent ev) {
        if (gridView == null) {
            return false;
        }
        View head = getChildAt(0);
        //检测点击区域是不是在scrollview


        if (ev.getY() > (gridView.getTop() - head.getHeight()) && ev.getY() <= (gridView.getBottom() - head.getHeight()) && gridView.getFirstVisiblePosition() != 0) {
            return true;
        }

        return false;
    }

    private boolean isMoveScrollView(MotionEvent ev) {
        if (scrollView == null) {
            return false;
        }
        View head = getChildAt(0);
        //检测点击区域是不是在scrollview
        if (ev.getY() > (scrollView.getTop() - head.getHeight()) && ev.getY() <=
                (scrollView.getBottom() - head.getHeight()) && scrollView.getScrollY() != 0) {
            return true;
        }

        return false;
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //动画还没结束的时候，直接消耗掉事件,不处理。
        if (!scroller.isFinished()) {
            return true;
        }
        tvDate.setText("上次更新" + lastDate);

        y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                yMove = y - yLast;
                if (yMove >= 0) {
                    i += yMove / 3;
                    scrollBy(0, -yMove / 3);   //  /3为了让下拉有感觉
                    /*LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
                    layoutParams.topMargin = -yMove / 3;*/


                } else {
                    i += yMove / 3;
                    scrollBy(0, -yMove / 3);   //  /3为了让下拉有感觉
                   /* LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
                    layoutParams.topMargin = -yMove / 3;*/

                }
                //Log.e("iiiiii", i + "");
                //Log.e("yMove", yMove + "");
                if (isNeedUp && i >= getChildAt(0).getHeight() * 1.2 && yMove >= 0) {
                    isNeedUp = false;
                    startRadius(0f, 180f);

                }

                if (isNeedDown && i < getChildAt(0).getHeight() * 1.2 && yMove < 0) {
                    isNeedDown = false;
                    startRadius(180f, 360f);
                }


                break;
            case MotionEvent.ACTION_UP:
               // Log.e("i", i + "");
               // Log.e("getChildAt(0).getHeight()", getChildAt(0).getHeight() + "");

                if (i >= getChildAt(0).getHeight() * 1.2) {
                    imageView.clearAnimation();
                    imageView.setVisibility(GONE);
                    progressBar.setVisibility(VISIBLE);
                    smoothToScroll(getChildAt(0).getHeight());
                    i = getChildAt(0).getHeight();
                    if (onRefreshListener != null) {
                        if (!isRefresh) {

                            lastDate = currentDate;
                            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                            currentDate = formatter.format(curDate);

                            onRefreshListener.onRefresh();
                        }
                    }
                    isRefresh = true;
                } else {

                    if (!isRefresh) {
                        endRefresh();
                    }

                }
                break;
        }
        yLast = y;
        return true;
    }

    private void startRadius(float from, float to) {

        //Log.e("startRadius", "startRadius");
        RotateAnimation rotateAnimation = new RotateAnimation(from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
        imageView.startAnimation(rotateAnimation);
    }

    public void endRefresh() {
        progressBar.setVisibility(GONE);
        imageView.setVisibility(VISIBLE);
        isNeedUp = true;
        isNeedDown = true;
        isRefresh = false;
        smoothToScroll(-getChildAt(0).getHeight());
        i = 0;
    }

    private void addHeadRefresh() {
        setOrientation(LinearLayout.VERTICAL);
        View inflate = inflate(getContext(), R.layout.pull_to_refresh, null);
        imageView = inflate.findViewById(R.id.arrow);
        tvDate = inflate.findViewById(R.id.updated_at);
        progressBar = inflate.findViewById(R.id.progress_bar);
        addView(inflate, 0);

        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        currentDate = formatter.format(curDate);
        lastDate = currentDate;
        tvDate.setText("上次更新" + currentDate);


    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}

