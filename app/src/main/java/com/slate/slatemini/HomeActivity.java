package com.slate.slatemini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.slate.slatemini.Helper.HomeFragmentAdapter;
import com.slate.slatemini.Helper.HomeWatcher;
import com.slate.slatemini.Helper.NotificationService;
import com.slate.slatemini.Interface.OnHomePressedListener;
import com.slate.slatemini.Modals.Packages;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    private ViewPager HomeViewPager;
    private HomeFragmentAdapter HomeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        HomeViewPager = findViewById(R.id.home_viewpager);
        HomeAdapter = new HomeFragmentAdapter(getSupportFragmentManager(),0);
        HomeViewPager.setAdapter(HomeAdapter);
        HomeViewPager.setCurrentItem(1);

        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                HomeViewPager.setCurrentItem(1);

            }
        });
        mHomeWatcher.startWatch();

        /*
        Intent serviceintent = new Intent(this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 234324243, serviceintent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (3000), pendingIntent);
         */


    }


}
