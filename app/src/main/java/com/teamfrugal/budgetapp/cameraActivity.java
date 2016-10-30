package com.teamfrugal.budgetapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.IOException;

import static com.teamfrugal.budgetapp.R.id.camera;

public class cameraActivity extends AppCompatActivity {
    /** Check if this device has a camera */
    private FrameLayout preview;
    private Camera ocrCamera = null;
    private cameraOCR ocrPreview = null;
    private int flashOn = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.getBackground().setAlpha(1);


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
        setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("New Reciept");
        //toolbar.getBackground().setAlpha(0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Camera.Parameters p = ocrCamera.getParameters();
        if (id == R.id.torch) {
            if (flashOn == 1) {
                if (p.getSupportedFlashModes().contains(Camera.Parameters.FLASH_MODE_OFF)) {
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    ocrCamera.setParameters(p);
                }
                flashOn = 0;
            } else {
                if (p.getSupportedFlashModes().contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    ocrCamera.setParameters(p);
                }
                flashOn = 1;
            }
        }
        return false;
    }
}