package com.slate.slatemini.Modals;

import android.graphics.drawable.Drawable;

public class Packages {

    private String Name;
    private String Lable;
    private Drawable icon;

    public Packages(String name, String lable, Drawable icon) {
        Name = name;
        Lable = lable;
        this.icon = icon;
    }

    public String getName() {
        return Name;
    }

    public String getLable() {
        return Lable;
    }

    public Drawable getIcon() {
        return icon;
    }
}
