package com.example.painter;

import android.graphics.Matrix;
import android.view.ScaleGestureDetector;

class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener{

    private float scaleFactor = 1f;
    private DrawView dv;
    //private Matrix scaleMatrix;

    public ScaleListener(DrawView drawView) {
        super();
        dv = drawView;
        //scaleMatrix = new Matrix();
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();

        if(scaleFactor > 3) scaleFactor = 3;//Limit to your liking
        else if(scaleFactor < 0.5f) scaleFactor = 0.4f;//Limit to your liking
        scaleFactor = (scaleFactor * 100) / 100;//jitter-protection
       // scaleMatrix.setScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
        // This is for usage with a Matrix: Good for canvas and other areas where this is usable.
        // This is from my own scaling code, so I keep the matrix around in this example in case it is needed
        //dv.getmCanvas().setMatrix(scaleMatrix);
        dv.setScaleX(scaleFactor);
        dv.setScaleY(scaleFactor);
        dv.setPivotX(detector.getFocusX());
        dv.setPivotY(detector.getFocusY());
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;}

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}