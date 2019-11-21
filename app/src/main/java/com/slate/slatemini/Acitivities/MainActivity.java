package com.slate.slatemini.Acitivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.slate.slatemini.HomeActivity;
import com.slate.slatemini.Modals.Packages;
import com.slate.slatemini.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Packages> AppPackagesList;
    private PackageManager packageManager;
    private RecyclerView MainRecyclerview;
    private EditText MainSearchEditText;
    private boolean isSearching=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppPackagesList = new ArrayList<>();
        AppPackagesList.clear();
        packageManager =  getPackageManager();

        MainSearchEditText = findViewById(R.id.main_search_edittext);

        MainRecyclerview = findViewById(R.id.main_recyclerview);
        MainRecyclerview.setHasFixedSize(true);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        MainRecyclerview.setLayoutManager(new GridLayoutManager(this,(int) (dpWidth / 80)));

        SetMenuItem("");

        MainSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                SetMenuItem(s.toString());

            }
        });

    }

    private void SetMenuItem(String searched_string)
    {
        List<Packages> AppList = new ArrayList<>();

        if(!TextUtils.isEmpty(searched_string))
        {
            isSearching = true;
            Intent mainIntent = new Intent(Intent.ACTION_MAIN,null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> paclist = packageManager.queryIntentActivities(mainIntent,0);
            for(int i=0;i<paclist.size();i++)
            {
                if(paclist.get(i).loadLabel(packageManager).toString().toLowerCase().contains(searched_string.toLowerCase()))
                {
                    AppList.add(new Packages(paclist.get(i).activityInfo.packageName,paclist.get(i).loadLabel(packageManager).toString(),paclist.get(i).loadIcon(packageManager)));

                }
            }

            MainPackagesAdapter mainPackagesAdapter = new MainPackagesAdapter(AppList);
            MainRecyclerview.setAdapter(mainPackagesAdapter);
        }
        else
        {
            final InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(MainSearchEditText.getWindowToken(),0);


            isSearching  = false;
            Intent mainIntent = new Intent(Intent.ACTION_MAIN,null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> paclist = packageManager.queryIntentActivities(mainIntent,0);
            for(int i=0;i<paclist.size();i++)
            {
                AppList.add(new Packages(paclist.get(i).activityInfo.packageName,paclist.get(i).loadLabel(packageManager).toString(),paclist.get(i).loadIcon(packageManager)));
            }

            MainPackagesAdapter mainPackagesAdapter = new MainPackagesAdapter(AppList);
            MainRecyclerview.setAdapter(mainPackagesAdapter);
        }
    }

    public class MainPackagesAdapter extends RecyclerView.Adapter<MainPackagesAdapter.MainPackagesViewHolder>
    {

        List<Packages> AppPackagesList;

        public MainPackagesAdapter(List<Packages> appPackagesList) {
            AppPackagesList = appPackagesList;
        }

        @NonNull
        @Override
        public MainPackagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View PackageSingleItem = LayoutInflater.from(getApplicationContext()).inflate(R.layout.package_single_item_layout,parent,false);
            return new MainPackagesAdapter.MainPackagesViewHolder(PackageSingleItem);
        }

        @Override
        public void onBindViewHolder(@NonNull MainPackagesViewHolder holder, final int position) {

            Glide.with(getApplicationContext()).load(AppPackagesList.get(position).getIcon()).into(holder.Appicon);
            holder.Appname.setText(AppPackagesList.get(position).getLabel());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = getPackageManager().getLaunchIntentForPackage(AppPackagesList.get(position).getName());
                    if (intent != null) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return AppPackagesList.size();
        }

        public class MainPackagesViewHolder extends RecyclerView.ViewHolder {

            ImageView Appicon;
            TextView Appname;

            public MainPackagesViewHolder(@NonNull View itemView) {
                super(itemView);

                Appicon = itemView.findViewById(R.id.package_single_item_imageview);
                Appname = itemView.findViewById(R.id.package_single_item_textview);
            }
        }
    }


    @Override
    public void onBackPressed() {
       if(isSearching)
       {
           isSearching = false;
           SetMenuItem("");

       }
       else
       {
           overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
           finish();
       }
    }
}
