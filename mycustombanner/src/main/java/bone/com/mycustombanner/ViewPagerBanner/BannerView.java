package bone.com.mycustombanner.ViewPagerBanner;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import bone.com.mycustombanner.R;
import bone.com.mycustombanner.itemtype.BaseItemType;
import bone.com.mycustombanner.navigatorview.NavigatorLocationConstant;
import bone.com.mycustombanner.navigatorview.NavigatorView;

/**
 * 功能：轮播控件
 * 注意：控件不可见时会自动停止轮播  可见后会恢复轮播--故界面中无需控制
 * <p>
 * <p>
 * ＊创建者：赵然 on 2017/10/9 13:53
 * ＊
 */

public class BannerView extends ConstraintLayout {
    private Context context;

    private ViewPager pager;
    private BannerViewPagerAdapter adapter;

    private ConstraintSet bannerSet;
    //viewpager数据改变时的回调
    private DataSetObserver pagerDataSetOberver;
    /**
     * 是否需要导航点
     */
    private boolean isNeedNavigator = true;
    /**
     * 导航点的布局
     */
    private NavigatorView navigatorLay;

    /**
     * 自动轮播的时间间隔
     */
    private long delay = 3000L;
    /**
     * 自动轮播的计时器
     */
    private Timer timer;
    /**
     * 是否自动轮播
     */
    private boolean isAutoPlay;
    /**
     * 是否在自动轮播
     */
    private boolean isPlaying;
    private boolean isWindowGoneStop;
    private boolean isAttachedToWindow;
    /**
     * 用于外部回调 页面切换的viewpager的回调
     */
    private ViewPager.OnPageChangeListener pageChangeListener;
    /**
     * 导航点的位置间距
     */
    private int leftMargin, topMargin, rightMargin, bottomMargin;
    /**
     * 导航点的位置
     */
    private int location;

