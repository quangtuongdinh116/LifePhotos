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

public class AboutAppFragment extends Fragment {

    TextView intro;
    TextView wallpaper;
    TextView favorite;
    TextView wanted;
    TextView wallpaperexplain;
    TextView favoriteexplain;
    TextView wantedexplain;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_app, container, false);
        intro = (TextView) view.findViewById(R.id.introduction);
        wallpaper = (TextView) view.findViewById(R.id.wallpaper_label_section);
        favorite = (TextView) view.findViewById(R.id.favorite_label_section);
        wanted = (TextView) view.findViewById(R.id.wanted_label_section);
        wallpaperexplain = (TextView) view.findViewById(R.id.wallpaper_explain);
        favoriteexplain = (TextView) view.findViewById(R.id.favorite_explain);
        wantedexplain = (TextView) view.findViewById(R.id.wanted_explain);

        intro.setText(getString(R.string.introduction));
        wallpaper.setText(getString(R.string.wallpaper_label));
        favorite.setText(getString(R.string.favorite_label));
        wanted.setText(getString(R.string.wanted_label));
        wallpaperexplain.setText(getString(R.string.wallexplain));
        favoriteexplain.setText(getString(R.string.favoriteexplain));
        wantedexplain.setText(getString(R.string.wantedexplain));

        return view;
    }
}
