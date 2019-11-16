package com.slate.slatemini.Modals;

public class NotificationMessage {

    private String AppPackage,Title,Text;

    public NotificationMessage(String appPackage, String title, String text) {
        AppPackage = appPackage;
        Title = title;
        Text = text;
    }

    public String getAppPackage() {
        return AppPackage;
    }

    public void setAppPackage(String appPackage) {
        AppPackage = appPackage;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
