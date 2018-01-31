package com.example.user.android_practice0130_accsensor;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sm;
    Sensor sr;
    TextView txv;
    ImageView igv;
    RelativeLayout layout;
    double mx = 0, my = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sr = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        txv = (TextView) findViewById(R.id.txvlno);
        igv = (ImageView) findViewById(R.id.igvMove);
        layout = (RelativeLayout) findViewById(R.id.layout);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(mx == 0){
            mx = ((layout.getWidth() - igv.getWidth()) / 20.0);
            my = ((layout.getHeight() - igv.getHeight()) / 20.0);
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) igv.getLayoutParams();
        params.leftMargin = (int) ((10 - sensorEvent.values[0]) * mx);
        params.topMargin = (int) ((10 + sensorEvent.values[1]) * my);
        igv.setLayoutParams(params);
        txv.setText(String.format("X: %1.2f, Y: %1.2f, Z: %1.2f, mx: %1.2f, my: %1.2f",
                sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2], mx, my));


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, sr, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
