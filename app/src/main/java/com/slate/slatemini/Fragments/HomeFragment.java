package com.slate.slatemini.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.SyncStateContract;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.slate.slatemini.Acitivities.MainActivity;
import com.slate.slatemini.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homeview=inflater.inflate(R.layout.fragment_home, container, false);

        final GestureDetector gd = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){


            @Override
            public boolean onDoubleTap(MotionEvent e) {

                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);

            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {

                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }


        });

        homeview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gd.onTouchEvent(event);
            }
        });
        return homeview;
    }
}
