package com.teamfrugal.budgetapp.camera;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamfrugal.budgetapp.ui.CameraActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Class:           CameraPreview
 * Desc:            Sets up all of the required Methods that handle the camera and drawing of the
 *                  camera on a surface.
 * Related Layout:  N/A
 * Called from:     cameraActivity.java
 * Calls:           OCR.java <---- Used to interface with the OCR Library. Need to refactor because
 *                                 it will no longer be called within this file.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private Camera ocrCamera;
    private SurfaceHolder ocrHolder;
    File directory;
    AssetManager assets;
    Context context;
    private int num = 0;
    private int i = 0;
    OCR ocrObject;
    boolean previewOpen;
    OcrBox box;
    int rotation;


    public CameraPreview(Context context, Camera camera, File dir, AssetManager assets) {
        super(context);
        this.ocrCamera = camera;
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        ocrHolder = getHolder();
        ocrHolder.addCallback(this);
        ocrHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        directory = dir;
        this.assets = assets;
        this.context = context;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
            if (ocrCamera != null) {
                ocrCamera.setPreviewDisplay(holder);
                ocrCamera.startPreview();
                //ocrCamera.setDisplayOrientation(270);
                Activity a = (Activity) context;


                rotation = a.getWindowManager().getDefaultDisplay().getRotation();

                int degrees = 0;
                switch (rotation) {
                    case Surface.ROTATION_0: degrees = 0; break;
                    case Surface.ROTATION_90: degrees = 90; break;
                    case Surface.ROTATION_180: degrees = 180; break;
                    case Surface.ROTATION_270: degrees = 270; break;
                }

                int result;
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    result = (info.orientation + degrees) % 360;
                    result = (360 - result) % 360;  // compensate the mirror
                } else {  // back-facing
                    result = (info.orientation - degrees + 360) % 360;
                }
                ocrCamera.setDisplayOrientation(result);


                Camera.Parameters p = ocrCamera.getParameters();
                p.setRotation((info.orientation + 360) % 360);
                p.setJpegQuality(100);
                p.setPictureSize(1920, 1080);
                //List<Camera.Size> sizes = p.getSupportedPictureSizes();

                //for (int i = 0; i < sizes.size(); i++) {
                //    System.out.println("" + i + " "+sizes.get(i).width + " x " + sizes.get(i).height);
               // }
                ocrCamera.setParameters(p);
            }
        } catch (IOException e) {
            Log.d("Camera", "Surface unable to be created");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (ocrCamera != null) {
            ocrCamera.release();
            ocrCamera = null;
        }
    }
}