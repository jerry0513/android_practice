package com.example.user.android_practice0129_intent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    ListView lv;
    String aMemo[] = {"1.", "2.", "3.", "4.", "5.", "6."};
    ArrayAdapter<String> aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView)findViewById(R.id.listView);
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aMemo);

        lv.setAdapter(aa);
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
        Intent it = new Intent(this, Main2Activity.class);
        it.putExtra("編號", pos+1);
        it.putExtra("備忘", aMemo[pos]);
        startActivityForResult(it, pos);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
        aMemo[pos] = (pos+1)+".";
        aa.notifyDataSetChanged();
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent it){
        if (resultCode == RESULT_OK){
            aMemo[requestCode] = it.getStringExtra("備忘");
            aa.notifyDataSetChanged();
        }
    }

}
