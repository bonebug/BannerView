
package com.chad.library.adapter.base.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.R;
import com.chad.library.adapter.base.BaseAllAdapter;
import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

/**
 * Created by songzhiyang on 2017/6/6.
 */

public class StickyHeaderRecycler extends FrameLayout {
    private FrameLayout mStickyHeaderContainer;
    private int mHeaderRes = 0;
    private RecyclerView mRecyclerView = null;

    public StickyHeaderRecycler(Context context) {
        super(context);
        init(context);
    }

    public StickyHeaderRecycler(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StickyHeaderRecycler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.sticky_header_recycler, this, true);
    }

    public void setStickyHeaderResource(int headerRes) {
        this.mHeaderRes = headerRes;
        View v = LayoutInflater.from(getContext()).inflate(headerRes, null, false);
        getStickyHeaderContainer().addView(v);
        getStickyHeaderContainer().setVisibility(View.INVISIBLE);
    }

    public void initSearchIndexBar(List<String> indexDatas, int indexItemRes,
            final int clickColor) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.search_index);
        if (linearLayout.getChildCount() > 0) {
            linearLayout.removeAllViews();
        }
        for (String index : indexDatas) {
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(indexItemRes,
                    null, false);
            textView.setText(index);
            textView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (v instanceof TextView) {
                                ((TextView) v).setTextColor(clickColor);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (v instanceof TextView) {
                                ((TextView) v).setTextColor(Color.BLACK);
                            }
                            break;
                    }
                    return false;
                }
            });
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v instanceof TextView) {
                        int dataPosition = -1;
                        String indexStr = ((TextView) v).getText().toString();
                        RecyclerView.Adapter adapter = getRecyclerView().getAdapter();
                        if (adapter instanceof BaseAllAdapter) {
                            List<SectionEntity> datas = ((BaseAllAdapter) adapter).getData();
                            for (int i = 0; i < datas.size(); i++) {
                                String compareableStr = ((BaseAllAdapter) adapter)
                                        .getCompareableStr(datas.get(i));
                                if (!TextUtils.isEmpty(compareableStr)
                                        && compareableStr.equals(indexStr)) {
                                    dataPosition = i;
                                    break;
                                }
                            }
                            scrollToTop(dataPosition, ((BaseAllAdapter) adapter));
                        }
                    }
                }
            });
            linearLayout.addView(textView);
        }
    }

    private void scrollToTop(int dataPostion, BaseAllAdapter adapter) {
        if (dataPostion != -1) {
            int scrollPosition = dataPostion + adapter.getHeaderLayoutCount();
            RecyclerView.LayoutManager layoutManager = getRecyclerView().getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(scrollPosition, 0);
            }
        }
    }

    public RecyclerView getRecyclerView() {
        if (mRecyclerView == null) {
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
            mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    dealSticky(recyclerView);
                }
            });
        }
        return mRecyclerView;
    }

    /**
     * 处理sticky效果
     * 
     * @param recyclerView
     */
    private void dealSticky(RecyclerView recyclerView) {
        FrameLayout stickyHeaderContainer = getStickyHeaderContainer();
        int stickyHeaderHeight = stickyHeaderContainer.getMeasuredHeight();
        if (this.mHeaderRes == 0 || stickyHeaderHeight <= 0) {
            return;
        }
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (!(adapter instanceof BaseAllAdapter) || !((BaseAllAdapter) adapter).isHasHeader()) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int firstPosition = ((LinearLayoutManager) layoutManager)
                    .findFirstVisibleItemPosition();
            int firstViewType = adapter.getItemViewType(firstPosition);
            if (firstViewType == BaseAllAdapter.HEADER_VIEW) {
                getStickyHeaderContainer().setVisibility(View.GONE);
            } else {
                getStickyHeaderContainer().setVisibility(View.VISIBLE);
            }
            int headerCount = ((BaseAllAdapter) adapter).getHeaderLayoutCount();
            Object obj = null;
            if (firstPosition <= headerCount) {
                obj = ((BaseAllAdapter) adapter).getItem(firstPosition);
            } else {
                obj = ((BaseAllAdapter) adapter).getItem(firstPosition - headerCount);
            }
            if (obj instanceof SectionEntity) {
                ((BaseAllAdapter) adapter).updateStickyHeader(
                        getStickyHeaderContainer().getChildAt(0), ((SectionEntity) obj).header);
            }
            View nextInfoView = recyclerView.findChildViewUnder(
                    stickyHeaderContainer.getMeasuredWidth() / 2, stickyHeaderHeight - 0.01f);
            if (nextInfoView == null) {
                return;
            }
            int viewType = layoutManager.getItemViewType(nextInfoView);
            if (viewType != BaseAllAdapter.SECTION_HEADER_VIEW) {
                stickyHeaderContainer.setTranslationY(0);
                return;
            }
            View transInfoView = recyclerView.findChildViewUnder(
                    stickyHeaderContainer.getMeasuredWidth() / 2, stickyHeaderHeight + 0.01f);
            // 获取滚动范围
            int dealtY = transInfoView.getTop() - stickyHeaderHeight;
            if (transInfoView.getTop() > 0) {
                stickyHeaderContainer.setTranslationY(dealtY);
            } else {
                stickyHeaderContainer.setTranslationY(0);
            }
        }
    }

    private FrameLayout getStickyHeaderContainer() {
        if (this.mStickyHeaderContainer == null) {
            this.mStickyHeaderContainer = (FrameLayout) findViewById(R.id.sticky_header);
        }
        return this.mStickyHeaderContainer;
    }
}
