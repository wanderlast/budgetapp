package com.teamfrugal.budgetapp.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.teamfrugal.budgetapp.R;
import com.teamfrugal.budgetapp.camera.OCR;
import com.teamfrugal.budgetapp.camera.OcrBox;

public class CropActivity extends AppCompatActivity {
    private ImageView mImageView;
    private OcrBox boxes;
    private Bitmap image;
    TextView sText, aText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_layout);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        mImageView = (ImageView) findViewById(R.id.imageview);
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inSampleSize=2;
        image = Bitmap.createBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.jpeg", op));
        mImageView.setImageBitmap(image);

        OCR myOCR = new OCR(getFilesDir(), getAssets());

        sText = (TextView) this.findViewById(R.id.store);
        aText = (TextView) this.findViewById(R.id.amount);

        box b[] = new box[2];
        b[0] = new box(500,300,Color.BLUE,0);
        b[1] = new box(800,400,Color.GREEN,1);//(500,300,Color.BLUE,0, 800,400,Color.GREEN,1);

        boxes = new OcrBox(this, b, image, myOCR , sText, aText);

        addContentView(boxes, new Toolbar.LayoutParams(Toolbar.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.FILL_PARENT));

    }
}