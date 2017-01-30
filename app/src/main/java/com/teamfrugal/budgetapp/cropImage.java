package com.teamfrugal.budgetapp;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

/**
 * Created by Matthew on 1/29/2017.
 */

public class cropImage extends AppCompatActivity{
    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mImageView = (ImageView) findViewById(R.id.imageview);
        mImageView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpeg"));

        setSupportActionBar(toolbar);

    }
}
