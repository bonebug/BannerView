package bone.com.mycustombanner.navigatorview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import bone.com.mycustombanner.utils.DensityUtil;

/**
 * 功能： 导航点view
 * ＊创建者：赵然 on 2017/10/9 20:25
 * ＊
 */

public class NavigatorView extends LinearLayout {
    private String TAG = NavigatorView.class.getSimpleName().toString();

    private int selectedDrawableID;
    private int unSelectedDrawableID;

    private int selectedColorID = Color.WHITE;
    private int unSelectedColorID = Color.GRAY;
    /**
     * 是否使用图片背景
     */
    private boolean isDrawableBG;
    /**
     * 由于点数量一般改变频率不大 故此处做只增不减 view销毁时释放
     */
    private List<View> points;

    private int lastSelectedPosition;

    /**
     * 点的宽度 半径
     */
    private int pointsRadius;
    /**
     * 点间隙
     */
    private int gap;

    public NavigatorView(Context context) {
        super(context);
        init(context);
    }

    public NavigatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NavigatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);
        points = new ArrayList<>();
        pointsRadius = DensityUtil.dip2px(context,10);
        gap = pointsRadius * 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG,"onMeasure");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG,"onDraw");
    }

    /**
     * 设置点的数量
     *
     * @param pointsCount
     */
    public void setPointsCount(int pointsCount) {

        int childCount = points.size();

        if (pointsCount > childCount) {
            if (childCount == 0) childCount = 1;
            for (int i = childCount - 1; i < pointsCount; i++) {
                View pointView = getPointView(i ==0 );
                if (i == 0 ){

                    pointView.setSelected(true);
                    LinearLayout.LayoutParams layoutParams = (LayoutParams) pointView.getLayoutParams();
                    layoutParams.leftMargin = 0;
                    pointView.setLayoutParams(layoutParams);
                }
                points.add(pointView);
                addView(pointView);
            }
        } else if (pointsCount < childCount) {
            for (int i = childCount - 1; i > pointsCount - 1; i--) {

                View view = points.get(i);
                points.remove(i);
                removeView(view);
            }
        }

    }


    /**
     * 获取点的view
     *
     * @return
     */
    private View getPointView(boolean  isFirstOne) {
        View point = new View(getContext());
        resetPointViewLayoutparams(point,isFirstOne);
        return point;
    }

    /**
     * 更新点的选中状态
     */
    public void updatePointsSelectedStatus(int position) {

        if (points == null || points.size() == 0)  return ;

        if (points.get(lastSelectedPosition) != null) {

            points.get(lastSelectedPosition).setSelected(false);
        }
        lastSelectedPosition = position;
        points.get(position).setSelected(true);

    }

    public  void setPointsBackgroundColors(@ColorRes int selectedColorID,@ColorRes int unSelectedColorID){
        isDrawableBG = false;
        this.selectedColorID = getResources().getColor(selectedColorID);
        this.unSelectedColorID = getResources().getColor(unSelectedColorID);
        updatePoints();
    }

    public void setPointsBackgroundDrawable(@DrawableRes int selectedDrawableID,@DrawableRes int unSelectedDrawableID){
        isDrawableBG = true;
        this.selectedDrawableID = selectedDrawableID;
        this.unSelectedDrawableID = unSelectedDrawableID;
        updatePoints();
    }


    public void setGap(int gap) {
        this.gap = gap;
        updatePoints();
    }

    public void setPointsRadius(int pointsRadius) {
        this.pointsRadius = pointsRadius;
        updatePoints();
    }

    /**
     * 设置完点的属性后更新
     */
    private void  updatePoints(){

        int   pointsCount =  getChildCount();
        if (pointsCount == 0 )  return ;

        for (int i = 0; i < pointsCount; i++) {

            resetPointViewLayoutparams(getChildAt(i),i==0);
        }
        invalidate();

    }

    /**
     * 设置view的  布局参数
     * @param view
     */
    private void resetPointViewLayoutparams(View  view,boolean isFirstOne){
        LinearLayout.LayoutParams layoutParams = new LayoutParams(2 * pointsRadius, 2 * pointsRadius);
        if (!isFirstOne){

            layoutParams.leftMargin = gap;
        }
//        tv.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_navigatorpoints_selector));
        StateListDrawable drawable = new StateListDrawable();
        if (isDrawableBG){
            //设置图片背景
            drawable.addState(new int[]{android.R.attr.state_selected}, getResources().getDrawable(selectedDrawableID));
            drawable.addState(new int[]{-android.R.attr.state_selected}, getResources().getDrawable(unSelectedDrawableID));
        }else{
            GradientDrawable selectDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{selectedColorID,selectedColorID});
            selectDrawable.setCornerRadius(pointsRadius);
            GradientDrawable unSelectDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{unSelectedColorID,unSelectedColorID});
            unSelectDrawable.setCornerRadius(pointsRadius);

            //设置颜色背景
            drawable.addState(new int[]{android.R.attr.state_selected},selectDrawable);
            drawable.addState(new int[]{-android.R.attr.state_selected}, unSelectDrawable);
        }

        view.setBackgroundDrawable(drawable);

        view.setLayoutParams(layoutParams);
    }
}


