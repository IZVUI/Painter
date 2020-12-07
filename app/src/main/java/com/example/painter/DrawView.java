package com.example.painter;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.painter.DAO.DB;
import com.example.painter.activity.HomeActivity;
import com.example.painter.model.Picture;
import com.example.painter.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;


public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    public static int brush_size = 10;
    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    public static final float TOUCH_TOLERANCE = 1;

    DrawThread drawThread;

    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    // private List<FingerPath> paths = new ArrayList();
    // private LinkedSet<FingerPath> paths = new LinkedSet<>();
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private boolean emboss;
    private boolean blur;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private Bitmap mBitmap;
    private Bitmap backgroundBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private boolean erase = false;
    private ScaleListener listener;
    private Scaler s;
    private boolean isScaling = false;

    public DrawView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public DrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        mPaint.setAlpha(0xff);
        mPaint.setStrokeWidth(brush_size);


        mPath = new Path();

        mEmboss = new EmbossMaskFilter(new float[]{1, 1, 1}, 0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);



    }


    public void init(DisplayMetrics metrics) {
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        if(mBitmap == null)
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        clear();
        mCanvas = new Canvas(mBitmap);

        currentColor = DEFAULT_COLOR;
        strokeWidth = brush_size;

        listener = new ScaleListener(this);
        s = new Scaler(getContext(), listener);


    }

    private void setImageOnBitmap(Bitmap image) {
        for(int i = 0; i < image.getHeight(); i++ ) {
            for(int j = 0; j < image.getWidth(); j++) {
              //  if(j >= mBitmap.getHeight()) return;
               // if(i >= mBitmap.getWidth()) break;
                mBitmap.setPixel(j, i, image.getPixel(j, i));
            }
        }
    }


    private void initBackgroundBitmap(int width, int height) {
        backgroundBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < backgroundBitmap.getWidth(); i++)
            for (int j = 0; j < backgroundBitmap.getHeight(); j++) {
                backgroundBitmap.setPixel(i, j, backgroundColor);
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bitmap setOnBack(Bitmap bitmap) {
        Bitmap newBitmap = backgroundBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Color empty = Color.valueOf(0, 0, 0, 0);
        for (int i = 0; i < newBitmap.getWidth(); i++) {
            for (int j = 0; j < newBitmap.getHeight(); j++) {
                if (!Color.valueOf(bitmap.getPixel(i, j)).equals(empty)) {
                    newBitmap.setPixel(i, j, bitmap.getPixel(i, j));
                }
            }
        }
        return newBitmap;
    }


    /*@Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(backgroundColor);

        for (FingerPath fp : paths) {
            mPaint.setColor(fp.getColor());
            mPaint.setStrokeWidth(fp.getStrokeWidth());
            mPaint.setMaskFilter(null);

            if (fp.isEmboss())
                mPaint.setMaskFilter(mEmboss);
            else if (fp.isBlur())
                mPaint.setMaskFilter(mBlur);

            mCanvas.drawPath(fp.getPath(), mPaint);
        }

       canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }*/

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void touchStart(float x, float y) {
        drawThread.touchStart(x, y, currentColor, emboss, blur, strokeWidth);
        /*mPath = new Path();
        FingerPath fp = new FingerPath(currentColor, emboss, blur, strokeWidth, mPath);
        paths.add(fp);

        mPath.reset();
        mPath.moveTo(x, y);*/
        mX = x;
        mY = y;
    }

    public void setErase(boolean isErase) {
        erase = isErase;
        if (erase) {
            setCurrentColor(backgroundColor);
            removeBlur();
            // mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        } else {
            //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        }
    }

    private void touchMove(float x, float y) {
        drawThread.touchMove(x, y, mX, mY);
    /*    float dX = Math.abs(x - mX);
        float dY = Math.abs(y - mY);

        if (dX >= TOUCH_TOLERANCE
                || dY >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);*/
        mX = x;
        mY = y;
    }

    private void touchUp() {
        drawThread.touchUp(mX, mY);
        //mPath.lineTo(mX, mY);
    }

    public void clear() {
        mBitmap.eraseColor(backgroundColor);
        //paths.clear();
        invalidate();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder());
        drawThread.init(this);
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getPointerCount() == 2 && isScaling) {
            s.onTouchEvent(event);
        } else if (!isScaling) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchStart(x, y);
                    // invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touchUp();
                    // invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchMove(x, y);
                    // invalidate();
                    break;
            }
        }

        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void savePicture(String pictureName) {
        // File file = new File("Painter\\"+pictureName+".png");

        ContentResolver resolver = getContext().getContentResolver();
        String imgSaved = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            imgSaved = MediaStore.Images.Media.insertImage(
                    resolver, mBitmap,//this.getDrawingCache(),
                    pictureName + ".png", "drawing");

        }
        if (imgSaved != null) {
            Toast savedToast = Toast.makeText(getContext(),
                    "Drawing saved to Gallery!" + "\n", Toast.LENGTH_LONG);

            String finalImgSaved = imgSaved;
            Completable.fromRunnable(new Runnable() {
                @Override
                public void run() {
                    Picture picture = new Picture(finalImgSaved, pictureName, HomeActivity.user.getId());
                    DB.getDB().pictureDao().insert(picture);
                }
            })
                    .subscribeOn(Schedulers.io())
                    .subscribe();

            savedToast.show();

        } else {
            Toast unsavedToast = Toast.makeText(getContext(),
                    "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
            unsavedToast.show();
        }


        //MediaStore.Images.Media.insertImage(resolver, mBitmap, pictureName,"Saved image");
        /*synchronized (getHolder()) {
            try {
                FileOutputStream fos = (FileOutputStream) resolver.openOutputStream(Uri.fromFile(file), "rwt");
                //new FileOutputStream(file);
                //getContext()
                //.openFileOutput(pictureName, Context.MODE_APPEND);
                //new FileOutputStream(file, false);
                boolean isSaved = mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                if (isSaved) {
                    Toast savedToast = Toast.makeText(getContext(),
                            "Drawing saved to Gallery!" + "\n", Toast.LENGTH_LONG);
                    savedToast.show();
                } else {
                    Toast unsavedToast = Toast.makeText(getContext(),
                            "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }*/
    }

    public void setBlur(MaskFilter blur) {
        mPaint.setMaskFilter(blur);
        setBlur(true);
    }

    public void removeBlur() {
        mPaint.setMaskFilter(null);
        setBlur(false);
    }



    /*
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            drawThread = new DrawThread(getHolder());
            drawThread.init(this);
            drawThread.setRunning(true);
            drawThread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            drawThread.setRunning(false);
            while (retry) {
                try {
                    drawThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
        }

    */
    public static int getBrush_size() {
        return brush_size;
    }

    public static void setBrush_size(int brush_size) {
        DrawView.brush_size = brush_size;
    }

    public static int getDefaultColor() {
        return DEFAULT_COLOR;
    }

    public static int getDefaultBgColor() {
        return DEFAULT_BG_COLOR;
    }

    public static float getTouchTolerance() {
        return TOUCH_TOLERANCE;
    }

    public DrawThread getDrawThread() {
        return drawThread;
    }

    public void setDrawThread(DrawThread drawThread) {
        this.drawThread = drawThread;
    }

    public float getmX() {
        return mX;
    }

    public void setmX(float mX) {
        this.mX = mX;
    }

    public float getmY() {
        return mY;
    }

    public boolean isScaling() {
        return isScaling;
    }

    public void setScaling(boolean scaling) {
        isScaling = scaling;
    }

    public void setmY(float mY) {
        this.mY = mY;
    }

    public Path getmPath() {
        return mPath;
    }

    public void setmPath(Path mPath) {
        this.mPath = mPath;
    }

    public Paint getmPaint() {
        return mPaint;
    }

    public void setmPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

    /* public LinkedSet<FingerPath> getPaths() {
        return paths;
    }

    public void setPaths(LinkedSet<FingerPath> paths) {
        this.paths = paths;
    }*/

    public int getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
        getmPaint().setColor(currentColor);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        getmPaint().setStrokeWidth(strokeWidth);
    }

    public boolean isEmboss() {
        return emboss;
    }

    public void setEmboss(boolean emboss) {
        this.emboss = emboss;
    }

    public boolean isBlur() {
        return blur;
    }

    public void setBlur(boolean blur) {
        this.blur = blur;
    }

    public MaskFilter getmEmboss() {
        return mEmboss;
    }

    public void setmEmboss(MaskFilter mEmboss) {
        this.mEmboss = mEmboss;
    }

    public MaskFilter getmBlur() {
        return mBlur;
    }

    public void setmBlur(MaskFilter mBlur) {
        this.mBlur = mBlur;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmapImage(Bitmap bitmap) {
        setImageOnBitmap(bitmap);
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
        setmCanvas(new Canvas(mBitmap));
        if(getDrawThread() != null)
        getDrawThread().init(this);
    }

    public Canvas getmCanvas() {
        return mCanvas;
    }

    public void setmCanvas(Canvas mCanvas) {
        this.mCanvas = mCanvas;
    }

    public Paint getmBitmapPaint() {
        return mBitmapPaint;
    }

    public void setmBitmapPaint(Paint mBitmapPaint) {
        this.mBitmapPaint = mBitmapPaint;
    }

}
