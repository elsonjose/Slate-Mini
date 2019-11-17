package com.slate.slatemini.Fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.slate.slatemini.R;

import java.io.IOException;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class WidgetFragment extends Fragment {

    private TextView BatteryHealthTV,BatteryPercentageTV,BatteryPlugTV,BatteryChargeTV,BatteryTempTV,BatteryTechTV,BatteryVoltageTV;
    private CardView BatteryOptimizer;
    private TextView BatteryOPTextview;

    MapView mapView;
    GoogleMap map;
    private double MainLat, MainLng;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ImageButton RefreshLocation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View widgetview =inflater.inflate(R.layout.fragment_widget, container, false);

        InitBatteryDialog(widgetview);
        loadBatterySection();

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        mapView = widgetview.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    getContext(), R.raw.map_style));

                    if (!success) {
                        Toast.makeText(getContext(), "Map rendering failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Resources.NotFoundException e) {

                }

                map = googleMap;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);

                    }
                    else
                    {
                        map.setMyLocationEnabled(true);
                        ShowMyLocation();

                    }
                }
                map.getUiSettings().setMapToolbarEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);

            }
        });

        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        BatteryOptimizer = widgetview.findViewById(R.id.battery_optimizer);
        BatteryOPTextview= widgetview.findViewById(R.id.battery_optimizer_textview);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        {
            if(Build.BRAND.equalsIgnoreCase("xiaomi") ){

                BatteryOPTextview.setText(Build.BRAND+" require auto start permission.");


            }else if(Build.BRAND.equalsIgnoreCase("Letv")){

                BatteryOPTextview.setText(Build.BRAND+" require auto start permission.");


            }
            else if(Build.BRAND.equalsIgnoreCase("Honor")){

                BatteryOPTextview.setText(Build.BRAND+" require auto start permission.");


            }
            else if(Build.BRAND.equalsIgnoreCase("vivo"))
            {
                BatteryOPTextview.setText(Build.BRAND+" require auto start permission.");

            }
            else  if (Build.MANUFACTURER.equalsIgnoreCase("oppo")) {

                BatteryOPTextview.setText(Build.BRAND+" require auto start permission.");

            }
            else
            {
                BatteryOptimizer.setVisibility(View.GONE);

            }
        }
        else
        {
            BatteryOptimizer.setVisibility(View.GONE);
        }

        BatteryOPTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + "com.slate.slatemini"));
                    getContext().startActivity(intent);
                }
            }
        });

        RefreshLocation = widgetview.findViewById(R.id.widget_maps_refreshbtn);
        RefreshLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowMyLocation();

            }
        });

        return widgetview;
    }

    private boolean IsConnectedToNet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

    }

    private boolean IsGPSEnabled() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void ShowMyLocation() {


        if (IsGPSEnabled() && IsConnectedToNet()) {

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {


                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    MainLat = latitude;
                    MainLng = longitude;

                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        List<Address> addresses =
                                geocoder.getFromLocation(latitude, longitude, 1);
                        String result = addresses.get(0).getLocality() + ":";
                        result += addresses.get(0).getCountryName();

                        LatLng latLng = new LatLng(latitude, longitude);
                        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                map.animateCamera(CameraUpdateFactory.zoomTo(15));

                            }
                        },2000);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION},
                        123);
            }
            //locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,locationListener,null);
            //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,locationListener,null);

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 60, 10, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60, 10, locationListener);

        }


    }

    private void InitBatteryDialog(View widgetview) {


        BatteryHealthTV = widgetview.findViewById(R.id.battery_dialog_health);
        BatteryPercentageTV = widgetview.findViewById(R.id.battery_dialog_percentage);
        BatteryPlugTV = widgetview.findViewById(R.id.battery_dialog_plugged);
        BatteryChargeTV = widgetview.findViewById(R.id.battery_dialog_charge);
        BatteryTechTV = widgetview.findViewById(R.id.battery_dialog_tech);
        BatteryVoltageTV = widgetview.findViewById(R.id.battery_dialog_voltage);
        BatteryTempTV = widgetview.findViewById(R.id.battery_dialog_temp);

    }

    private void loadBatterySection() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        getActivity().registerReceiver(batteryInfoReceiver, intentFilter);
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


        }

    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}
