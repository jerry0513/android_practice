package com.example.user.android_practice0131_gps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {

    static final int MIN_TIME = 5000;
    static final float MIN_dict = 0;
    LocationManager mgr;
    TextView txvLoc;
    TextView txvSetting;
    Boolean isGPSEnabled;
    boolean isNetworkEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvLoc = (TextView) findViewById(R.id.txvLoc);
        txvSetting = (TextView) findViewById(R.id.txvSetting);
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);

        checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();

        txvLoc.setText("no");

        enableLocationUpdates(true);

        String str = "GPS:" + (isGPSEnabled ? "open" : "close");
        str += "\nNetworkGPS:" + (isNetworkEnabled ? "open" : "close");
        txvSetting.setText((str));
    }

    @Override
    protected void onPause() {
        super.onPause();

        enableLocationUpdates(false);
    }

    public void onLocationChanged(Location location) {
        String str = "GPS Provider:" + location.getProvider();
        str += String.format("\n緯度:%.5f\n經度:%.5f\n高度:%.2f公尺",
                location.getLatitude(),
                location.getLongitude(),
                location.getAltitude());
        txvLoc.setText(str);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}

    public void setup(View v) {
        Intent it = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(it);
    }

    private void enableLocationUpdates(boolean isTurnOn) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (isTurnOn) {
                isGPSEnabled = mgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = mgr.isProviderEnabled((LocationManager.NETWORK_PROVIDER));
                if (!isGPSEnabled && !isNetworkEnabled) {
                    Toast.makeText(this, "請確認已開啟定位功能", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "取得定位資訊中", Toast.LENGTH_LONG).show();
                    if (isGPSEnabled)
                        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_dict, this);
                    if (isNetworkEnabled)
                        mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_dict, this);
                }
            } else {
                mgr.removeUpdates(this);
            }
        }
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        }
    }

    public void onRequestPremissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length >= 1 &&
                    grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "程式需要定位權限才能運作", Toast.LENGTH_LONG).show();
            }
        }

    }
}
