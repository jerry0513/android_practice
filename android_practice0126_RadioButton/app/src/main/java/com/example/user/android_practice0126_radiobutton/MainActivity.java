package com.example.user.android_practice0126_radiobutton;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, TextWatcher{
    RadioGroup unit;
    EditText value;
    TextView degF;
    TextView degC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unit = (RadioGroup) findViewById(R.id.unit);
        unit.setOnCheckedChangeListener(this);

        value = (EditText) findViewById(R.id.value);
        value.addTextChangedListener(this);

        degF = (TextView) findViewById(R.id.degF);
        degC = (TextView) findViewById(R.id.degC);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        calc();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        calc();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void calc() {
        double val, f, c;
        String str = value.getText().toString();
        try {
            val = Double.parseDouble(str);
        } catch (Exception e){
            val = 0;
        }

        if (unit.getCheckedRadioButtonId() == R.id.unitF){
            f = val;
            c = (f-32)*5/9;
        }else{
            c = val;
            f = c*9/5+32;
        }

        degC.setText(String.format("%.1f", c) + getResources().getString(R.string.charC));
        degF.setText(String.format("%.1f", f) + getResources().getString(R.string.charF));
    }
}

