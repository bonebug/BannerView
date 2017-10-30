package com.chad.library.adapter.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.entity.BaseSectionInterface;

import java.util.List;

import gome.com.commonutilslibs.LogUtils;


/**
 * 功能：辅助recycle实现分组的ItemDecoration
 * ＊创建者：赵然 on 2017/7/19 14:52
 * ＊
 */

public class SectionItemDecoration extends RecyclerView.ItemDecoration {
    private Paint textPaint,stickerViewPaint;
    private int itemHeight = 50;
    private float textSize = 30f;
    private int textColor = Color.BLACK;
    private Context mContext;



    public SectionItemDecoration(Context context) {
        this.mContext = context;
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.LEFT);


        stickerViewPaint = new Paint();
        stickerViewPaint.setColor(Color.GRAY);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int childAdapterPosition = parent.getChildAdapterPosition(view);
        if (isNewGroup(childAdapterPosition, parent)) {
            outRect.top = itemHeight;
        } else {
            outRect.top = 0;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();
        List<BaseSectionInterface> data = getData(parent);

        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int left = view.getLeft() + view.getPaddingLeft() + parent.getLeft() + parent.getPaddingLeft();
            int position = parent.getChildAdapterPosition(view);
            BaseSectionInterface dataEntity = data.get(position);
            if (isNewGroup(position, parent)) {

                Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
                int baseline = view.getTop() - (itemHeight / 2 - (fontMetrics.descent - fontMetrics.ascent) / 2 + fontMetrics.descent);
                stickerViewPaint.setColor(Color.GRAY);
                c.drawRect(parent.getPaddingLeft(), 0, parent.getPaddingRight(), itemHeight, stickerViewPaint);
                c.drawText(dataEntity.getGroupShowName(), left, baseline, textPaint);
            }

        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int firstVisibleItemPosition = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        View itemView = parent.findViewHolderForLayoutPosition(firstVisibleItemPosition).itemView;

        boolean willNewGroup = isWillNewGroup(firstVisibleItemPosition, parent);
        int  yOffset = itemHeight;
        if (willNewGroup){
            yOffset = itemView.getBottom();
        }
        LogUtils.gome().e("position:"+firstVisibleItemPosition+" yOffset:"+yOffset + " name:"+ getData(parent).get(firstVisibleItemPosition).getGroupShowName());
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = yOffset - (itemHeight / 2 - (fontMetrics.descent - fontMetrics.ascent) / 2 + fontMetrics.descent);
        c.drawRect(parent.getPaddingLeft(),  0, parent.getRight(), yOffset, stickerViewPaint);
        c.drawText(getData(parent).get(firstVisibleItemPosition).getGroupShowName() , 0, baseline, textPaint);
    }

    /**
     * 获取列表当前的数据
     *`
     * @param parent
     * @return
     */
    private List<BaseSectionInterface> getData(RecyclerView parent) {
        if (parent.getAdapter() instanceof BaseQuickAdapter) {
            BaseQuickAdapter adapter = (BaseQuickAdapter) parent.getAdapter();
            List data = adapter.getData();
            if (data != null && data.size() > 0) {
                if (BaseSectionInterface.class.isAssignableFrom(data.get(0).getClass())) {
                    return data;
                }
            }

        }
        return null;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        if (textPaint != null) {
            textPaint.setTextSize(textSize);
        }
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        if (textPaint != null) {
            textPaint.setColor(textColor);
        }
    }

    /**
     * 判断是否是新的组
     *
     * @param position
     * @param parent
     * @return
     */
    private boolean isNewGroup(int position, RecyclerView parent) {
        if (position == 0) {
            return true;
        } else {
            List<BaseSectionInterface> data = getData(parent);
            String groupID = data.get(position).getGroupID();
            String groupID1 = data.get(position - 1).getGroupID();
            return !groupID.equals(groupID1);

        }
    }
    private boolean isWillNewGroup(int position, RecyclerView parent){
            List<BaseSectionInterface> data = getData(parent);
            String groupID = data.get(position).getGroupID();
            String groupID1 = data.get(position + 1).getGroupID();
            return !groupID.equals(groupID1);

    }
}