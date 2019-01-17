package com.example.ntub.android_digitrecognition;

import android.app.Activity;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImagePre {

    String[][] imagePath;

    Bitmap bitmap;
    Activity activity;

    public ImagePre(Bitmap bitmap, Activity activity) {
        this.bitmap = bitmap;
        this.activity = activity;
    }

    public void cropByParPlayer(int par, int player) {
        android.util.Log.e("player", String.valueOf(player));
        android.util.Log.e("par", String.valueOf(par));
        imagePath = new String[player][par];
        int cropHeight = getHeight() / player;
        int cropWidth = getWidth() / par;
        for (int i = 0; i < player; i++) {
            for (int j = 0; j < par; j++) {
                String filename = String.valueOf(i) + String.valueOf(j) + ".png";
                int coordinateX = cropWidth * j;
                int coordinateY = cropHeight * i;
                Bitmap croppedBitmap = Bitmap.createBitmap(this.bitmap,
                        coordinateX, coordinateY, cropWidth, cropHeight);
                imagePath[i][j] = saveImage(filename, croppedBitmap);
                android.util.Log.e("imagePath", imagePath[i][j]);
            }
        }
    }

    private String saveImage(String fileName, Bitmap bitmap){
        File file = new File(activity.getApplicationContext().getFilesDir(), fileName);
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public int getHeight() {
        return this.bitmap.getHeight();
    }

    public int getWidth() {
        return this.bitmap.getWidth();
    }

    public String[][] getCroppedImagePath() { return this.imagePath; }
}
