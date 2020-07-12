package com.bytedance.androidcamp.network.dou.model;

import com.google.gson.annotations.SerializedName;

public class PostVideoResponse {

    @SerializedName("url") public String url;
    @SerializedName("success") public boolean success;
    @SerializedName("error") public String error;

    @Override
    public String toString() {
        return "success=" + success +
                ", error=" + error +
                '}';
    }
}
