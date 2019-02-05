package com.example.dell1.myweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL1 on 2019/2/3.
 */

public class AQI {
    @SerializedName("air_now_city")
    public AQICity city;

    public String status;

    public class AQICity {

        public String aqi;

        public String pm25;

    }

    public AQI() {
        city = new AQICity();
    }
}
