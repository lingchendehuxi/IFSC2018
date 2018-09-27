package com.android.incongress.cd.conference.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Scroller;

import com.android.incongress.cd.conference.utils.DensityUtil;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Jacky on 2016/3/13.
 */
public class RefreshLayout extends ViewGroup {

    private static final boolean DEFAULT_CAN_REFRESH = true;
    private static final boolean DEFAULT_CAN_LOAD = true;

    private boolean mIsCanRefresh = DEFAULT_CAN_REFRESH;
    private boolean mIsCanLoad = DEFAULT_CAN_LOAD;

    //下拉偏移
    private int mHeaderOffY;
    //上拉偏移
    private int mFooterOffY;
    //内容偏移
    private int mContentOffY;
    //最后一次触摸的位置
    private float mLastY;

    //  通过触摸判断滑动方向
    private static byte NO_SCROLL = 0;
    private static byte NO_SCROLL_UP = 1;
    private static byte NO_SCROLL_DOWN = 2;

    private int mDuration = 500;

    //摩擦系数
    private float mFriction = 0.9f;

    //是否处于刷新状态或者加载更多状态
    private boolean mIsInRefreshMode = false;
    private boolean mIsInLoadMode = false;

    //当前滑动方向
    private int mCurrentScrollDirection = NO_SCROLL;

    //判断X轴方向
    private float mDirectionX;
    //判断轴方向
    private float mDirectionY;

    //偏移
    private int mCurrentOffsetY;
    //触摸移动的位置
    private int mOffsetSum;
    //触摸移动的位置之和
    private int mScrollSum;

    //中间的内容布局
    private View mContentView;
    //下拉刷新布局
    private RefreshView mHeaderView;
    //上拉加载更多布局
    private RefreshView mFooterView;

    //头部高度
    protected int mHeaderViewHeight;
    //底部高度
    protected int mFooterViewHeight;

    //   下拉监听
    protected OnRefreshListener mOnRefreshListener;
    //    上拉监听
    protected OnLoadMoreListener mOnLoadMoreListener;


    private Scroller mScroller = new Scroller(getContext());

    public RefreshLayout(Context context) {
        this(context,null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context,attrs, 0);
    }

