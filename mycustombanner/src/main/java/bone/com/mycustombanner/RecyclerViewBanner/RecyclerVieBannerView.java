package bone.com.mycustombanner.RecyclerViewBanner;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import bone.com.mycustombanner.R;
import bone.com.mycustombanner.ViewPagerBanner.NavigatorLocationConstant;
import bone.com.mycustombanner.itemtype.BaseItemType;
import bone.com.mycustombanner.navigatorview.NavigatorView;
import bone.com.mycustombanner.utils.LogUtils;

/**
 * 功能：
 * ＊创建者：赵然 on 2017/10/23 14:15
 * ＊
 */

public class RecyclerVieBannerView extends ConstraintLayout {
    private Context context;
    private RecyclerView pagerView;
    private RecyclerViewPagerAdatper adapter;

    private ConstraintSet bannerSet;
    //数据改变时的回调
    private RecyclerView.AdapterDataObserver pagerDataSetOberver;
    /**
     * 是否需要导航点
     */
    private boolean isNeedNavigator = false;
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
     * 当前页
     */
    private int currentPage = 0;


    private LinearLayoutManager layoutManager;

    private Handler autoHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (adapter.getData().size() > 1) {
                pagerView.smoothScrollToPosition(++currentPage);
                //smoothScrollToPosition 方法不触发
                LogUtils.Zlog().e("real position:" + adapter.getRealPosition(currentPage)+" pager Height:"+pagerView.getHeight());
                navigatorLay.updatePointsSelectedStatus(adapter.getRealPosition(currentPage));
                if (pageChangeListener != null) {
                    pageChangeListener.onPageSelected(adapter.getRealPosition(currentPage));
                }
            } else {
                stopAutoPlay();
            }
        }
    };


    public RecyclerVieBannerView(Context context) {
        super(context);
        init(context);
    }

    public RecyclerVieBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

        init(context);
    }

    public RecyclerVieBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        pagerDataSetOberver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                int count = adapter.getData().size();
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
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        pagerView = new RecyclerView(context);
        pagerView.setId(R.id.selfbanner_viewpagerids);
        pagerView.setBackgroundColor(Color.GRAY);
        pagerView.setLayoutManager(layoutManager);

        SnapHelper helper = new PagerSnapHelper() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {

                //注意： 手动滑动revyclerview 时触发   smoothScrollToPosition(） 方法不触发此方法


                int itemCount = adapter.getItemCount();
                int position = findTargetSnapPosition(pagerView.getLayoutManager(), velocityX, velocityY);
                //最后一条时如果继续滑动会发现position还可以+1
                //系统PagerSnapHelper的findTargetSnapPosition 方法有bug 拷贝出来一份改牵连类过多
                //故此处做类布丁操作  使用v7包为 com.android.support:appcompat-v7:26.+ 后续依赖包修复后可以删除此行
                //注意 并非一定是itemcount  无限轮播时 itmecount可能时自定义的 比如是：Integer.MAX_VALUE
                position = position < itemCount ? position : itemCount - 1;
                LogUtils.Zlog().d("velocityX:" + adapter.getRealPosition(position) + " position:" + position + " itemCount:" + itemCount);

                if (isNeedNavigator) {

                    navigatorLay.updatePointsSelectedStatus(adapter.getRealPosition(position));
                    LogUtils.Zlog().e("real position:" + adapter.getRealPosition(position));
                }
                if (pageChangeListener != null) {
                    pageChangeListener.onPageSelected(position);
                }
                currentPage = position;
                return super.onFling(velocityX, velocityY);

            }
        };
        helper.attachToRecyclerView(pagerView);
        adapter = new RecyclerViewPagerAdatper(getContext());
        pagerView.setAdapter(adapter);
        setPageTransformer(RecyclerViewPagerAdatper.SCALEIN);

        //设置pager在容器中的位置约束
        bannerSet.constrainWidth(pagerView.getId(), ConstraintSet.MATCH_CONSTRAINT);
        bannerSet.constrainHeight(pagerView.getId(), ConstraintSet.MATCH_CONSTRAINT);
        bannerSet.connect(pagerView.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT);
        bannerSet.connect(pagerView.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP);
        bannerSet.connect(pagerView.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT);
        bannerSet.connect(pagerView.getId(),ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM);

        addView(pagerView);

        navigatorLay = new NavigatorView(getContext());
        navigatorLay.setId(R.id.selfbanner_navigatorids);

        bannerSet.constrainWidth(navigatorLay.getId(), ConstraintSet.WRAP_CONTENT);
        bannerSet.constrainHeight(navigatorLay.getId(), ConstraintSet.WRAP_CONTENT);
        navigatorLay.setVisibility(GONE);

        addView(navigatorLay);
        initNavigator();
        //  限制完约束后设置给容器
        bannerSet.applyTo(this);
        adapter.registerAdapterDataObserver(pagerDataSetOberver);

    }


    public void setNeedNavigator(boolean needNavigator) {
        isNeedNavigator = needNavigator;
        initNavigator();
    }

    /**
     * 初始化导航点
     */
    private void initNavigator() {
        if (isNeedNavigator) {
            navigatorLay.setVisibility(VISIBLE);
        } else {
            navigatorLay.setVisibility(GONE);
        }

    }

    public void addItemType(BaseItemType itemType) {
        adapter.addItemTypes(itemType);
    }

    public RecyclerViewPagerAdatper getAdapter() {
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
            if (currentPage == 0) {
                currentPage = adapter.getStartPosition();
                layoutManager.scrollToPosition(currentPage);
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

    /**
     * 设置导航点的位置
     */
    public void setNavigatorParams(int leftMargin, int topMargin, int rightMargin, int bottomMargin, int location) {

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


    public RecyclerVieBannerView setPageTransformer(@RecyclerViewPagerAdatper.AnimationType int animationType) {
        adapter.openLoadAnimation(animationType);
        adapter.isFirstOnly(false);
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

    public void setEndLoop(boolean endLoop) {
        adapter.setEndLoop(endLoop);
    }
}
