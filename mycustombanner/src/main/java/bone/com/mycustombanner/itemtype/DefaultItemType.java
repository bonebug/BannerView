package bone.com.mycustombanner.itemtype;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

/**
 * 功能： 如果只有一个类型的item  可以继承这个
 * ＊创建者：赵然 on 2017/10/9 16:44
 * ＊
 */

public abstract class DefaultItemType<T> implements BaseItemType<T> {

    private static final  int DEFAULT_ITEMTYPE = 1000;

    @CheckResult
    @NonNull
    @Override
    public int getItemType() {
        return DEFAULT_ITEMTYPE;
    }


}
