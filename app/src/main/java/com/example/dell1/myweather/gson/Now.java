package com.example.dell1.myweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL1 on 2019/2/3.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond_txt")
    public String info;

}
