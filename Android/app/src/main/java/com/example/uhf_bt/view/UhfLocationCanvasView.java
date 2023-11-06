package com.example.uhf_bt.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2019/5/11.
 */

public class UhfLocationCanvasView extends View {

    String TAG="UHF_LocationCanvasView";
    float value_top=0;
    private Paint paint=new Paint();
    private Paint periphery_paint=new Paint();
    private Handler mh=new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what==0){
                UhfLocationCanvasView.this.invalidate();
            }
        };
    };

    private void clean(Canvas canvas){
        if(canvas!=null) {
            //--------清空canvas----------------
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPaint(paint);
            canvas.drawARGB(255,255,255,255);
            //------------------------
            paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLUE);
            paint.setFakeBoldText(true);
            paint.setTextSize(16);
        }
    }

    public UhfLocationCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setStyle(Paint.Style.FILL);//实心
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setFakeBoldText(true);
        paint.setTextSize(16);

        periphery_paint.setStyle(Paint.Style.STROKE);//空心
        periphery_paint.setAntiAlias(true);
        periphery_paint.setColor(Color.RED);
        periphery_paint.setFakeBoldText(true);
        periphery_paint.setTextSize(16);
    }
    public void clean(){
        setData(0);
    }
    public void setData(int value){
        Log.e(TAG,"value="+value);
        if(value<0)
            value=0;
        value_top=(100-value) * ((bottom-top)/100F);
        mh.sendEmptyMessage(0);   //发送空消息通知刷新
    }

    int width=100;
    int left=0;
    int right=0;
    int top=0;
    int bottom=getMeasuredWidth();
    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        bottom = getMeasuredHeight();
        left=getMeasuredWidth()/2-width/2;
        right=getMeasuredWidth()/2+width/2;
        Log.e(TAG,"bottom="+bottom);
        Log.e(TAG,"left="+left);
        Log.e(TAG,"right="+right);
        Log.e(TAG,"value_top="+value_top);
        clean(canvas);
        canvas.drawRect(left,top,right,bottom,periphery_paint);
        canvas.drawRect(left,value_top,right,bottom,paint);

    }

}
