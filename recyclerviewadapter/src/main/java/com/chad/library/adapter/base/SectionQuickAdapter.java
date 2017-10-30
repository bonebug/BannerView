package com.chad.library.adapter.base;


import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

/**
 * 功能： 悬浮item的常用adapter
 * 需扩展 请查看{@link BaseSectionQuickAdapter<T extends SectionEntity, K extends BaseViewHolder> }.
 * ＊创建者：赵然 on 2017/6/14 17:36
 * ＊
 */

public abstract class SectionQuickAdapter<T extends SectionEntity> extends BaseSectionQuickAdapter<T,BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public SectionQuickAdapter(int layoutResId, int sectionHeadResId, List<T> data) {
        super(layoutResId, sectionHeadResId, data);
    }
}
