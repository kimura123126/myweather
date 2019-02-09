package com.example.dell1.myweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL1 on 2019/2/2.
 */

/*public class Basic {
    @SerializedName("location")
    public String countyName;

    @SerializedName("parent_city")
    public String cityName;

    @SerializedName("cid")
    public String weatherId;

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

}*/
public class Basic {
    /**
     * 使用@SerializedName注解的方式让JSON字段和Java字段建立映射关系
     */
    @SerializedName("location")
    public String cityName;

    @SerializedName("cid")
    public String weatherId;

    public Update update;

    public class Update
    {
        @SerializedName("loc")
        public String updateTime;
    }
}
