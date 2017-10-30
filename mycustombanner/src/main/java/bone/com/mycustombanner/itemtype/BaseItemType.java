package bone.com.mycustombanner.itemtype;

import android.support.annotation.LayoutRes;

import bone.com.mycustombanner.RecyclerViewBanner.adapter.BaseViewHolder;


/**
 * 功能：多type类型的item基类
 * ＊创建者：赵然 on 2017/10/9 15:21
 * <p>
 * 注意  itemType = 1000 已被使用  详见{@link DefaultItemType}
 * ＊
 */

public interface BaseItemType<T> {
    //基本只做inflate布局用
    @LayoutRes
    int getItemViewLay();

    //数据填充 点击事件
    void instantiateItem(BaseViewHolder holder, T data);

    //返回当前UI类型对应的类型ID  databean中的类型需与此处返回对应
    int getItemType();

}
