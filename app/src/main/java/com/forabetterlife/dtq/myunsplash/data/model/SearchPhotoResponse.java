package com.forabetterlife.dtq.myunsplash.data.model;

/**
 * Created by DTQ on 3/23/2018.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchPhotoResponse {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private List<PhotoResponse> results = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<PhotoResponse> getResults() {
        return results;
    }

    public void setResults(List<PhotoResponse> results) {
        this.results = results;
    }

}