    private Handler autoHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (adapter.getDatas().size() == 0) {
                stopAutoPlay();
            } else {
                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            }
        }
    };

    public BannerView(Context context) {
        super(context);
        init(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

        init(context);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化操作
     */
    private void init(Context context) {
        this.context = context;
        initParams();

        initView();
    }

    /**
     * 所有的对象，控件参数的初始化
     */
    private void initParams() {
        /**
         * viewpager 适配器 数据源变更时的回调 ---- 导航点需要更新
         */
        pagerDataSetOberver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                int count = adapter.getDatas().size();
                navigatorLay.setPointsCount(count);
            }
        };

        bannerSet = new ConstraintSet();
        bannerSet.clone(this);


    }

    /**
     * view的初始化
     */
    private void initView() {
        adapter = new BannerViewPagerAdapter(context);
        pager = new ViewPager(context);

        pager.setId(R.id.selfbanner_viewpagerids);
        pager.setBackgroundColor(Color.TRANSPARENT);
        addView(pager);


        //设置pager在容器中的位置约束
        bannerSet.constrainWidth(pager.getId(), ConstraintSet.MATCH_CONSTRAINT);
        bannerSet.constrainHeight(pager.getId(), ConstraintSet.WRAP_CONTENT);
        //todo 设置navigator 在容器中的位置约束

        //  限制完约束后设置给容器
        bannerSet.applyTo(this);

        initViewPager(context);
        adapter.registerDataSetObserver(pagerDataSetOberver);
        navigatorLay = new NavigatorView(getContext());
        navigatorLay.setId(R.id.selfbanner_navigatorids);

        bannerSet.constrainWidth(navigatorLay.getId(), ConstraintSet.WRAP_CONTENT);
        bannerSet.constrainHeight(navigatorLay.getId(), ConstraintSet.WRAP_CONTENT);

//            setNavigatorParams(0, DensityUtil.dip2px(context, 10), 0, 0, NavigatorLocationConstant.TOP_CENTER);

        initNavigator();

    }

    /**
     * 初始化导航点
     */
    private void initNavigator() {
        if (isNeedNavigator) {
            addView(navigatorLay);
        } else {
            removeView(navigatorLay);
        }

    }

    /**
     * 初始化轮播banner
     */
    private void initViewPager(Context context) {

        pager.setAdapter(adapter);
        ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (pageChangeListener != null) {
                    pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (isNeedNavigator) {

                    navigatorLay.updatePointsSelectedStatus(adapter.getRealPosition(position));
                }
                Log.d("TAG", "POSTION:" + position);
                if (pageChangeListener != null) {
                    pageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (pageChangeListener != null) {
                    pageChangeListener.onPageScrollStateChanged(state);
                }

            }
        };
        pager.addOnPageChangeListener(listener);

        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            BannerScroller mScroller = new BannerScroller(getContext());
            mField.set(pager, mScroller);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }

    }

    public void addItemType(BaseItemType itemType) {
        adapter.addItemType(itemType);
    }

    public BannerViewPagerAdapter getAdapter() {
        return adapter;
    }

    /**
     * 是否自动轮播
     *
     * @return
     */
    public boolean isAutoPlay() {
        return isAutoPlay;
    }

    /**
     * 设置自动播
     *
     * @param autoPlay
     */
    public void setAutoPlay(boolean autoPlay) {
        boolean isChange = isAutoPlay == autoPlay;
        isAutoPlay = autoPlay;
        if (isChange) {

            if (isAutoPlay) {
//不处理 用户自己调用开启播
            } else {
                //停止轮播
                stopAutoPlay();

            }
        }
    }

    /**
     * 开启自动轮播
     */
    public void startAutoPlay() {
        if (isAutoPlay && !isPlaying) {
            isPlaying = true;
            if (pager.getCurrentItem() == 0) {

                pager.setCurrentItem(adapter.getStartPosition());
            }
            if (timer == null) timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    autoHandler.sendEmptyMessage(0);

                }
            }, delay, delay);

        }
    }

    /**
     * 关闭自动轮播
     */
    public void stopAutoPlay() {
        isPlaying = false;
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    public void setEndLoop(boolean isEndLoop) {
        adapter.setEndLoop(isEndLoop);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.i(tag, ev.getAction() + "--" + isAutoPlay);
        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setNeedNavigator(boolean needNavigator) {
        if (isNeedNavigator != needNavigator){

            isNeedNavigator = needNavigator;
            initNavigator();
            updateNavigatorLocation();

        }


    }

    /**
     * 设置导航点的位置
     */
    public void setNavigatorParams(int leftMargin, int topMargin, int rightMargin, int bottomMargin, int location) {

        this.leftMargin = leftMargin;
        this.topMargin = topMargin;
        this.bottomMargin = bottomMargin;
        this.rightMargin  = rightMargin;
        this.location = location;
        updateNavigatorLocation();

    }

    private void updateNavigatorLocation(){

        bannerSet.clear(navigatorLay.getId());
        bannerSet.constrainWidth(navigatorLay.getId(), ConstraintSet.WRAP_CONTENT);
        bannerSet.constrainHeight(navigatorLay.getId(), ConstraintSet.WRAP_CONTENT);
        switch (location) {
            case NavigatorLocationConstant.TOP_LEFT:
                bannerSet.connect(navigatorLay.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
                bannerSet.connect(navigatorLay.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);

                break;
            case NavigatorLocationConstant.TOP_CENTER:
                bannerSet.connect(navigatorLay.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
                bannerSet.connect(navigatorLay.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
                bannerSet.connect(navigatorLay.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);

                break;
            case NavigatorLocationConstant.TOP_RIGHT:
                bannerSet.connect(navigatorLay.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
                bannerSet.connect(navigatorLay.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                break;
            case NavigatorLocationConstant.BOTTOM_LEFT:
                bannerSet.connect(navigatorLay.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
                bannerSet.connect(navigatorLay.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                break;
            case NavigatorLocationConstant.BOTTOM_CENTER:
                bannerSet.connect(navigatorLay.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
                bannerSet.connect(navigatorLay.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
                bannerSet.connect(navigatorLay.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                break;
            case NavigatorLocationConstant.BOTTOM_RIGHT:
                bannerSet.connect(navigatorLay.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
                bannerSet.connect(navigatorLay.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                break;

        }

        bannerSet.setMargin(navigatorLay.getId(), 1, leftMargin);
        bannerSet.setMargin(navigatorLay.getId(), 2, rightMargin);
        bannerSet.setMargin(navigatorLay.getId(), 3, topMargin);
        bannerSet.setMargin(navigatorLay.getId(), 4, bottomMargin);
        bannerSet.setMargin(navigatorLay.getId(), 6, leftMargin);
        bannerSet.setMargin(navigatorLay.getId(), 7, rightMargin);

        bannerSet.applyTo(this);
    }

    /**
     * 设置导航点的背景图
     *
     * @param selectedColorID
     * @param unSelectedColorID
     */

    public void setNavigatorPointsBackgroundColors(@ColorRes int selectedColorID, @ColorRes int unSelectedColorID) {
        navigatorLay.setPointsBackgroundColors(selectedColorID, unSelectedColorID);
    }

    /**
     * 设置导航点的背景图
     *
     * @param selectedDrawableID
     * @param unSelectedDrawableID
     */
    public void setNavigatorPointsBackgroundDrawable(@DrawableRes int selectedDrawableID, @DrawableRes int unSelectedDrawableID) {
        navigatorLay.setPointsBackgroundDrawable(selectedDrawableID, unSelectedDrawableID);
    }

    /**
     * 设置导航点的间隙  两点之间的距离
     *
     * @param gaps 单位PX
     */
    public void setNavigatorPointsGaps(int gaps) {
        navigatorLay.setGap(gaps);

    }

    /**
     * 导航点的半径
     */
    public void setNavigatorPointsRadius(int radius) {
        navigatorLay.setPointsRadius(radius);
    }


    /**
     * Set the number of pages that should be retained to either side of the
     * current page in the view hierarchy in an idle state. Pages beyond this
     * limit will be recreated from the adapter when needed.
     *
     * @param limit How many pages will be kept offscreen in an idle state.
     * @return Banner
     */
    public BannerView setOffscreenPageLimit(int limit) {
        if (pager != null) {
            pager.setOffscreenPageLimit(limit);
        }
        return this;
    }

    /**
     * Set a {@link ViewPager.PageTransformer} that will be called for each attached page whenever
     * the scroll position is changed. This allows the application to apply custom property
     * transformations to each page, overriding the default sliding look and feel.
     *
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     *                            to be drawn from last to first instead of first to last.
     * @param transformer         PageTransformer that will modify each page's animation properties
     * @return Banner
     */
    public BannerView setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        pager.setPageTransformer(reverseDrawingOrder, transformer);
        return this;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
//        if (!isPlaying || !isAttachedToWindow) return ;
        if (View.VISIBLE == visibility && isAttachedToWindow && isWindowGoneStop) {
            startAutoPlay();
            isWindowGoneStop = false;
        } else if (View.GONE == visibility && isPlaying && isAttachedToWindow) {
            isWindowGoneStop = true;
            stopAutoPlay();
        }
    }

    public ViewPager.OnPageChangeListener getPageChangeListener() {
        return pageChangeListener;
    }

    public void setPageChangeListener(ViewPager.OnPageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }


}



