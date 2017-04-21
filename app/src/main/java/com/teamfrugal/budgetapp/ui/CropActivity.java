package com.teamfrugal.budgetapp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.teamfrugal.budgetapp.R;
import com.teamfrugal.budgetapp.camera.OCR;
import com.teamfrugal.budgetapp.camera.OcrBox;

import java.io.IOException;

import static android.R.color.holo_green_light;
import static com.teamfrugal.budgetapp.R.color.theme_primary_light;

public class CropActivity extends AppCompatActivity {
    private ImageView mImageView;
    private OcrBox boxes;
    private Bitmap image;

    TextView sText, aText;

    int ht, wt;

    int sPressed, aPressed;

    Bitmap rotatedBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_layout);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mImageView = (ImageView) findViewById(R.id.imageview);

        String file = Environment.getExternalStorageDirectory() + "/image.jpeg";
        image = BitmapFactory.decodeFile(file);

        mImageView.setImageBitmap(image);

        OCR myOCR = new OCR(getFilesDir(), getAssets());

        sText = (TextView) this.findViewById(R.id.store);
        aText = (TextView) this.findViewById(R.id.amount);

        box b[] = new box[2];
        b[0] = new box(500,300,getResources().getColor(theme_primary_light),0);
        b[1] = new box(800,400,getResources().getColor(holo_green_light),1);//(500,300,Color.BLUE,0, 800,400,Color.GREEN,1);

        System.out.println("h: " + ht);

        ProgressBar mProgress = (ProgressBar) findViewById(R.id.pbStore);
        FloatingActionButton sButton = (FloatingActionButton) findViewById(R.id.storeCheck);
        FloatingActionButton aButton = (FloatingActionButton) findViewById(R.id.amtCheck);

        boxes = new OcrBox(this, b, image, myOCR , sText, aText, mProgress, sButton, aButton);


        addContentView(boxes, new Toolbar.LayoutParams(Toolbar.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.FILL_PARENT));

    }
}