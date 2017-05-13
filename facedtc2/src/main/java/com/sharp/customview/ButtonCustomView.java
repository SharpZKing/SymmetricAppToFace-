package com.sharp.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Zjf on 2017/1/4.
 */
public class ButtonCustomView extends Button {

    public ButtonCustomView(Context context) {
        super(context);
    }

    public ButtonCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        Paint paint = new Paint();
        paint.setStrokeWidth(8);
        paint.setColor(Color.WHITE);
        canvas.drawLine(width/2-70,height/2,width/2+72,height/2,paint);
        canvas.drawLine(width/2+30, height/2-25, width/2+70,height/2,paint);
        canvas.drawLine(width/2+30, height/2+25, width/2+70,height/2,paint);
    }

}
