package com.teamfrugal.budgetapp.camera;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;

import static org.opencv.android.Utils.matToBitmap;

// link below shows how to setup opencv, tested on phone not on emulator
// https://www.learn2crack.com/2016/03/setup-opencv-sdk-android-studio.html


// provides opencv functions and other input functions
// using openCVLibrary 3.2.0

/* 	the main activity class must have the code below in order to load openCV
	else the openCV library may not be accessible when opencv methods are called

	static final String TAG = "mainActivity";
	static {
		if(!OpenCVLoader.initDebug()){
			Log.d(TAG, "OpenCV not loaded");
			// handle this here
		} else {
			Log.d(TAG, "OpenCV loaded");
		}
	}

	run functions in thread to not hang phone
	// thread
	{
		Bitmap binaryOutput = createBinary(inputImageBitmap);
	}
*/

public final class ImgUtil {
    // values here may change
    static private final double SIGMA =  31.1;
    static private final double ALPHA = 2.7;
    static private final double BETA = -1.5;
    static private final float BLUR_TOLERANCE = 0.6f;

    // turns a bitmap into a byte array (jpeg can be changed png)
    static public byte [] bitmap2byte(Bitmap e){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        e.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    // turns a byte array into a bitmap
    static public Bitmap byte2bitmap(byte [] e){
        return BitmapFactory.decodeByteArray(e , 0, e.length);
    }

    // use this for quick preprocessing
    // function creates a binary image
    // applies grayscale, sharpening, OSTU binary thresholding
    static public Bitmap createBinary(Bitmap input){
        Mat m = new Mat(input.getWidth(), input.getHeight(), CvType.CV_8UC3);
        Utils.bitmapToMat(input, m);

        Mat gray = new Mat(input.getWidth(), input.getHeight(), CvType.CV_8UC1);
        Imgproc.cvtColor(m, gray, Imgproc.COLOR_RGB2GRAY);

        Mat sharp =  new Mat(input.getWidth(), input.getHeight(), CvType.CV_8UC1);
        Imgproc.GaussianBlur(m, sharp, new Size(0,0), SIGMA);
        Core.addWeighted(m, ALPHA, sharp, BETA, 0, sharp);

        Mat binary =  new Mat(input.getWidth(), input.getHeight(), CvType.CV_8UC1);
        Imgproc.cvtColor(sharp, sharp, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(sharp, binary, 0, 255, Imgproc.THRESH_OTSU);

        Bitmap result = input.copy(input.getConfig(), true);
        matToBitmap(binary, result);

        return result;
    }

    // grayscale input, Bitmap, or byte array
    static public Bitmap grayscale(Bitmap input){
        Mat m = new Mat(input.getWidth(), input.getHeight(), CvType.CV_8UC3);
        Utils.bitmapToMat(input, m);
        Mat gray = new Mat(input.getWidth(), input.getHeight(), CvType.CV_8UC1);
        Imgproc.cvtColor(m, gray, Imgproc.COLOR_RGB2GRAY);
        Bitmap result = input.copy(input.getConfig(), true);
        matToBitmap(gray, result);
        return result;
    }

    // sharpen input expects grayscaled input
    static public Bitmap sharpen(Bitmap input){
        Mat sharp =  new Mat(input.getWidth(), input.getHeight(), CvType.CV_8UC1);
        Mat m = new Mat(input.getWidth(), input.getHeight(), CvType.CV_8UC3);
        Utils.bitmapToMat(input, m);
        Imgproc.GaussianBlur(m, sharp, new Size(0,0), SIGMA);
        Core.addWeighted(m, ALPHA, sharp, BETA, 0, sharp);
        Bitmap result = input.copy(input.getConfig(), true);
        Utils.matToBitmap(sharp, result);
        return result;
    }


    // binary input, expects input to be in grayscale
    static public Bitmap binary(Bitmap input){
        Mat binary =  new Mat(input.getWidth(), input.getHeight(), CvType.CV_8UC1);
        Mat m = new Mat(input.getWidth(), input.getHeight(), CvType.CV_8UC1);

        Utils.bitmapToMat(input, m);
        Imgproc.cvtColor(m, m, Imgproc.COLOR_RGB2GRAY);
        try {
            Imgproc.threshold(m, binary, 0, 255, Imgproc.THRESH_OTSU);
        } catch(Exception e){
            e.printStackTrace();
        }

        Bitmap result = input.copy(input.getConfig(), true);
        Utils.matToBitmap(binary, result);
        return result;
    }

    // blur detection LAPV algorithm
    //http://stackoverflow.com/questions/7765810/is-there-a-way-to-detect-if-an-input-is-blurry/7768918#7768918
    // needs changes not fully functional, may hang phone
    static public double testBlur(Bitmap input){
        Mat m = new Mat(input.getWidth(), input.getHeight(), CvType.CV_64F);
        Utils.bitmapToMat(input, m);
        Mat test = new Mat();
        Imgproc.Laplacian(m, test, CvType.CV_64F);
        MatOfDouble mu = new MatOfDouble(), sig = new MatOfDouble();
        org.opencv.core.Core.meanStdDev(test, mu, sig);
        double t = sig.get(0,0)[0];
        return t * t;
    }
}
