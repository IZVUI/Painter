package com.example.painter;

import android.content.Context;
import android.view.ScaleGestureDetector;

class Scaler extends ScaleGestureDetector {
    public Scaler(Context context, ScaleGestureDetector.OnScaleGestureListener listener) {
        super(context, listener);
    }

    @Override
    public float getScaleFactor() {//Leave this method empty.
        return super.getScaleFactor();
    }
}