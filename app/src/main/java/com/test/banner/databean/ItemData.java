package com.test.banner.databean;


import bone.com.mycustombanner.entity.ItemTypeBeanInterface;

/**
 * 功能：页面数据bean
 * ＊创建者：赵然 on 2017/10/9 15:28
 * ＊
 */

public class ItemData implements ItemTypeBeanInterface {

    public String url;
    public String text;
    public int type;
    @Override
    public int getItemType() {
        return type;
    }
}
