package com.forabetterlife.dtq.myunsplash.about;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.forabetterlife.dtq.myunsplash.R;

public class AuthorFragment extends Fragment {

    TextView authorTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_author, container, false);
        authorTV = (TextView) view.findViewById(R.id.author);
        authorTV.setText(getString(R.string.author_explain));
        return view;
    }
}
