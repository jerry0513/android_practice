package com.example.user.android_practice0125_linearlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText lastnameInput, firstnameInput, phonenumberInput;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastnameInput = (EditText) findViewById(R.id.lastnameInput);
        firstnameInput = (EditText) findViewById(R.id.firstnameInput);
        phonenumberInput = (EditText) findViewById(R.id.phonenumberInput);
        textView = (TextView) findViewById(R.id.textView);
    }

    public void user_phonenumber(View v){
        String lastname = lastnameInput.getText().toString();
        String firstname = firstnameInput.getText().toString();
        String phonenumber = phonenumberInput.getText().toString();
        textView.setText(lastname + firstname + "'s phone number is " + phonenumber);
    }
}
