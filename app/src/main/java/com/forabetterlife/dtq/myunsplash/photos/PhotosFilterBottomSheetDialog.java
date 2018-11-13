package com.forabetterlife.dtq.myunsplash.photos;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.forabetterlife.dtq.myunsplash.R;
import com.forabetterlife.dtq.myunsplash.data.model.FilterOptionsModel;
import com.forabetterlife.dtq.myunsplash.utils.BaseBottomSheetDialog;
import com.forabetterlife.dtq.myunsplash.utils.BundleConstant;
import com.forabetterlife.dtq.myunsplash.utils.Bundler;

import butterknife.BindView;
import butterknife.OnClick;

public class PhotosFilterBottomSheetDialog extends BaseBottomSheetDialog {

    @BindView(R.id.type_selection)
    Spinner typeSelectionSpinner;
    @BindView(R.id.sort_selection)
    Spinner sortSelectionSpinner;

    private FilterOptionsModel currentFilterOptions;

    private PhotosFilterChangeListener listener;

    public static PhotosFilterBottomSheetDialog newInstance(@NonNull FilterOptionsModel currentFilterOptions) {
        PhotosFilterBottomSheetDialog fragment = new PhotosFilterBottomSheetDialog();
        fragment.setArguments(Bundler.start().put(BundleConstant.ITEM, currentFilterOptions).end());
        return fragment;
    }

    @Override
    protected int layoutRes() {
        return R.layout.filter_bottom_sheet;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentFilterOptions = getArguments().getParcelable(BundleConstant.ITEM);

        if (currentFilterOptions == null)
            return;

        ArrayAdapter<String> typesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,
                currentFilterOptions.getTypesList());
        ArrayAdapter<String> sortOptionsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,
                currentFilterOptions.getSortList());

        typeSelectionSpinner.setAdapter(typesAdapter);
        sortSelectionSpinner.setAdapter(sortOptionsAdapter);

        typeSelectionSpinner.setSelection(currentFilterOptions.getSelectedTypeIndex());
        sortSelectionSpinner.setSelection(currentFilterOptions.getSelectedSortIndex());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (PhotosFragment) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement PhotosFilterChangeListener");
        }
    }

    @Override public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @OnClick(R.id.filter_sheet_apply_btn) public void onApply() {
        if (listener != null) {
            listener.onTypeSelected((String) typeSelectionSpinner.getSelectedItem());
            listener.onSortOptionSelected((String) sortSelectionSpinner.getSelectedItem());
            listener.onFilterApply();
            dismiss();
        }
    }

    public interface PhotosFilterChangeListener {
        void onFilterApply();

        void onTypeSelected(String selectedType);

        void onSortOptionSelected(String selectedSortOption);

    }
}
