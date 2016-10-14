package com.teamfrugal.budgetapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import static com.teamfrugal.budgetapp.R.id.camera;

public class cameraActivity extends AppCompatActivity {
    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        }
        else {
            return false;
        }
    }

    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        }
        catch (Exception e) {

        }
        return camera;
    }
    private FrameLayout preview;
    private Camera ocrCamera = null;
    private cameraOCR ocrPreview = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        try {
            ocrCamera = Camera.open();
        } catch (Exception e) {
            //
        }

        if (ocrCamera != null) {
            CharSequence success = "Camera initialized";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, success, duration);
            toast.show();
            Camera.Parameters p = ocrCamera.getParameters();

            // So no exceptions are thrown
            if (p.getSupportedFocusModes().contains(
                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            ocrCamera.setParameters(p);
            ocrPreview = new cameraOCR(this, ocrCamera);
            preview = (FrameLayout) findViewById(R.id.content_camera);
            preview.addView(ocrPreview);
        }
        else {
            CharSequence error = "Camera unable to be initialized";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, error, duration);
            toast.show();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Reciept");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
