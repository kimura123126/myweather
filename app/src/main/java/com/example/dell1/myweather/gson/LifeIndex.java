package com.example.dell1.myweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL1 on 2019/2/5.
 */

public class LifeIndex {
    @SerializedName("lifestyle")
    public List<Lifestyle> lifestyleList;

    public String status;
}
