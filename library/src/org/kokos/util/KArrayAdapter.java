package org.kokos.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Fix ListAdapter by providing a default for the list item resource
 */
public class KArrayAdapter<T> extends android.widget.ArrayAdapter<T> {

    public KArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public KArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public KArrayAdapter(Context context, int resource, T[] objects) {
        super(context, resource, objects);
    }

    public KArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public KArrayAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
    }

    public KArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public KArrayAdapter(Context context, List<T> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }
}
