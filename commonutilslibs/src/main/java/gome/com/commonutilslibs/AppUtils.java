package gome.com.commonutilslibs;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

/**
 * 功能：
 * ＊创建者：赵然 on 2017/1/18 15:17
 * ＊
 */

public class AppUtils {
    /**
     * 显示键盘
     * @param context
     */
    public static void showKeyBoard(Activity context){

        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(context.getWindow().getDecorView()
                    .getWindowToken(), 0);
        }
    }
    /**
     * 隐藏键盘
     */
    public static void HiddenKeyBoard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(context.getWindow().getDecorView()
                    .getWindowToken(), 0);
        }
    }

    /**
     * 获取屏幕宽高信息
     *
     * @param activity
     * @return DisplayMetrics
     */
    public static DisplayMetrics getScreenDisplay(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取屏幕尺寸
     *
     * @return
     */
    public static String getScreen(Activity activity) {
        DisplayMetrics dm = getScreenDisplay(activity);
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        String screen = dm.widthPixels + "x" + dm.heightPixels;
        if (TextUtils.isEmpty(screen)) {
            screen = "screen";
        }
        return screen;
    }



}
