package com.teamfrugal.budgetapp.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamfrugal.budgetapp.R;

/**
 * Just dummy content. Nothing special.
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class DummyContent {

    /**
     * An array of sample items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample items. Key: sample ID; Value: Item.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<>(10);

    static {
        addItem(new DummyItem("1", R.drawable.p1, "Wal-mart", "11.57"));
        addItem(new DummyItem("2", R.drawable.p2, "Starbucks", "10.00"));
        addItem(new DummyItem("3", R.drawable.p3, "Paycheck", "564.00"));
        addItem(new DummyItem("4", R.drawable.p4, "Shangri-La", "15.12"));
        addItem(new DummyItem("5", R.drawable.p5, "Togos", "3.12"));
    }

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class DummyItem {
        public final String id;
        public final int photoId;
        public final String title;
        public final String author;

        public DummyItem(String id, int photoId, String title, String author) {
            this.id = id;
            this.photoId = photoId;
            this.title = title;
            this.author = author;
        }
    }
}
