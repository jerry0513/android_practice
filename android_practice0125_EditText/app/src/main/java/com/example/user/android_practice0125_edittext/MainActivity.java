package com.example.user.android_practice0125_edittext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText name;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
/* 即時顯示
        name.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hello();
            }
        });
*/
    }

//    public void hello() {
//        String s = name.getText().toString();
//        textView.setText(s);
//    }

    public void hello(View v) {
        String s = name.getText().toString();
        textView.setText(s);
    }
}
