package com.example.user.android_practice0126_radiogroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    RadioGroup ticketGroup ;
    Button btn;
    TextView txv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ticketGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btn = (Button) findViewById(R.id.button);
        txv = (TextView) findViewById(R.id.textView);
    }

    public void show(View v){
        switch (ticketGroup.getCheckedRadioButtonId()){
            case R.id.adultAdm:
                txv.setText("買全票");
                break;
            case R.id.childAdm:
                txv.setText("買半票");
                break;
            case R.id.seniorAdm:
                txv.setText("買敬老票");
                break;
        }
    }
}
