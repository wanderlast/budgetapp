package com.teamfrugal.budgetapp;

import android.content.Context;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by Matthew on 9/30/2016.
 */

public class cameraOCR extends SurfaceView implements SurfaceHolder.Callback{
    private Camera ocrCamera;
    private SurfaceHolder ocrHolder;

    public cameraOCR(Context context, Camera camera) {
        super(context);
        ocrCamera = camera;
        ocrCamera.setDisplayOrientation(90);
        // Install a SurfaceHolder.Callback so we get notifed when the
        // underlying surface is created and destroyed.
        ocrHolder = getHolder();
        ocrHolder.addCallback(this);
        ocrHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
         try {
            ocrCamera.setPreviewDisplay(holder);
            ocrCamera.startPreview();
        } catch (IOException e) {
            CharSequence error = "Surface unable to be created";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this.getContext(), error, duration);
            toast.show();
            //e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (ocrHolder.getSurface() == null)
            return;

        try {
            ocrCamera.stopPreview();
        } catch (Exception ex) {
            CharSequence error = "Camera not running";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this.getContext(), error, duration);
            toast.show();
        }

        try {
            ocrCamera.setPreviewDisplay(ocrHolder);
            ocrCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ocrCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                Camera.Parameters p = camera.getParameters();
                int height = p.getPreviewSize().height;
                int width = p.getPreviewSize().width;

                YuvImage y = new YuvImage(data, p.getPictureFormat(), width, height, null);


            }
        });
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ocrCamera.stopPreview();
        ocrCamera.release();
    }
}
