package com.test.banner.itemtype;

import android.support.annotation.NonNull;

import com.test.banner.R;
import com.test.banner.databean.ItemData;

import bone.com.mycustombanner.RecyclerViewBanner.adapter.BaseViewHolder;
import bone.com.mycustombanner.itemtype.BaseItemType;

/**
 * 功能:文本类型的item
 * ＊创建者：赵然 on 2017/10/9 17:06
 * ＊
 */

public class TextItemTypeImp implements BaseItemType<ItemData> {
    public static final int TYPE = 1002;

    @Override
    public int getItemViewLay() {
        return R.layout.selftextbanner_layout;
    }

    @Override
    public void instantiateItem(BaseViewHolder holder, ItemData data) {
        holder.setText(R.id.iv_selfbanner_text,data.text);
    }

    @NonNull
    @Override
    public int getItemType() {
        return TYPE;
    }
}
