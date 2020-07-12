package com.bytedance.androidcamp.network.dou.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetVideosResponse {

    @SerializedName("success") public boolean success;
    @SerializedName("feeds") public List<Video> videos;
    @SerializedName("error") public String error;

    @Override
    public String toString() {
        return "success=" + success +
                ", error=" + error +
                '}';
    }
}
