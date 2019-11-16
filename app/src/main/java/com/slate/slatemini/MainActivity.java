package com.slate.slatemini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.slate.slatemini.Modals.Packages;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Dialog BatteryDialog;
    private TextView BatteryHealthTV,BatteryPercentageTV,BatteryPlugTV,BatteryChargeTV,BatteryTempTV,BatteryTechTV,BatteryVoltageTV;
    private List<Packages> AppPackagesList;
    private PackageManager packageManager;
    private RecyclerView MainRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitBatteryDialog();
        AppPackagesList = new ArrayList<>();
        AppPackagesList.clear();
        packageManager =  getPackageManager();

        MainRecyclerview = findViewById(R.id.main_recyclerview);
        MainRecyclerview.setHasFixedSize(true);
        MainRecyclerview.setLayoutManager(new GridLayoutManager(this,5));

        Intent mainIntent = new Intent(Intent.ACTION_MAIN,null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> paclist = packageManager.queryIntentActivities(mainIntent,0);
        for(int i=0;i<paclist.size();i++)
        {
            AppPackagesList.add(new Packages(paclist.get(i).activityInfo.packageName,paclist.get(i).loadLabel(packageManager).toString(),paclist.get(i).loadIcon(packageManager)));
        }


        /*

                loadBatterySection();
                BatteryDialog.show();
         */
    }

    private void InitBatteryDialog() {

        BatteryDialog = new Dialog(this);
        BatteryDialog.setContentView(R.layout.battery_dialog_layout);
        BatteryDialog.setCancelable(true);
        BatteryDialog.setCanceledOnTouchOutside(true);
        BatteryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        BatteryHealthTV = BatteryDialog.findViewById(R.id.battery_dialog_health);
        BatteryPercentageTV = BatteryDialog.findViewById(R.id.battery_dialog_percentage);
        BatteryPlugTV = BatteryDialog.findViewById(R.id.battery_dialog_plugged);
        BatteryChargeTV = BatteryDialog.findViewById(R.id.battery_dialog_charge);
        BatteryTechTV = BatteryDialog.findViewById(R.id.battery_dialog_tech);
        BatteryVoltageTV = BatteryDialog.findViewById(R.id.battery_dialog_voltage);
        BatteryTempTV = BatteryDialog.findViewById(R.id.battery_dialog_temp);

    }

    public class MainPackagesAdapter extends RecyclerView.Adapter<MainPackagesAdapter.MainPackagesViewHolder>
    {

        @NonNull
        @Override
        public MainPackagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull MainPackagesViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public class MainPackagesViewHolder extends RecyclerView.ViewHolder {
            public MainPackagesViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

    private void loadBatterySection() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(batteryInfoReceiver, intentFilter);
    }

    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateBatteryData(intent);
        }
    };

    private void updateBatteryData(Intent intent) {
        boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);

        if (present) {
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            int healthLbl = -1;

            switch (health) {
                case BatteryManager.BATTERY_HEALTH_COLD:
                    healthLbl = R.string.battery_health_cold;
                    break;

                case BatteryManager.BATTERY_HEALTH_DEAD:
                    healthLbl = R.string.battery_health_dead;
                    break;

                case BatteryManager.BATTERY_HEALTH_GOOD:
                    healthLbl = R.string.battery_health_good;
                    break;

                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    healthLbl = R.string.battery_health_over_voltage;
                    break;

                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    healthLbl = R.string.battery_health_overheat;
                    break;

                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    healthLbl = R.string.battery_health_unspecified_failure;
                    break;

                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                default:
                    break;
            }

            if (healthLbl != -1) {
                // display battery health ...
                //Toast.makeText(this, "Health : " + getString(healthLbl), Toast.LENGTH_SHORT).show();
                BatteryHealthTV.setText(getString(healthLbl));
            }

            // Calculate Battery Pourcentage ...
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            if (level != -1 && scale != -1) {
                int batteryPct = (int) ((level / (float) scale) * 100f);
                //Toast.makeText(this, "Battery Pct : " + batteryPct + " %", Toast.LENGTH_SHORT).show();
                BatteryPercentageTV.setText("Battery Percentage : " + batteryPct + " %");
            }

            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            int pluggedLbl = R.string.battery_plugged_none;

            switch (plugged) {
                case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                    pluggedLbl = R.string.battery_plugged_wireless;
                    break;

                case BatteryManager.BATTERY_PLUGGED_USB:
                    pluggedLbl = R.string.battery_plugged_usb;
                    break;

                case BatteryManager.BATTERY_PLUGGED_AC:
                    pluggedLbl = R.string.battery_plugged_ac;
                    break;

                default:
                    pluggedLbl = R.string.battery_plugged_none;
                    break;
            }

            // display plugged status ...
            //Toast.makeText(this,"Plugged : " + getString(pluggedLbl), Toast.LENGTH_SHORT).show();
            BatteryPlugTV.setText(getString(pluggedLbl));

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int statusLbl = R.string.battery_status_discharging;

            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    statusLbl = R.string.battery_status_charging;
                    break;

                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    statusLbl = R.string.battery_status_discharging;
                    break;

                case BatteryManager.BATTERY_STATUS_FULL:
                    statusLbl = R.string.battery_status_full;
                    break;

                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    statusLbl = -1;
                    break;

                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                default:
                    statusLbl = R.string.battery_status_discharging;
                    break;
            }

            if (statusLbl != -1) {
                //Toast.makeText(this, "Battery Charging Status : " + getString(statusLbl), Toast.LENGTH_SHORT).show();
                BatteryChargeTV.setText(getString(statusLbl));
            }

            if (intent.getExtras() != null) {
                String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);

                if (!"".equals(technology)) {
                    //Toast.makeText(this, "Technology : " + technology, Toast.LENGTH_SHORT).show();
                    BatteryTechTV.setText("Technology : " + technology);
                }
            }

            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);

            if (temperature > 0) {
                float temp = ((float) temperature) / 10f;
                //Toast.makeText(this,"Temperature : " + temp + "°C", Toast.LENGTH_SHORT).show();
                BatteryTempTV.setText("Temperature : " + temp + "°C");
            }

            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);

            if (voltage > 0) {
                //Toast.makeText(this,"Voltage : " + voltage + " mV", Toast.LENGTH_SHORT).show();
                BatteryVoltageTV.setText("Voltage : " + voltage + " mV");
            }

            long capacity = getBatteryCapacity(this);

        }

    }

    public long getBatteryCapacity(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager mBatteryManager = (BatteryManager) ctx.getSystemService(Context.BATTERY_SERVICE);
            Long chargeCounter = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            Long capacity = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            if (chargeCounter != null && capacity != null) {
                long value = (long) (((float) chargeCounter / (float) capacity) * 100f);
                return value;
            }
        }

        return 0;
    }
}
