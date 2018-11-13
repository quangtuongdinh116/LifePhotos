package com.forabetterlife.dtq.myunsplash.wallpaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.forabetterlife.dtq.myunsplash.R;
import com.forabetterlife.dtq.myunsplash.utils.ThemeUtils;

import java.util.ArrayList;
import java.util.List;

public class HiddenTopArrayAdapter<T> extends ArrayAdapter<T> {

    private final List<String> hints;

    public HiddenTopArrayAdapter(Context context, int resources, List<T> objects) {
        this(context, resources, objects, new ArrayList<String>());
    }

    public HiddenTopArrayAdapter(Context context, int resource, List<T> objects, List<String> hints) {
        super(context, resource, objects);
        this.hints = hints;
    }

//    @Override
//    public View getDropDownView(
//            final int position, final View convertView, @NonNull final ViewGroup parent) {
//        View v;
//
//        if (position == 0) {
//            TextView tv = new TextView(getContext());
//            tv.setHeight(0);
//            tv.setVisibility(View.GONE);
//            v = tv;
//        } else {
//            ViewGroup vg =
//                    (ViewGroup)
//                            LayoutInflater.from(getContext())
//                                    .inflate(R.layout.simple_spinner_dropdown_item, parent, false);
//
//            if (position < hints.size()) {
//                ((TextView) vg.findViewById(R.id.text)).setText(hints.get(position));
//                ((TextView) vg.findViewById(R.id.text)).setTextColor(ThemeUtils.getThemeAttrColor(getContext(), R.attr.colorAccent));
//            }
//            v = vg;
//        }
//
//        parent.setVerticalScrollBarEnabled(false);
//        return v;
//    }
}
