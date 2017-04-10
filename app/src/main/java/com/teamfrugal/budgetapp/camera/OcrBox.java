package com.teamfrugal.budgetapp.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.teamfrugal.budgetapp.R;
import com.teamfrugal.budgetapp.dummy.DummyContent;
import com.teamfrugal.budgetapp.ui.CropActivity;
import com.teamfrugal.budgetapp.ui.box;
import com.teamfrugal.budgetapp.ui.quote.ArticleListFragment;
import com.teamfrugal.budgetapp.ui.quote.ListActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static android.view.MotionEvent.INVALID_POINTER_ID;
import static com.teamfrugal.budgetapp.dummy.DummyContent.addItem;

/**
 * Class:           OcrBox.java
 * Desc:            This class is used to draw a box on the screen and will support functions to
 *                  move and scale the box. Currently only move is implemented, but it has some
 *                  bugs that need to be worked out.
 * Related Layout:  N/A
 * Called from:     cameraActivity.java <---- Need to remove from cameraActivity.java and put inside
 *                                            cropImage.java instead.
 * Calls:           N/A
 */

public class OcrBox extends View {
    private Paint b[] = new Paint[2];
    private static int offsetX[]=new int[2];
    private static int offsetY[]=new int[2];
    private float mLastTouchX=0;
    private float mLastTouchY=0;
    private int mActivePointerId=INVALID_POINTER_ID;
    private int leftx[]=new int[2], rightx[]=new int[2], bottomy[]=new int[2], topy[]= new int[2];
    private int centerX[] = new int[2], centerY[]=new int[2];
    private File loc;
    private Bitmap image;
    private int active=-1;
    private int scale = 0;
    TextView sText, aText;
    OCR myOCR;
    int pos[][]=new int[2][2];

    private box boxes[] = new box[2];


