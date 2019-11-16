package com.slate.slatemini.Helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class NotificationService extends NotificationListenerService {

    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context =  getApplicationContext();

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();
        String ticker = sbn.getNotification().tickerText.toString();
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();

        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("package", pack);
        msgrcv.putExtra("ticker", ticker);
        msgrcv.putExtra("title", title);
        msgrcv.putExtra("text", text);

        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);

    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
