package com.slate.slatemini;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.slate.slatemini.Helper.HomeFragmentAdapter;
import com.slate.slatemini.Helper.HomeWatcher;
import com.slate.slatemini.Interface.OnHomePressedListener;

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
        HomeViewPager.setCurrentItem(0);

        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                HomeViewPager.setCurrentItem(0);

            }
        });

        mHomeWatcher.startWatch();


    }

    @Override
    public void onBackPressed() {
        if(HomeViewPager.getCurrentItem()==0)
        {
            Toast.makeText(this, "cant go back.", Toast.LENGTH_SHORT).show();
        }
        else if(HomeViewPager.getCurrentItem()!=0)
        {
            HomeViewPager.setCurrentItem(0);
        }
        else
        {
              super.onBackPressed();

        }

    }
}
