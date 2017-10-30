package com.chad.library.adapter.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import java.util.List;


/**
 * 功能：常用的基本Adapter  需扩展 请查看
 * {@link BaseQuickAdapter<T, K extends BaseViewHolder> }.
 * ＊创建者：赵然 on 2017/6/14 15:31
 * ＊
 */

public abstract class QuickAdapter<T> extends BaseQuickAdapter<T,BaseViewHolder> {

    public QuickAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public QuickAdapter(@Nullable List<T> data) {
        super(data);
    }

    public QuickAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }
}
