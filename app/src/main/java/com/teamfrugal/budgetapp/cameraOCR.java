package com.teamfrugal.budgetapp;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;

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
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ocrCamera.stopPreview();
        ocrCamera.release();
    }
}
