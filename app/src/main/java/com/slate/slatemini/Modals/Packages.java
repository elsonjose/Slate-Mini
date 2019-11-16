package com.slate.slatemini.Modals;

import android.graphics.drawable.Drawable;

public class Packages {

    private String Name;
    private String Label;
    private Drawable icon;

    public Packages(String name, String label, Drawable icon) {
        Name = name;
        Label = label;
        this.icon = icon;
    }

    public String getName() {
        return Name;
    }

    public String getLabel() {
        return Label;
    }

    public Drawable getIcon() {
        return icon;
    }
}
