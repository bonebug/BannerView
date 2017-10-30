
package com.chad.library.adapter.base;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

/**
 * Created by songzhiyang on 2017/6/6.
 */

public abstract class BaseAllAdapter<T extends SectionEntity, K extends BaseViewHolder>
        extends BaseQuickAdapter<T, K> {

    protected int mSectionHeadResId;
    public static final int SECTION_HEADER_VIEW = 0x00000444;

    public BaseAllAdapter(int layoutResId, int sectionHeadResId, List<T> data) {
        super(layoutResId, data);
        this.mSectionHeadResId = sectionHeadResId;
    }

    @Override
    protected int getDefItemViewType(int position) {
        return mData.get(position).isHeader && mSectionHeadResId != 0 ? SECTION_HEADER_VIEW : 0;
    }

    @Override
    protected K onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SECTION_HEADER_VIEW)
            return createBaseViewHolder(getItemView(mSectionHeadResId, parent));

        return super.onCreateDefViewHolder(parent, viewType);
    }

    @Override
    protected boolean isFixedViewType(int type) {
        return super.isFixedViewType(type) || type == SECTION_HEADER_VIEW;
    }

    @Override
    public void onBindViewHolder(K holder, int positions) {
        switch (holder.getItemViewType()) {
            case SECTION_HEADER_VIEW:
                setFullSpan(holder);
                convertHead(holder, mData.get(holder.getLayoutPosition() - getHeaderLayoutCount()));
                break;
            default:
                super.onBindViewHolder(holder, positions);
                break;
        }
    }

    public boolean isHasHeader() {
        return mSectionHeadResId == 0 ? false : true;
    }

    public abstract void updateStickyHeader(View view, String header);

    protected abstract void convertHead(K helper, T item);

    public abstract String getCompareableStr(SectionEntity<T> entity);
}
