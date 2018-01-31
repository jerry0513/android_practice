package com.example.user.android_practice0125_changecolor;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView txvR, txvG, txvB;
    View colorBlock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvR = (TextView) findViewById(R.id.redText);
        txvG = (TextView) findViewById(R.id.greenText);
        txvB = (TextView) findViewById(R.id.blueText);
        colorBlock = (View) findViewById(R.id.colorBlock);
    }

    public void changeColor(View v){
        Random x = new Random();
        int red = x.nextInt(256);
        txvR.setText("Red : " + red);
        txvR.setTextColor(Color.rgb(red,0,0));

        int green = x.nextInt(256);
        txvG.setText("Green : " + green);
        txvG.setTextColor(Color.rgb(0,green,0));

        int blue = x.nextInt(256);
        txvB.setText("Blue : " + blue);
        txvB.setTextColor(Color.rgb(0,0,blue));

        colorBlock.setBackgroundColor(Color.rgb(red,green,blue));
    }
}