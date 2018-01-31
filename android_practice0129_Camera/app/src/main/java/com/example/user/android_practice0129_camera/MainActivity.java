package com.example.user.android_practice0129_camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onGet(View v){
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(it, 100);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && resultCode == 100) {
            ImageView imv = (ImageView) findViewById(R.id.imageView);
            Bundle extras = data.getExtras();

            Bitmap bmp = (Bitmap) extras.get("data");
            imv.setImageBitmap(bmp);
        }else{
            Toast.makeText(this, "no picture", Toast.LENGTH_LONG).show();
        }
    }
}
