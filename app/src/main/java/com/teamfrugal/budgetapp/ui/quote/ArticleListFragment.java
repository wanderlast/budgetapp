package com.teamfrugal.budgetapp.ui.quote;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import com.teamfrugal.budgetapp.R;
import com.teamfrugal.budgetapp.database.ListContent;
import com.teamfrugal.budgetapp.dummy.DummyContent;

import java.io.File;

/**
 * Shows a list of all available quotes.
 * <p/>
 * Created by Andreas Schrade on 14.12.2015.
 */
public class ArticleListFragment extends ListFragment {

    private Callback callback = dummyCallback;
    private MyListAdapter adapter;
    private static int dt = 0;
    private String date = "0000/00/00 - 00:00:00";
    /**
     * A callback interface. Called whenever a item has been selected.
     */
    public interface Callback {
        void onItemSelected(String id);
    }

    /**
     * A dummy no-op implementation of the Callback interface. Only used when no active Activity is present.
     */
    private static final Callback dummyCallback = new Callback() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new MyListAdapter());
        adapter = (MyListAdapter) getListAdapter();
        setHasOptionsMenu(true);
        System.out.println("fragment created");
        new ListContent();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // notify callback about the selected list item
        //callback.onItemSelected(DummyContent.ITEMS.get(position).id);
        callback.onItemSelected(""+ListContent.results.get(position).id);
    }

    /**
     * onAttach(Context) is not called on pre API 23 versions of Android.
     * onAttach(Activity) is deprecated but still necessary on older devices.
     */
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /**
     * Deprecated on API 23 but still necessary for pre API 23 devices.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        if (!(context instanceof Callback)) {
            throw new IllegalStateException("Activity must implement callback interface.");
        }

        callback = (Callback) context;
    }

    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            //System.out.println(DummyContent.ITEMS.size());
            //return DummyContent.ITEMS.size();
            return ListContent.results.size();
        }

        @Override
        public Object getItem(int position) {
            //return DummyContent.ITEMS.get(position);
            return ListContent.results.get(position);
        }

        @Override
        public long getItemId(int position) {
            //return DummyContent.ITEMS.get(position).id.hashCode();
            return ListContent.results.get(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            final ListContent.Item item = (ListContent.Item) getItem(position);
            dt = 0;

            if (convertView == null) {
               //if (date.subSequence(3,5)).date;
                if (!item.date.equals("d")) {
                    System.out.println(item.date.substring(8, 10));//.substring(3, 5));
                    if (date.equals("")) {
                        System.out.println("updating date");
                        date = item.date;//.substring(8, 10);
                        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_article_date, container, false);
                        dt = 1;

                    } else if (!date.equals("") &&
                            Integer.parseInt(date.substring(8,10)) != Integer.parseInt(item.date.substring(8,10))) {
                        System.out.println("updating with new date");
                        date = item.date;//.substring(8, 10);
                        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_article_date, container, false);
                        dt = 1;

                    } else {
                        System.out.println("date is the same");
                        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_article, container, false);
                        dt = 0;
                    }

                }
                if (dt == 0) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_article, container, false);
                    System.out.println("no date");
                }
                //if (dt == 0) {
                //    dt++;
                //} else {
                  //  System.out.println("inflatinggggggggggggg");
                //}
                //convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_article_date, container, false);
            }

            //final DummyContent.DummyItem item = (DummyContent.DummyItem) getItem(position);
            ((TextView) convertView.findViewById(R.id.article_title)).setText(item.store);
            ((TextView) convertView.findViewById(R.id.article_subtitle)).setText(""+item.amount);
            if (dt == 1) {
                ((TextView) convertView.findViewById(R.id.date)).setText(item.date);
                //dt++;
            }

            final ImageView img = (ImageView) convertView.findViewById(R.id.thumbnail);

            Glide.with(getActivity()).load(item.photoId).asBitmap().fitCenter().into(new BitmapImageViewTarget(img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    img.setImageDrawable(circularBitmapDrawable);
                }
            });

            return convertView;
        }
    }

    public ArticleListFragment() {

    }
}
