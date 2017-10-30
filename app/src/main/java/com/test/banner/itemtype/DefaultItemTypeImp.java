package com.test.banner.itemtype;

import com.test.banner.R;
import com.test.banner.databean.ItemData;

import bone.com.mycustombanner.RecyclerViewBanner.adapter.BaseViewHolder;
import bone.com.mycustombanner.itemtype.DefaultItemType;

/**
 * 功能：默认Item类型   继承 @{@link DefaultItemType}
 * ＊创建者：赵然 on 2017/10/9 15:28
 * ＊
 */

public class DefaultItemTypeImp extends DefaultItemType<ItemData> {

    @Override
    public int getItemViewLay() {
        return R.layout.selfimagebanner_layout;
    }

    @Override
    public void instantiateItem(BaseViewHolder holder, ItemData data) {
        holder.loadImage(R.id.iv_selfbanner_pic,data.url);
    }
}
