package com.example.painter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.view.SurfaceHolder;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import static com.example.painter.DrawView.TOUCH_TOLERANCE;

class DrawThread extends Thread {

    private Path mPath;

    private boolean running = false;
    private SurfaceHolder surfaceHolder;
    private int counter;

    private  int backgroundColor;
    private  MaskFilter mEmboss;
    private  MaskFilter mBlur;
    private  Paint mPaint;
    private Canvas mCanvas;
    //private  LinkedSet<FingerPath> paths;
    private  Bitmap mBitmap;
    private  Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private DrawView dv;


    public void init(DrawView dv) {
        this.dv = dv;
        backgroundColor = dv.getBackgroundColor();
        mEmboss = dv.getmEmboss();
        mBitmap = dv.getmBitmap();
        mBlur = dv.getmBlur();
        mPaint = dv.getmPaint();
       // paths = dv.getPaths();
        mBitmapPaint = dv.getmBitmapPaint();
        mCanvas = dv.getmCanvas();
        mPath = dv.getmPath();

    }

    public DrawThread(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void touchStart(float x, float y,
                           int currentColor, boolean emboss, boolean blur, int strokeWidth ) {
        /*mPath = new Path();
        FingerPath fp = new FingerPath(currentColor, emboss, blur, strokeWidth, mPath);
        paths.add(fp);*/
        mPath.moveTo(x, y);

        /*mCanvas.drawPath(mPath, mPaint);

        mPath.reset();*/

       // mPath.moveTo(x, y);

    }

    public void touchMove(float x, float y, float mX, float mY) {
        float dX = Math.abs(x - mX);
        float dY = Math.abs(y - mY);

       /* if (dX >= TOUCH_TOLERANCE
                || dY >= TOUCH_TOLERANCE) {*/
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
        //}
    }

    public void touchUp(float mX, float mY) {
        mCanvas.drawPath(mPath, mPaint);

        mPath.reset();

        //mPath.lineTo(mX, mY);
    }

    @Override
    public void run() {
        Canvas canvas;
        while (running) {
            canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas(null);
                //mCanvas = surfaceHolder.lockCanvas(null);

                synchronized (surfaceHolder) {
                if (canvas == null)
                    continue;
                canvas.save();
                //counter = paths.size();
                canvas.drawColor(backgroundColor);

                   /* if (mCanvas == null)
                        continue;
                    mCanvas.save();
                    mCanvas.drawColor(backgroundColor);*/

                /*for (FingerPath fp : paths) {
                    if(counter < 0) break;
                    mPaint.setColor(fp.getColor());
                    mPaint.setStrokeWidth(fp.getStrokeWidth());
                    mPaint.setMaskFilter(null);

                    if (fp.isEmboss())
                        mPaint.setMaskFilter(mEmboss);
                    else if (fp.isBlur())
                        mPaint.setMaskFilter(mBlur);

                    canvas.drawPath(fp.getPath(), mPaint);


                    counter--;
                }*/

                    /*mCanvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
                    mCanvas.drawPath(mPath, mPaint);*/

                    canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
                    canvas.drawPath(mPath, mPaint);


               /* canvas.drawBitmap(mBitmap, 0, 0, mPaint);
                canvas.restore();*/
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                /*if(mCanvas != null) {
                    surfaceHolder.unlockCanvasAndPost(mCanvas);
                }*/
            }
        }
    }
}