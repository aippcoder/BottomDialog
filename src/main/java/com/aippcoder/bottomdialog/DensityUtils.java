package com.aippcoder.bottomdialog;

import android.content.Context;

/**
 * 密度工具类，提供计算屏幕分辨率和屏幕密度相关的方法
 *
 *
 * Created by san on 2017/3/18.
 */

public class DensityUtils {

    public static int dip2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5);
    }

    public static int px2dip(Context context, int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5);
    }

}
