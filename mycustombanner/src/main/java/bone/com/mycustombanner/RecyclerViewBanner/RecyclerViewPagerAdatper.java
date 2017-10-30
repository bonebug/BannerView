package bone.com.mycustombanner.RecyclerViewBanner;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import bone.com.mycustombanner.RecyclerViewBanner.adapter.BaseViewHolder;
import bone.com.mycustombanner.animation.AlphaInAnimation;
import bone.com.mycustombanner.animation.BaseAnimation;
import bone.com.mycustombanner.animation.ScaleInAnimation;
import bone.com.mycustombanner.animation.SlideInBottomAnimation;
import bone.com.mycustombanner.animation.SlideInLeftAnimation;
import bone.com.mycustombanner.animation.SlideInRightAnimation;
import bone.com.mycustombanner.entity.ItemTypeBeanInterface;
import bone.com.mycustombanner.itemtype.BaseItemType;

/**
 * 功能：
 * ＊创建者：赵然 on 2017/10/26 10:28
 * ＊
 */


public class RecyclerViewPagerAdatper<T> extends RecyclerView.Adapter<BaseViewHolder> {
//Animation
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int ALPHAIN = 0x00000001;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SCALEIN = 0x00000002;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_BOTTOM = 0x00000003;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_LEFT = 0x00000004;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_RIGHT = 0x00000005;
    @IntDef({ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }

    private List<T> datas;

    private Context context;

    private SparseArray<BaseItemType> itemTypeMap;
    /**
     * 是否无限循环
     */
    private boolean isEndLoop = true;
    /**
     * 当无限循环时  item的最大值
     */
    private int maxItemCount = Integer.MAX_VALUE;

    private boolean mFirstOnlyEnable = false;
    private boolean mOpenAnimationEnable = true;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mDuration = 300;

    private BaseAnimation mCustomAnimation;
    private BaseAnimation mSelectAnimation = new AlphaInAnimation();
    private int mLastPosition = -1;


    public RecyclerViewPagerAdatper(Context context) {
        this.context = context;
        itemTypeMap = new SparseArray<>();
        datas = new ArrayList<>();
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int index = getRealPosition(viewType);
        Log.d("adapter", "position:" + viewType + " index:" + index);
        ItemTypeBeanInterface t = (ItemTypeBeanInterface) datas.get(index);

        if (itemTypeMap.get(t.getItemType()) != null) {
            BaseItemType itemType = itemTypeMap.get(t.getItemType());
            View itemView = LayoutInflater.from(context).inflate(itemType.getItemViewLay(), null);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
            itemView.setLayoutParams(params);

            return new BaseViewHolder(itemView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
//        holder.loadImage(context, datas.get(position).getUrl(), R.id.iv_item_pic);
        int index = getRealPosition(position);
        Log.d("adapter", "position:" + position + " index:" + index);
        ItemTypeBeanInterface t = (ItemTypeBeanInterface) datas.get(index);

        if (itemTypeMap.get(t.getItemType()) != null) {
            BaseItemType itemType = itemTypeMap.get(t.getItemType());

            itemType.instantiateItem(holder, datas.get(index));

        }
    }

    @Override
    public int getItemCount() {
        int loopItemCount = datas.size() == 0 ? 0 : maxItemCount;
        return isEndLoop ? loopItemCount : datas.size();
    }


    public void addItemTypes(BaseItemType<ItemTypeBeanInterface> itemType) {
        itemTypeMap.put(itemType.getItemType(), itemType);

    }

    public List<T> getData() {

        return datas;
    }

    public void setNewData(List<T> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();

    }


    public boolean isEndLoop() {
        return isEndLoop;
    }

    public void setEndLoop(boolean endLoop) {
        isEndLoop = endLoop;
    }

    /**
     * 获取UI展示item对应角标位置 需 position > 0
     *
     * @param position
     * @return
     */
    public int getRealPosition(int position) {

        if (datas.size() == 0) return 0;

        return isEndLoop ? position % datas.size() : position;

    }

    public int getStartPosition() {
        int startPosition = 0;

        if (getItemCount() == maxItemCount) {
            startPosition = maxItemCount / 2 - ((maxItemCount / 2) % datas.size());
        }
        return startPosition;
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        return position;
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        addAnimation(holder);
    }
    /**
     * {@link #addAnimation(RecyclerView.ViewHolder)}
     *
     * @param firstOnly true just show anim when first loading false show anim when load the data every time
     */
    public void isFirstOnly(boolean firstOnly) {
        this.mFirstOnlyEnable = firstOnly;
    }
    /**
     * To open the animation when loading
     */
    public void openLoadAnimation() {
        this.mOpenAnimationEnable = true;
    }
    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (mOpenAnimationEnable) {
            if (!mFirstOnlyEnable || holder.getLayoutPosition() > mLastPosition) {
                BaseAnimation animation = null;
                if (mCustomAnimation != null) {
                    animation = mCustomAnimation;
                } else {
                    animation = mSelectAnimation;
                }
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    startAnim(anim, holder.getLayoutPosition());
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }
    /**
     * set anim to start when loading
     *
     * @param anim
     * @param index
     */
    protected void startAnim(Animator anim, int index) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
    }
    /**
     * Set the view animation type.
     *
     * @param animationType One of {@link #ALPHAIN}, {@link #SCALEIN}, {@link #SLIDEIN_BOTTOM},
     *                      {@link #SLIDEIN_LEFT}, {@link #SLIDEIN_RIGHT}.
     */
    public void openLoadAnimation(@AnimationType int animationType) {
        this.mOpenAnimationEnable = true;
        mCustomAnimation = null;
        switch (animationType) {
            case ALPHAIN:
                mSelectAnimation = new AlphaInAnimation();
                break;
            case SCALEIN:
                mSelectAnimation = new ScaleInAnimation();
                break;
            case SLIDEIN_BOTTOM:
                mSelectAnimation = new SlideInBottomAnimation();
                break;
            case SLIDEIN_LEFT:
                mSelectAnimation = new SlideInLeftAnimation();
                break;
            case SLIDEIN_RIGHT:
                mSelectAnimation = new SlideInRightAnimation();
                break;
            default:
                break;
        }
    }


}
