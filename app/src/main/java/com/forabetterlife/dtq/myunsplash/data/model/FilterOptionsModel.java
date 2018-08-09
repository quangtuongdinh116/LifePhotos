package com.forabetterlife.dtq.myunsplash.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringDef;

import com.annimon.stream.Stream;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.forabetterlife.dtq.myunsplash.data.model.FilterOptionsModel.FilterType.ALL;
import static com.forabetterlife.dtq.myunsplash.data.model.FilterOptionsModel.FilterType.LATEST;

public class FilterOptionsModel implements Parcelable {
    private static final String TYPE = "type";
    private static final String SORT = "sort";

    private String type=ALL;
    private String sort = LATEST;


    public interface FilterType {
        String ALL = "All";
        String CURATED = "Curated";
        String LATEST = "Latest";
        String OLDEST = "Oldest";
        String POPULAR = "Popular";

        @StringDef({
                ALL,
                CURATED,
                LATEST,
                OLDEST,
                POPULAR
        })
        @Retention(RetentionPolicy.SOURCE) @interface Filter {}
    }
    private List<String> typesList = Stream.of("All", "Curated").toList();
    private List<String> sortList = Stream.of("Latest", "Oldest", "Popular").toList();

    public FilterOptionsModel() {
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public String getSort() {
        return sort;
    }

    public List<String> getTypesList() {
        return typesList;
    }

    public List<String> getSortList() {
        return sortList;
    }

    public int getSelectedTypeIndex() {
        return typesList.indexOf(type);
    }

    public int getSelectedSortIndex() {
        return sortList.indexOf(sort);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.sort);
        dest.writeStringList(this.typesList);
        dest.writeStringList(this.sortList);
    }

    protected FilterOptionsModel(Parcel in) {
        this.type = in.readString();
        this.sort = in.readString();
        this.typesList = in.createStringArrayList();
        this.sortList = in.createStringArrayList();
    }

    public static final Parcelable.Creator<FilterOptionsModel> CREATOR = new Parcelable.Creator<FilterOptionsModel>() {
        @Override
        public FilterOptionsModel createFromParcel(Parcel source) {
            return new FilterOptionsModel(source);
        }

        @Override
        public FilterOptionsModel[] newArray(int size) {
            return new FilterOptionsModel[size];
        }
    };
}