    @SuppressWarnings("ResourceType")
    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout,defStyleAttr, 0);

        try {
            mIsCanRefresh = a.getBoolean(0,DEFAULT_CAN_REFRESH);
            //下面这个a.getBoolean(1,xxx)会在编译打包时报错，所以需要使用上面的注解来注释掉这个错误，本身没有问题
            mIsCanLoad = a.getBoolean(1, DEFAULT_CAN_LOAD);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            a.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        setupViews();
    }

    /**
     * 初始化上拉刷新和下拉加载更多布局
     */
    private void setupViews() {
        ViewGroup.LayoutParams lp;
        if(mIsCanRefresh) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getContext(),50f));
            mHeaderView = new RefreshView(getContext());
            addView(mHeaderView, lp);
            mHeaderView.setIsHeaderOrFooter(true);
        }

        if(mIsCanLoad) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getContext(),50f));
            mFooterView = new RefreshView(getContext());
            addView(mFooterView, lp);
            mFooterView.setIsHeaderOrFooter(false);
        }
    }

    //设置view的位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        childLayout();
    }

    /**
     * 布置子空间的位置
     */
    private void childLayout() {
        int  paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        if(mHeaderView != null) {
            MarginLayoutParams mlp = (MarginLayoutParams) mHeaderView.getLayoutParams();
            int left = paddingLeft + mlp.leftMargin;
            int top = paddingTop + mlp.topMargin - mHeaderViewHeight + mHeaderOffY;
            int right = left + mHeaderView.getMeasuredWidth();
            int bottom = top + mHeaderView.getMeasuredHeight();

            mHeaderView.layout(left,top,right,bottom);
        }

        if(mFooterView != null) {
            MarginLayoutParams mlp = (MarginLayoutParams) mFooterView.getLayoutParams();
            int left = paddingLeft + mlp.leftMargin;
            int top = paddingTop + getMeasuredHeight() + mlp.topMargin - mFooterOffY;
            int right = left + mFooterView.getMeasuredWidth();
            int bottom = top + mFooterView.getMeasuredHeight();

            mFooterView.layout(left, top, right, bottom);
        }

        if(mContentView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = paddingTop + lp.topMargin + mContentOffY;
            final int right = left + mContentView.getMeasuredWidth();
            final int bottom = top + mContentView.getMeasuredHeight();

            mContentView.layout(left, top, right, bottom);
        }
    }

    /**
     * TODO getMeasuredHeight()和getHeight()有什么区别
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(mHeaderView != null) {
            measureChildWithMargins(mHeaderView,widthMeasureSpec,0, heightMeasureSpec, 0);

            MarginLayoutParams mlp = (MarginLayoutParams) mHeaderView.getLayoutParams();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight() + mlp.topMargin + mlp.bottomMargin;
        }

        if(mFooterView != null) {
            measureChildWithMargins(mFooterView,widthMeasureSpec,0, heightMeasureSpec, 0);

            MarginLayoutParams mlp = (MarginLayoutParams) mFooterView.getLayoutParams();
            mFooterViewHeight = mFooterView.getMeasuredHeight() +  mlp.topMargin + mlp.bottomMargin;
        }

        if(mContentView != null) {
            measureChildWithMargins(mContentView, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDirectionX = ev.getX();
                mDirectionY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                if(mDirectionY <=0 || mDirectionX <=0)
                    break;

                float eventY = ev.getY();
                float eventX = ev.getX();

                //获得y轴和x轴的偏移量
                float offY = eventY - mDirectionY;
                float offX = eventX - mDirectionX;

                mDirectionY = eventY;
                mDirectionX = eventX;

                boolean moved = Math.abs(offY) > Math.abs(offX);

                if(offY>0 && moved && canRefresh()) {
                    mCurrentScrollDirection = NO_SCROLL_UP;
                }else if(offY<0 && moved && canLoadMore()) {
                    mCurrentScrollDirection = NO_SCROLL_DOWN;
                }else {
                    mCurrentScrollDirection = NO_SCROLL;
                }

                if(mCurrentScrollDirection == NO_SCROLL_DOWN || mCurrentScrollDirection == NO_SCROLL_UP)
                    return true;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //不可滑动的View
        if(!canChildScrollDown() && !canChildScrollUp()) {
            if(mCurrentScrollDirection == NO_SCROLL_UP) {
                if(canRefresh()) {
                    return touch(event,true);
                }
            }else if(mCurrentScrollDirection == NO_SCROLL_DOWN) {
                if(canLoadMore()) {
                    return touch(event,false);
                }
            }else {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDirectionY = event.getY();
                        mDirectionX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mDirectionY <= 0 || mDirectionX <= 0) {
                            break;
                        }

                        float eventY = event.getY();
                        float eventX = event.getX();

                        float offY = eventY - mDirectionY;
                        float offX = eventX - mDirectionX;

                        mDirectionY = eventY;
                        mDirectionX = eventX;

                        boolean moved = Math.abs(offY) > Math.abs(offX);


                        if (offY > 0 && moved && canRefresh() ) {
                            mCurrentScrollDirection = NO_SCROLL_UP;
                        } else if (offY < 0 && moved && canLoadMore()) {
                            mCurrentScrollDirection = NO_SCROLL_DOWN;
                        } else {
                            mCurrentScrollDirection = NO_SCROLL;
                        }
                        break;
                }
                return true;
            }
        }else {
          //可滑动view
            if(mCurrentScrollDirection == NO_SCROLL_UP && canRefresh()) {
                return touch(event,true);
            }else if(mCurrentScrollDirection == NO_SCROLL_DOWN && canLoadMore()) {
                return touch(event,false);
            } else {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDirectionY = event.getY();
                        mDirectionX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mDirectionY <= 0 || mDirectionX <= 0) {
                            break;
                        }

                        float eventY = event.getY();
                        float eventX = event.getX();

                        float offY = eventY - mDirectionY;
                        float offX = eventX - mDirectionX;

                        mDirectionY = eventY;
                        mDirectionX = eventX;

                        boolean moved = Math.abs(offY) > Math.abs(offX);


                        if (offY > 0 && moved && canRefresh() ) {
                            mCurrentScrollDirection = NO_SCROLL_UP;
                        } else if (offY < 0 && moved && canLoadMore()) {
                            mCurrentScrollDirection = NO_SCROLL_DOWN;
                        } else {
                            mCurrentScrollDirection = NO_SCROLL;
                        }
                        break;
                }
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 是否能上拉
     *
     * @return
     */
    protected boolean canChildScrollDown() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mContentView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mContentView;
                return absListView.getChildCount() > 0 && (absListView.getLastVisiblePosition() < absListView.getChildCount() - 1
                        || absListView.getChildAt(absListView.getChildCount() - 1).getBottom() > absListView.getPaddingBottom());
            } else {
                return ViewCompat.canScrollVertically(mContentView, 1) || mContentView.getScrollY() < 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mContentView, 1);
        }
    }

    /**
     * 是否能下拉
     *
     * @return
     */
    protected boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mContentView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mContentView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mContentView, -1) || mContentView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mContentView, -1);
        }
    }


    /**
     * 能否刷新
     *
     * @return
     */
    private boolean canRefresh() {
        return mIsCanRefresh && mHeaderView != null && !canChildScrollUp() && !mIsInRefreshMode && !mIsInLoadMode ;
    }

    /**
     * 能否加载更多
     *
     * @return
     */
    private boolean canLoadMore() {
        return mIsCanLoad && mFooterView != null && !canChildScrollDown() && !mIsInLoadMode && !mIsInRefreshMode;
    }

    private boolean touch(MotionEvent e, boolean isHead) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = e.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                if(mLastY > 0) {
                    mCurrentOffsetY = (int)(e.getY() - mLastY);
                    mOffsetSum += mCurrentOffsetY;
                }
                mLastY = e.getY();

                boolean isCanMove;

                if(isHead) {
                    isCanMove = mOffsetSum > 0;
                }else {
                    isCanMove = mOffsetSum < 0;
                }

                if(isCanMove) {
                   float ratio = getRatio();

                    if(ratio <0)
                        ratio = 0;

                    int scrollNum = -((int)(mCurrentOffsetY*ratio));
                    mScrollSum += scrollNum;


                    if(isHead) {
                        if(!mIsInRefreshMode) {
                            smoothMove(true,true, scrollNum, mScrollSum);
                            if(Math.abs(mScrollSum) > mHeaderViewHeight) {
                                mHeaderView.onPrepare();
                            }

                            mHeaderView.onPositionChange(Math.abs(mScrollSum)/mHeaderViewHeight);
                        }
                    }else {
                        if(!mIsInLoadMode) {
                            smoothMove(false, true, scrollNum, mScrollSum);


                            if (Math.abs(mScrollSum) > mFooterViewHeight) {

                                mFooterView.onPrepare();
                            }
                            mFooterView.onPositionChange(Math.abs(mScrollSum) / (float) mFooterViewHeight);
                        }
                    }
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                if(Math.abs(mScrollSum) > 3) {
                    if (isHead ) {
                        if(!mIsInRefreshMode) {
                            if (Math.abs(mScrollSum) > mHeaderViewHeight) {
                                smoothMove(true, false, -mHeaderViewHeight, mHeaderViewHeight);
                                mHeaderView.onRelease();
                                refreshing();
                            } else {
                                //松开手指回弹
                                smoothMove(true, false, 0, 0);
                            }
                        }
                    } else {
                        if(!mIsInLoadMode) {
                            if (Math.abs(mScrollSum) > mFooterViewHeight) {
                                smoothMove(false, false, mContentView.getMeasuredHeight() - getMeasuredHeight() + mFooterViewHeight, mFooterViewHeight);
                                mFooterView.onRelease();
                                loadingMore();
                            } else {
                                //松开手指回弹
                                smoothMove(false, false, mContentView.getMeasuredHeight() - getMeasuredHeight(), 0);
                            }
                        }
                    }

                }
                resetParameter();
                break;
        }

        return super.onTouchEvent(e);
    }

    private void resetParameter() {
        mDirectionX = 0;
        mDirectionY = 0;
        mCurrentScrollDirection = NO_SCROLL;
        mLastY = 0;
        mOffsetSum = 0;
        mScrollSum = 0;
    }

    private void smoothMove(boolean isHeader, boolean isMove, int moveScrollY, int moveY) {
        moveY = Math.abs(moveY);

        if(isHeader) {
            if (isMove) {
                smoothScrollBy(0,moveScrollY);
            }else {
                smoothScrollTo(0,moveScrollY);
            }
        }else {
            if (isMove) {
                smoothScrollBy(0,moveScrollY);
            }else {
                smoothScrollTo(0,moveScrollY);
            }
        }
    }

    /**
     * 设置滚动的相对偏移
     * @param dx
     * @param dy
     */
    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, mDuration);
        invalidate();
    }

    /**
     * 调用此方法滚动到目标位置
     *
     * @param fx
     * @param fy
     */
    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    /**
     * 滑动距离越大比率越小，越难拖动
     *
     * @return
     */
    private float getRatio() {
        return 1 - (Math.abs(mOffsetSum) / (float) getMeasuredHeight()) - 0.3f * mFriction;

    }

    /**
     * 刷新
     */
    private void refreshing() {
        if (mOnRefreshListener != null) {
            mIsInRefreshMode = true;
            mOnRefreshListener.onRefresh();
        }

    }

    /**
     * 加载更多
     */
    private void loadingMore() {
        if (mOnLoadMoreListener != null) {
            mIsInLoadMode = true;
            mOnLoadMoreListener.onLoadMore();
        }
    }


    public void setmIsCanLoad(boolean mIsCanLoad) {
        this.mIsCanLoad = mIsCanLoad;
    }
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }

        super.computeScroll();
    }


    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p != null && p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        @SuppressWarnings({"unused"})
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    /**
     * 刷新完成
     */
    public void refreshComplete() {

        postDelayed(new Runnable() {
            @Override
            public void run() {
                smoothMove(true, false, 0, 0);
                mHeaderView.onComplete();
                mHeaderView.onReset();
                mIsInRefreshMode = false;
            }
        }, mDuration);


    }

    /**
     * 加载更多完成
     */
    public void loadMoreComplete() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                smoothMove(false, false, mContentView.getMeasuredHeight() - getMeasuredHeight(), 0);
                mFooterView.onComplete();
                mFooterView.onReset();
                mIsInLoadMode = false;
            }
        }, mDuration);

    }

    /**
     * 设置刷新监听
     *
     * @param mOnRefreshListener
     */
    public void setOnRefreshListener(@NonNull OnRefreshListener mOnRefreshListener) {
        this.mOnRefreshListener = mOnRefreshListener;
    }


    /**
     * 设置加载更多监听
     *
     * @param mOnLoadMoreListener
     */
    public void setOnLoadMoreListener(@NonNull OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    /**
     * 结束当前的刷新或者加载更多
     */
    public void finishCurrentLoad(){
        if(mIsInRefreshMode) {
            this.refreshComplete();
        }

        if(mIsInLoadMode) {
            this.loadMoreComplete();
        }
    }
}
