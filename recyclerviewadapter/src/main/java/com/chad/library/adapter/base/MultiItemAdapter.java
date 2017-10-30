package com.chad.library.adapter.base;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;


/**
 * 功能：常用的多类型的adapter
 *
 * 需扩展请查看
 * {@link BaseMultiItemQuickAdapter<T extends MultiItemEntity, K extends BaseViewHolder>}
 *
 * ＊创建者：赵然 on 2017/6/14 15:31
 * ＊
 */

public abstract class MultiItemAdapter<T extends MultiItemEntity> extends BaseMultiItemQuickAdapter<T,BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MultiItemAdapter(List<T> data) {
        super(data);
    }
}
