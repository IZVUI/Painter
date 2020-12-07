package com.example.painter;


import android.graphics.Path;

public class FingerPath {
    private int color;
    private boolean emboss;
    private boolean blur;
    private int strokeWidth;
    private Path path;


    public FingerPath(int color, boolean emboss, boolean blur, int strokeWidth, Path path){
        setBlur(blur);
        setColor(color);
        setEmboss(emboss);
        setPath(path);
        setStrokeWidth(strokeWidth);
    }



    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
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

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
