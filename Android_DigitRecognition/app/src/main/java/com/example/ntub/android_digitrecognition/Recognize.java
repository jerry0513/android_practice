package com.example.ntub.android_digitrecognition;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.lang.Object;
import java.util.Collections;
import java.util.List;


public class Recognize {

    private static final String MODEL_PATH = "mnist.tflite";

    protected Interpreter tflite;
    Bitmap mBitmap;

    /** Dimensions of inputs. */
    private static final int DIM_BATCH_SIZE = 1;
    private static final int DIM_PIXEL_SIZE = 1;
    private static final int DIM_IMG_SIZE_X = 28;
    private static final int DIM_IMG_SIZE_Y = 28;
    private static final int CATEGORY_COUNT = 10;

    private ByteBuffer imgData = null;
    private final int[] imgPixels = new int[DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y];
    private final float[][] result = new float[1][CATEGORY_COUNT];

    public Recognize(Activity activity) throws IOException {
        tflite = new Interpreter(loadModelFile(activity));
        imgData = ByteBuffer.allocateDirect(
                4 * DIM_BATCH_SIZE * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE);
        imgData.order(ByteOrder.nativeOrder());
        result(activity);
    }

    private float[] result(Activity activity) throws IOException {
//        Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
        Bitmap bitmap = getImageFromAssetManager(activity);
        ImagePre imagePre = new ImagePre(bitmap, activity);
        imagePre.cropByParPlayer(9, 2);
        String[][] croppedImagePath = imagePre.getCroppedImagePath();
        convertBitmapToByteBuffer(getCroppedImagePath(croppedImagePath));
//        convertBitmapToByteBuffer(bitmap);
        this.mBitmap = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true);
        tflite.run(imgData, result);
        android.util.Log.e("-=-=-=-",
                "run(): result = " + Arrays.toString(result[0]));
        return result[0];
    }

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private Bitmap getImageFromAssetManager(Activity activity) throws IOException {
        AssetManager am = activity.getAssets();
        InputStream is = am.open("score2.jpg");
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }

        imgData.rewind();
        bitmap = Bitmap.createScaledBitmap(bitmap, 28, 28, true);
        bitmap.getPixels(imgPixels, 0, bitmap.getWidth(),
                0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < DIM_IMG_SIZE_X; ++i) {
            for (int j = 0; j < DIM_IMG_SIZE_Y; ++j) {
                final int val = imgPixels[pixel++];
                imgData.putFloat(convertToGreyScale(val));
            }
        }
    }

    private float convertToGreyScale(int color) {
        int A = Color.alpha(color);
        int R = Color.red(color);
        int G = Color.green(color);
        int B = Color.blue(color);
        int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);
        gray = (gray > 128)? 255: 0;
        int negative = Math.abs(255-gray);

        return negative;
    }

    public Bitmap getCroppedImagePath(String[][] path) {
        String[][] imgPath = path;
        File imgFile = new File(imgPath[0][0]);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        android.util.Log.e("path-=-=-=-", imgPath[0][0]);
        return myBitmap;
    }

    public Bitmap getMImage() {
        return mBitmap;
    }


}
