package com.example.user.android_practice0126_counter;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener{
    TextView numberText;
    Button plus1Button;

    Vibrator vb;
    int counter = 0;
    boolean Is_Action = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberText = (TextView) findViewById(R.id.numberText);
        plus1Button = (Button) findViewById(R.id.plus1Button);
        plus1Button.setOnClickListener(this);
        plus1Button.setOnLongClickListener(this);
        numberText.setOnLongClickListener(this);
        numberText.setOnTouchListener(this);

    }

    public void onClick(View v){
        numberText.setText(String.valueOf(++counter));
    }

    public boolean onLongClick(View v){
        if (v.getId() == R.id.numberText ) {
            counter = 0;
            numberText.setText("0");
        }
        else{
            numberText.setText(String.valueOf(counter+=2));
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        vb = (Vibrator) getSystemService(
                Context.VIBRATOR_SERVICE
        );
        if (e.getAction() == e.ACTION_DOWN){
            vb.vibrate(5000);
        }
        else if(e.getAction() == e.ACTION_UP){
            vb.cancel();
        }
        return true;
    }
}
