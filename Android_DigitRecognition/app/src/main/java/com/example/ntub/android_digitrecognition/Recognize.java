package com.example.ntub.android_digitrecognition;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Recognize {

    private static final String MODEL_PATH = "mnist.tflite";
    protected Interpreter tflite;
    private int size = 1;
    private int hole = 1;
    private int averageGray;
    private Activity activity;
    Mat imageMat;

    /** Dimensions of inputs. */
    private static final int DIM_BATCH_SIZE = 1;
    private static final int DIM_PIXEL_SIZE = 1;
    private static final int DIM_IMG_SIZE_X = 28;
    private static final int DIM_IMG_SIZE_Y = 28;
    private static final int CATEGORY_COUNT = 10;

    private ByteBuffer imgData = null;
    private final int[] imgPixels = new int[DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y];
    private final float[][] recognizeResult = new float[1][CATEGORY_COUNT];
    private int[][] result;

    public Recognize(Activity activity) throws IOException {
        this.activity = activity;
        tflite = new Interpreter(loadModelFile(activity));
        imgData = ByteBuffer.allocateDirect(
                4 * DIM_BATCH_SIZE * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE);
        imgData.order(ByteOrder.nativeOrder());
        result(activity);
    }

    private int[] result(Activity activity) throws IOException {
        Bitmap bitmap = getImageFromAssetManager(activity);

        int indexMax;
        result = new int[size][hole];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < hole; j++) {
                Bitmap cropBySizeHoleBitmap = cropBySizeHole(bitmap, i, j);
                cropBySizeHoleBitmap = getNumberInImage(cropBySizeHoleBitmap);
                convertBitmapToByteBuffer(cropBySizeHoleBitmap);
                tflite.run(imgData, recognizeResult);
                indexMax = getArrayMaxIndex(recognizeResult[0]);
                android.util.Log.e("-=-=-=-",
                        "run(): result = " + Arrays.toString(recognizeResult[0]));
                android.util.Log.e("-=-=-=-",
                        "run(): index = " + String.valueOf(indexMax));
                result[i][j] = indexMax;
            }
        }

//        convertBitmapToByteBuffer(bitmap);
//        this.mBitmap = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true);
//        tflite.run(imgData, result);
//        android.util.Log.e("-=-=-=-",
//                "run(): result = " + Arrays.toString(result[0]));
//        displayResult();
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
        InputStream is = am.open("12.png");
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) { return; }
        imgData.rewind();

        bitmap = Bitmap.createScaledBitmap(bitmap, 28, 28, true);
        bitmap.getPixels(imgPixels, 0, bitmap.getWidth(), 0, 0,
                bitmap.getWidth(), bitmap.getHeight());
        this.averageGray = averagePixelColor();
        int pixel = 0;
        for (int i = 0; i < DIM_IMG_SIZE_X; ++i) {
            for (int j = 0; j < DIM_IMG_SIZE_Y; ++j) {
//                if ( i < 2 || j < 2 || i > 25 || j > 25) {
//                    imgData.putFloat(Color.BLACK);
//                } else {
//                    imgPixels[pixel] = convertToGreyScale(imgPixels[pixel]);
//                    imgData.putFloat(imgPixels[pixel]);
//                }

//                if ( i < 1 || j < 1 || i > 26 || j > 26) {
//                    imgData.putFloat(Color.BLACK);
//                } else {
//                    imgPixels[pixel] = convertToGreyScale(imgPixels[pixel]);
//                    imgData.putFloat(imgPixels[pixel]);
//                }

                imgPixels[pixel] = invertGrey(convertToGreyScale(imgPixels[pixel]));
                imgData.putFloat(imgPixels[pixel]);
                pixel += 1;
            }
        }
    }

    private int convertToGreyScale(int color) {
        int R = Color.red(color);
        int G = Color.green(color);
        int B = Color.blue(color);
        int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);
//        android.util.Log.e("colorG", String.valueOf(gray));
        return gray;
    }

    private int invertGrey(int color) {
        return (color > this.averageGray)? Color.BLACK: Color.WHITE;
    }

    private int averagePixelColor() {
        int total = 0;
        int average = 0;
        for (int i = 0; i < DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y; i++) {
            int R = Color.red(imgPixels[i]);
            int G = Color.green(imgPixels[i]);
            int B = Color.blue(imgPixels[i]);
            int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);
            total += gray;
        }
        average = total / (DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y)-2;
        android.util.Log.e("total", String.valueOf(total));
        android.util.Log.e("average", String.valueOf(average));
        return average;
    }

    private Bitmap cropBySizeHole(Bitmap bitmap, int size, int hole) {
        int cropHeight = bitmap.getHeight() / this.size;
        int cropWidth = bitmap.getWidth() / this.hole;
        int coordinateX = cropWidth * hole;
        int coordinateY = cropHeight * size;
        android.util.Log.e("width", String.valueOf(bitmap.getWidth()));
        android.util.Log.e("Height", String.valueOf(bitmap.getHeight()));
        android.util.Log.e("cropHeight", String.valueOf(cropHeight));
        android.util.Log.e("cropWidth", String.valueOf(cropWidth));
        android.util.Log.e("coordinateX", String.valueOf(coordinateX));
        android.util.Log.e("coordinateY", String.valueOf(coordinateY));
        Bitmap cropBySizeHoleBitmap = Bitmap.createBitmap(bitmap,
                coordinateX, coordinateY, cropWidth, cropHeight);
        return cropBySizeHoleBitmap;
    }

    private Bitmap getNumberInImage(Bitmap bitmap) {
        int minX = 10000;
        int minY = 10000;
        int maxWidth = 0;
        int maxHeight = 0;

        imageMat = new Mat();
        Utils.bitmapToMat(bitmap, imageMat);

        //Blur
        Size size = new Size(11, 11);
        Imgproc.GaussianBlur(imageMat, imageMat, size, 0);

        //Canny
        Mat destination = new Mat(imageMat.rows(), imageMat.cols(), imageMat.type());
        Imgproc.Canny(imageMat, destination, 20, 120);

        //FindContours
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(destination, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            Rect r = Imgproc.boundingRect(contours.get(contourIdx));
            int rectWidth = r.width;
            int rectHeight = r.height;
            minX = (r.x < minX)? r.x: minX;
            minY = (r.y < minY)? r.y: minY;
            maxWidth = (rectWidth > maxWidth)? rectWidth: maxWidth;
            maxHeight = (rectHeight > maxHeight)? rectHeight: maxHeight;
        }

        android.util.Log.e("minX", String.valueOf(minX));
        android.util.Log.e("minY", String.valueOf(minY));
        android.util.Log.e("maxWidth", String.valueOf(maxWidth));
        android.util.Log.e("maxHeight", String.valueOf(maxHeight));

        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, minX, minY, maxWidth, maxHeight);
        return croppedBitmap;
    }

    public Bitmap getMImage() {
        Bitmap bitmap = Bitmap.createBitmap(imgPixels, 28, 28, Bitmap.Config.ARGB_8888);
        bitmap = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true);
        return bitmap;
    }

    private int getArrayMaxIndex(float[] inputArray){
        float maxValue = inputArray[0];
        int index = 0;
        for(int i=1;i < inputArray.length;i++){
            if(inputArray[i] > maxValue){
                maxValue = inputArray[i];
                index = i;
            }
        }
        return index;
    }

    public void displayResult() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < hole; j++) {
                android.util.Log.e("result" + String.valueOf(i) + String.valueOf(j), String.valueOf(result[i][j]));
            }
        }
    }

}
