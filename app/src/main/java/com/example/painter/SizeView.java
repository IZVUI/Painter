package com.example.painter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class SizeView extends View {

    public Paint paint;
    public float radius;
    public int density;
    public int color;

    public SizeView(Context context) {
        super(context);
        paint = new Paint();
        radius = 10;
    }

    public SizeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        radius = 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(radius <= 0) radius = 1;

        canvas.drawColor(color);

        canvas.setDensity(density);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2,
                radius, paint);


    }
}
