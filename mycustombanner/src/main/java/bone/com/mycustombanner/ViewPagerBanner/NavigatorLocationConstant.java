package bone.com.mycustombanner.ViewPagerBanner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 功能： 导航点所处的位置的枚举
 * ＊创建者：赵然 on 2017/10/12 17:18
 * ＊
 */

public class NavigatorLocationConstant {

    public static final int TOP_LEFT = 0;
    public static final int TOP_CENTER = 1;
    public static final int TOP_RIGHT = 2;
    public static final int BOTTOM_LEFT = 3;
    public static final int BOTTOM_CENTER = 4;
    public static final int BOTTOM_RIGHT = 5;

    public NavigatorLocationConstant() {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NavigatorLocation {
    }

}
