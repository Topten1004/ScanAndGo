package com.example.uhf_bt.tool;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;

/**
 * Created by WuShengjun on 2017/11/4.
 */

public class ToastUtils {
    
    public final static int SHORT = Toast.LENGTH_SHORT;
    public final static int LONG = Toast.LENGTH_LONG;
    public final static int TOP = Gravity.TOP;
    public final static int CENTER = Gravity.CENTER;
    public final static int BOTTOM = Gravity.BOTTOM;
    public final static int NORMAL = -24; // 标记是正常显示

    private static Toast mToast;

    public static void showNormal(Context context, String msg) {
        showNormal(true, context, msg);
    }

    public static void showNormal(boolean cancelLast, Context context, String msg) {
        if (cancelLast)
            close();
        Toast.makeText(context, msg, SHORT).show();
    }

    public static void showNormalLong(Context context, String msg) {
        showNormalLong(true, context, msg);
    }

    public static void showNormalLong(boolean cancelLast, Context context, String msg) {
        if(cancelLast)
            close();
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间底部toast显示
     * @param context
     * @param msg
     */
    public static void showBottom(Context context, String msg) {
        showBottom(context, msg, 0, 0);
    }

    /**
     * 短时间底部显示
     * @param context
     * @param msg
     * @param xOff 正数向右偏移
     * @param yOff 正数向上偏移
     */
    public static void showBottom(Context context, String msg, int xOff, int yOff) {
        showBottom(true, context, msg, xOff, yOff);
    }

    /**
     * 短时间底部显示（是否取消上一个toast显示）
     * @param cancelLast
     * @param context
     * @param msg
     */
    public static void showBottom(boolean cancelLast, Context context, String msg) {
        showBottom(cancelLast, context, msg, 0, 0);
    }

    /**
     * 短时间底部显示，可设置x，y偏移距离
     * @param cancelLast
     * @param context
     * @param msg
     * @param xOff 正数向右偏移
     * @param yOff 正数向上偏移
     */
    public static void showBottom(boolean cancelLast, Context context, String msg, int xOff, int yOff) {
        if (cancelLast)
            close();
        mToast = Toast.makeText(context, msg, SHORT);
        mToast.setGravity(Gravity.BOTTOM, xOff, yOff);
        mToast.show();
    }

    /**
     * 长时间底部显示
     * @param context
     * @param msg
     */
    public static void showBottomLong(Context context, String msg) {
        show(true, context, msg, LONG, BOTTOM);
    }

    /**
     * 长时间底部显示（是否取消上一个toast显示）
     * @param cancelLast
     * @param context
     * @param msg
     */
    public static void showBottomLong(boolean cancelLast, Context context, String msg) {
        show(cancelLast, context, msg, LONG, BOTTOM);
    }

    /**
     * 短时间中间显示
     * @param context
     * @param msg
     */
    public static void showCenter(Context context, String msg) {
        show(true, context, msg, SHORT, CENTER);
    }

    /**
     * 短时间中间显示（是否取消上一个toast显示）
     * @param cancelLast
     * @param context
     * @param msg
     */
    public static void showCenter(boolean cancelLast, Context context, String msg) {
        show(cancelLast, context, msg, SHORT, CENTER);
    }

    /**
     * 长时间中间显示
     * @param context
     * @param msg
     */
    public static void showCenterLong(Context context, String msg) {
        show(true, context, msg, LONG, CENTER);
    }

    /**
     * 长时间中间显示（是否取消上一个toast显示）
     * @param cancelLast
     * @param context
     * @param msg
     */
    public static void showCenterLong(boolean cancelLast, Context context, String msg) {
        show(cancelLast, context, msg, LONG, CENTER);
    }

    /**
     * 短时间顶部显示
     * @param context
     * @param msg
     */
    public static void showTop(Context context, String msg) {
        show(true, context, msg, SHORT, TOP);
    }

    /**
     * 短时间顶部显示
     * @param context
     * @param msg
     */
    public static void showTop(Context context, String msg, int xOff, int yOff) {
        showTop(true, context, msg, xOff, yOff);
    }

    /**
     * 短时间顶部显示（是否取消上一个toast显示）
     * @param cancelLast
     * @param context
     * @param msg
     */
    public static void showTop(boolean cancelLast, Context context, String msg) {
        show(cancelLast, context, msg, SHORT, TOP);
    }

    /**
     * 短时间顶部显示，可设置x，y偏移距离
     * @param cancelLast
     * @param context
     * @param msg
     * @param xOff 正数向右偏移
     * @param yOff 正数向下偏移
     */
    public static void showTop(boolean cancelLast, Context context, String msg, int xOff, int yOff) {
        if (cancelLast)
            close();
        mToast = Toast.makeText(context, msg, SHORT);
        mToast.setGravity(TOP, xOff, yOff);
        mToast.show();
    }

    /**
     * 长时间顶部显示
     * @param context
     * @param msg
     */
    public static void showTopLong(Context context, String msg) {
        show(true, context, msg, LONG, TOP);
    }

    /**
     * 长时间顶部显示（是否取消上一个toast显示）
     * @param cancelLast
     * @param context
     * @param msg
     */
    public static void showTopLong(boolean cancelLast, Context context, String msg) {
        show(cancelLast, context, msg, LONG, TOP);
    }

    /**
     * 指定布局显示msg，根布局需为TextView
     * @param context
     * @param toastLayout
     * @param msg
     */
    public static void showTextView(Context context, @LayoutRes int toastLayout, String msg) {
        close();
        mToast = new Toast(context);
        View view = View.inflate(context, toastLayout, null);
        mToast.setView(view);
        if(view instanceof TextView) {
            TextView tvMsg = (TextView) view;
            tvMsg.setText(msg);
        }
        mToast.show();
    }

    /**
     * 自定义布局显示（默认底部位置）
     * @param context
     * @param toastLayout
     */
    public static void showLayout(Context context, @LayoutRes int toastLayout) {
        showLayout(true, context, toastLayout, NORMAL);
    }

    /**
     * 自定义布局显示（默认底部位置）
     * @param cancelLast 是否取消上一次toast显示
     * @param context
     * @param toastLayout
     */
    public static void showLayout(boolean cancelLast, Context context, @LayoutRes int toastLayout) {
        showLayout(cancelLast, context, toastLayout, NORMAL);
    }

    /**
     * 自定义布局显示（默认底部位置）
     * @param context
     * @param toastLayout
     */
    public static void showLayoutTop(Context context, @LayoutRes int toastLayout) {
        showLayout(true, context, toastLayout, TOP);
    }

    /**
     * 自定义布局显示（默认底部位置）
     * @param context
     * @param toastLayout
     */
    public static void showLayoutBottm(Context context, @LayoutRes int toastLayout) {
        showLayout(true, context, toastLayout, BOTTOM);
    }

    /**
     * 显示自定义布局信息，仅当次有效(优先显示)
     * @param context
     * @param toastLayout  自定义布局
     * @param gravity   显示位置
     */
    public static void showLayout(boolean cancelLast, Context context, @LayoutRes int toastLayout, int gravity) {
        show(cancelLast, context, toastLayout, null, SHORT, gravity, 0, 0);
    }

    private static void show(boolean cancelLastToast, Context context, String msg, int duration, int gravity) {
        show(cancelLastToast, context, msg, duration, gravity, 0, 0);
    }

    public static void show(boolean cancelLastToast, Context context, String msg, int duration, int gravity, int xOffset, int yOffset) {
        show(cancelLastToast, context, null, msg, duration, gravity, xOffset, yOffset);
    }

    private static void show(boolean cancelLastToast, Context context, @LayoutRes int toastLayout, String msg, int duration, int gravity, int xOffset, int yOffset) {
        View view = View.inflate(context, toastLayout, null);
        show(cancelLastToast, context, view, msg, duration, gravity, xOffset, yOffset);
    }

    private static void show(boolean cancelLastToast, Context context, View view, String msg, int duration, int gravity, int xOffset, int yOffset) {
        if(cancelLastToast) {
            close();
        }
        if(view == null) {
            mToast = Toast.makeText(context, msg, duration);
        } else {
            mToast = new Toast(context);
            mToast.setView(view);
            mToast.setDuration(duration);
        }
        if(gravity != NORMAL)
            mToast.setGravity(gravity, xOffset, yOffset);
        mToast.show();
    }

    public static void close() {
        if (mToast != null){
            mToast.cancel();
            mToast = null;
        }
    }
    
    private static String getString(Context context, int resId) {
        return context.getString(resId);
    }
    
    private static float getDimension(Context context, int dimenId) {
        return context.getResources().getDimension(dimenId);
    }
    
    private static <T extends View> T getView(View parent, int id) {
        return (T) parent.findViewById(id);
    }
}
