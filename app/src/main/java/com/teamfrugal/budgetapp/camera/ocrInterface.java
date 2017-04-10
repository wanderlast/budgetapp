package com.teamfrugal.budgetapp.camera;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.teamfrugal.budgetapp.R;
import com.teamfrugal.budgetapp.dummy.DummyContent;
import com.teamfrugal.budgetapp.ui.CameraActivity;
import com.teamfrugal.budgetapp.ui.CropActivity;
import com.teamfrugal.budgetapp.ui.base.BaseActivity;
import com.teamfrugal.budgetapp.ui.quote.ArticleDetailActivity;
import com.teamfrugal.budgetapp.ui.quote.ArticleDetailFragment;
import com.teamfrugal.budgetapp.ui.quote.ArticleListFragment;
import com.teamfrugal.budgetapp.ui.quote.ListActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import static com.teamfrugal.budgetapp.dummy.DummyContent.addItem;

/**
 * Created by Matthew on 3/23/2017.
 */


class results {
    public int confidence;
    public String result;
    results() {}
}

public class ocrInterface extends AsyncTask<Object, Void, results> {
    private TessBaseAPI mTess;
    private Bitmap bitmap;
    private int i;
    private String result;
    private int maxConfidence;
    private TextView text;
    private TextView sText;
    private TextView aText;
    private Context c;

    ocrInterface(Context cc, TessBaseAPI mTess, Bitmap bitmap, int i, TextView text, TextView sText, TextView aText) {

        this.c = cc;
        this.mTess = mTess;
        this.bitmap = bitmap;
        this.i = i;
        this.text = text;
        this.sText = sText;
        this.aText = aText;
    }

    @Override
    protected results doInBackground(Object[] params) {
        mTess.setImage(bitmap);

        if (i == 0) {
            result = mTess.getUTF8Text();
            maxConfidence = mTess.meanConfidence();
        } else if (mTess.meanConfidence() > maxConfidence) {
            maxConfidence = mTess.meanConfidence();
            result = mTess.getUTF8Text();
        }
        results r = new results();
        r.confidence = maxConfidence;
        r.result = result;

        return r;
    }

    @Override
    protected void onPostExecute(results r) {
        if (i==4) {
            text.setText(r.result + "[" + r.confidence + "]");
        }
//        if (Integer.parseInt(sText.getText().subSequence(sText.length()-3, sText.length()-1).toString()) > 70 &&
  //              aText.getText() != "") {
        if (aText.getText() != "" && sText.getText() != "") {
            System.out.println("ADDING!!!!");
            addItem(new DummyContent.DummyItem("6", R.drawable.p5, sText.getText().toString(), aText.getText().toString()));
            c.startActivity(new Intent(c, ListActivity.class));
            ((Activity)c).finish();
        }
    }
}

