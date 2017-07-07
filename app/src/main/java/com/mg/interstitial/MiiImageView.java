package com.mg.interstitial;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wuqiyan on 17/6/13.
 */

public class MiiImageView extends ImageView {

    public MiiImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MiiImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiiImageView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //获取控件需要重新绘制的区域
        Rect rect=canvas.getClipBounds();
//        rect.bottom--;
//        rect.right--;
        Paint paint=new Paint();
        paint.setColor(Color.argb(30, 41, 36, 33));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        canvas.drawRect(rect, paint);
    }

}
