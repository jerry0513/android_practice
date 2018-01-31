package com.example.user.android_practice0129_intent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Main2Activity extends AppCompatActivity {

    Intent it;
    TextView txv;
    EditText edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        txv = (TextView) findViewById(R.id.textView);
        edt = (EditText) findViewById(R.id.editText);

        it = getIntent();
        int no = it.getIntExtra("編號", 0);
        String s = it.getStringExtra("備忘");

        txv.setText(s.substring(0, 2));

        if (s.length() > 3) {
            edt.setText(s.substring(3));
        }
    }

    public void onCancel(View v){
        finish();
        setResult(RESULT_CANCELED);
    }

    public void onSave(View v){
        Intent it2 = new Intent();
        it2.putExtra("備忘", txv.getText() + " " + edt.getText());
        setResult(RESULT_OK, it2);
        finish();

    }
}