    public OcrBox(CropActivity context, box[] box, Bitmap image, OCR myOCR, TextView sText, TextView aText) {
        super(context);
        b[0] = new Paint();
        b[1] = new Paint();

        this.boxes = box;

        this.image = image;

        this.myOCR = myOCR;

        this.sText = sText;
        this.aText = aText;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case MotionEvent.ACTION_DOWN: {

                //if (finished) {
                //    ArticleListFragment.class.not
               // }

                final int pIndex = MotionEventCompat.getActionIndex(event);
                System.out.println("pIndex: " + pIndex);
                final float x = MotionEventCompat.getX(event, pIndex);
                final float y = MotionEventCompat.getY(event, pIndex);

                if (x > leftx[0] && x < rightx[0] && y < topy[0] && y > bottomy[0]) {
                    mLastTouchX = x;
                    mLastTouchY = y;

                    mActivePointerId = MotionEventCompat.getPointerId(event, pIndex);
                    active=0;
                } else if (x > leftx[1] && x < rightx[1] && y < topy[1] && y > bottomy[1]) {
                    mLastTouchX = x;
                    mLastTouchY = y;

                    mActivePointerId = MotionEventCompat.getPointerId(event, pIndex);
                    active=1;
                } else{
                    active=-1;
                    return false;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);

                final float x = MotionEventCompat.getX(event, pIndex);
                final float y = MotionEventCompat.getY(event, pIndex);

                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                if (active != -1) {
                    offsetX[active] += (int) dx;
                    offsetY[active] += (int) dy;
                }
                invalidate();

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                applyOCR();
                active=-1;
            }
        }

        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < 2; i++) {
            if (boxes[i].once == 0) {
                pos[i][0] = boxes[i].origin[0];
                pos[i][1] = boxes[i].origin[1];

                leftx[i] = pos[i][0] - canvas.getWidth() / 8;
                rightx[i] = pos[i][0] + canvas.getWidth() / 8;
                topy[i] = pos[i][1] + canvas.getHeight() / 30;
                bottomy[i] = pos[i][1] - canvas.getHeight() / 30;
                boxes[i].once++;
            }

            super.onDraw(canvas);

            b[i].setStyle(Paint.Style.STROKE);
            b[i].setColor(boxes[i].color);
            b[i].setStrokeWidth(10);


            //center of ocrBox
            if (leftx[i] + offsetX[i] / 16 > 0 && rightx[i] + offsetX[i] / 16 < canvas.getWidth()) {
                centerX[i] = pos[i][0] + offsetX[i];
                leftx[i] = centerX[i] - canvas.getWidth() / 8;
                rightx[i] = centerX[i] + canvas.getWidth() / 8;
            }
            if (topy[i] + offsetY[i] / 16 < canvas.getHeight() - 225 && bottomy[i] + offsetY[i] / 16 > 40) {
                centerY[i] = pos[i][1] + offsetY[i];
                topy[i] = centerY[i] + canvas.getHeight() / 30 - 20;
                bottomy[i] = centerY[i] - canvas.getHeight() / 30;
            }

            //corners of ocrBox
            //Top left
            canvas.drawLine(leftx[i], bottomy[i], leftx[i] + 50, bottomy[i], b[i]);
            canvas.drawLine(leftx[i], bottomy[i] - 5, leftx[i], bottomy[i] + 50, b[i]);

            //Top right
            canvas.drawLine(rightx[i], bottomy[i], rightx[i] - 50, bottomy[i], b[i]);
            canvas.drawLine(rightx[i], bottomy[i] - 5, rightx[i], bottomy[i] + 50, b[i]);

            //Bottom left
            canvas.drawLine(leftx[i], topy[i], leftx[i] + 50, topy[i], b[i]);
            canvas.drawLine(leftx[i], topy[i] + 5, leftx[i], topy[i] - 50, b[i]);

            //Bottom right
            canvas.drawLine(rightx[i], topy[i], rightx[i] - 50, topy[i], b[i]);
            canvas.drawLine(rightx[i], topy[i] + 5, rightx[i], topy[i] - 50, b[i]);

            System.out.println("Cx: " + centerX[i] + "   Cy: " + centerY[i]);
            System.out.println("left: " + leftx[i] + "  Right: " + rightx[i]);
            System.out.println("top: " + topy[i] + "Bottom: " + bottomy[i]);

        }
    }

    Random rand = new Random();
    void applyOCR() {
        //System.out.println("origin: { " + leftx + ", " + rightx);

        Bitmap storeClip = null;
        for (int i = 0; i < 5; i++) {
            int r = rand.nextInt(5);
            if (r == 0)
                storeClip = Bitmap.createBitmap(image, leftx[active] / 2, (bottomy[active]+150) / 2, (rightx[active] - leftx[active]) / 2, (topy[active] - bottomy[active]) / 2);
            else if (r == 1)
                storeClip = Bitmap.createBitmap(image, leftx[active] / 2 + 5, (bottomy[active]+150) / 2, (rightx[active] - leftx[active]) / 2, (topy[active] - bottomy[active]) / 2);
            else if (r == 2)
                storeClip = Bitmap.createBitmap(image, leftx[active] / 2, (bottomy[active]+150) / 2 + 5, (rightx[active] - leftx[active]) / 2, (topy[active] - bottomy[active]) / 2);
            else if (r == 3)
                storeClip = Bitmap.createBitmap(image, leftx[active] / 2, (bottomy[active]+150) / 2, (rightx[active] - leftx[active]) / 2 + 5, (topy[active] - bottomy[active]) / 2);
            else
                storeClip = Bitmap.createBitmap(image, leftx[active] / 2, (bottomy[active]+150) / 2, (rightx[active] - leftx[active]) / 2, (topy[active] - bottomy[active]) / 2 + 5);

            int width = storeClip.getWidth();
            int height = storeClip.getHeight();

            File f = new File(Environment.getExternalStorageDirectory() + "/sub" + active + ".jpeg");
            try {
                f.createNewFile();
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                storeClip.compress(Bitmap.CompressFormat.JPEG, 100, b);
                FileOutputStream ff = new FileOutputStream(f);
                ff.write(b.toByteArray());
                ff.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            TessBaseAPI mTess = myOCR.getApi();

            TextView text;

            if (active == 0) {
                text = sText;
            } else {
                text = aText;
            }
            new ocrInterface(getContext(), mTess, storeClip,i,text, sText, aText).execute();


        }
    }
}