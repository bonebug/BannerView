package com.test.banner.itemtype;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.test.banner.R;
import com.test.banner.databean.ItemData;

import bone.com.mycustombanner.RecyclerViewBanner.adapter.BaseViewHolder;
import bone.com.mycustombanner.itemtype.BaseItemType;
import bone.com.mycustombanner.utils.LogUtils;

/**
 * 功能：牵扯到多Item类型时
 * ＊创建者：赵然 on 2017/10/9 17:06
 * ＊
 */

public class ImageItemTypeImp implements BaseItemType<ItemData> {
    public static final  int TYPE = 1001;

    public ImageItemTypeImp(){

    }

    @Override
    public int getItemViewLay() {
        return R.layout.selfimagebanner_layout;
    }

    @Override
    public void instantiateItem(final BaseViewHolder holder, ItemData data) {
        holder.loadImage(R.id.iv_selfbanner_pic,data.url);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.Zlog().e("条目 imageItem条目点击");
                Toast.makeText(holder.itemView.getContext(), "条目点击了", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @NonNull
    @Override
    public int getItemType() {
        return TYPE;
    }
}
