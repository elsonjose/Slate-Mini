package com.slate.slatemini.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.slate.slatemini.Modals.NotificationMessage;
import com.slate.slatemini.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class NotificationFragment extends Fragment {

    private List<NotificationMessage> NotifMessageList;
    private RecyclerView NotifRecyclerview;
    private ImageButton ClearNotifBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View notifyview =inflater.inflate(R.layout.fragment_notification, container, false);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onNotice, new IntentFilter("Msg"));

        NotifMessageList = new ArrayList<>();
        NotifMessageList.clear();

        NotifRecyclerview = notifyview.findViewById(R.id.notify_recyclerview);
        NotifRecyclerview.setHasFixedSize(true);
        NotifRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        ClearNotifBtn = notifyview.findViewById(R.id.clear_notifs);
        ClearNotifBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotifMessageList.clear();
                NotifRecyclerview.removeAllViews();
                ClearNotifBtn.setVisibility(View.GONE);
                NotifAdapter notifAdapter = new NotifAdapter(new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>());
                NotifRecyclerview.setAdapter(notifAdapter);

            }
        });

        return notifyview;
    }

    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");

            if(!TextUtils.isEmpty(text))
            {
                if(!NotifMessageList.contains(new NotificationMessage(pack,title,text)))
                {
                    NotifMessageList.add(new NotificationMessage(pack,title,text));
                    NotifRecyclerview.removeAllViews();
                    SetNotificationRecyclerview(NotifMessageList);

                    if(NotifMessageList.size()==0)
                    {
                        ClearNotifBtn.setVisibility(View.GONE);

                    }
                    else
                    {
                        ClearNotifBtn.setVisibility(View.VISIBLE);
                    }

                }
            }
        }
    };

    private void SetNotificationRecyclerview(List<NotificationMessage> notifMessageList) {


        List<String> pack=new ArrayList<>();
        List<String> title=new ArrayList<>();
        List<String> text=new ArrayList<>();
        List<String> time = new ArrayList<>();

        for(int i=0;i<notifMessageList.size();i++)
        {
            if(!notifMessageList.get(i).getAppPackage().toLowerCase().contains(notifMessageList.get(i).getTitle().toLowerCase()))
            {
                if(!TextUtils.isEmpty(notifMessageList.get(i).getText()))
                {
                    if(!title.contains(notifMessageList.get(i).getTitle()))
                    {
                        pack.add(notifMessageList.get(i).getAppPackage());
                        text.add(notifMessageList.get(i).getText());
                        title.add(notifMessageList.get(i).getTitle());
                        time.add(String.valueOf(System.currentTimeMillis()));

                        Collections.reverse(pack);
                        Collections.reverse(text);
                        Collections.reverse(title);

                        NotifAdapter notifAdapter = new NotifAdapter(pack,text,title,time);
                        NotifRecyclerview.setAdapter(notifAdapter);

                    }
                    else
                    {
                        text.set(getPostion(title,notifMessageList.get(i).getTitle()),notifMessageList.get(i).getText());
                        NotifAdapter notifAdapter = new NotifAdapter(pack,text,title,time);
                        NotifRecyclerview.setAdapter(notifAdapter);
                    }
                }
            }


        }




    }

    private int getPostion(List<String> title, String title1) {

        for(int i=0;i<title.size();i++)
        {
            if(title.get(i).equals(title1))
                return i;
        }

        return 0;
    }

    public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.NotifViewHolder>
    {
        List<String> Package,Text,Title,Time;


        public NotifAdapter(List<String> aPackage, List<String> text, List<String> title, List<String> time) {
            Package = aPackage;
            Text = text;
            Title = title;
            Time = time;
        }

        @NonNull
        @Override
        public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View notifsingleitemview = LayoutInflater.from(getContext()).inflate(R.layout.notification_single_item_layout,parent,false);
            return new NotifAdapter.NotifViewHolder(notifsingleitemview);
        }

        @Override
        public void onBindViewHolder(@NonNull NotifViewHolder holder, int position) {

            holder.Title.setText(Title.get(position));
            holder.Text.setText(Text.get(position));
            long time = Long.parseLong(Time.get(position));
            CharSequence Time = DateUtils.getRelativeDateTimeString(getContext(), time, DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);
            String timesubstring = Time.toString().substring(Time.length()-8);
            Date date = new Date(time);
            String dateformat = DateFormat.format("dd-MMM-yyyy",date).toString();
            holder.Time.setText(dateformat+", "+timesubstring);

        }

        @Override
        public int getItemCount() {
            return Title.size();
        }

        public class NotifViewHolder extends RecyclerView.ViewHolder {

            TextView Title,Text,Time;

            public NotifViewHolder(@NonNull View itemView) {
                super(itemView);

                Title = itemView.findViewById(R.id.notif_title);
                Text = itemView.findViewById(R.id.notif_text);
                Time = itemView.findViewById(R.id.notif_time);
            }
        }
    }

}
