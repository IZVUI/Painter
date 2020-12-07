package com.example.painter;

import android.graphics.Color;

import java.util.ArrayList;

public class MyColor {

    private String name;
    private int color;
    private static ArrayList<MyColor> COLORS;


    public  MyColor(String name, int color) {
        setName(name);
        setColor(color);
    }

    private static void initColors() {
        COLORS = new ArrayList<>();
        COLORS.add(new MyColor("Black", Color.BLACK));
        COLORS.add(new MyColor("Blue", Color.BLUE));
        COLORS.add(new MyColor("Green", Color.GREEN));
        COLORS.add(new MyColor("Yellow", Color.YELLOW));
        COLORS.add(new MyColor("Red", Color.RED));
        COLORS.add(new MyColor("White", Color.WHITE));
        COLORS.add(new MyColor("Gray", Color.GRAY));
    }

    public static ArrayList<MyColor> getCOLORS() {
        if(COLORS == null)
            initColors();
        return COLORS;
    }

    public static String[] getCOLORSNames() {
        if(COLORS == null)
            initColors();
        String[] names = new String[COLORS.size()];
        for(int i = 0; i < names.length; i++) {
            names[i] = COLORS.get(i).getName();
        }
        return names;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
