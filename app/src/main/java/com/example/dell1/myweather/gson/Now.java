package com.example.dell1.myweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL1 on 2019/2/3.
 */

/*public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond_txt")
    public String info;

}*/
public class Now {

    //当前温度
    @SerializedName("tmp")
    public String temperature;

    //天气状况
    @SerializedName("cond")
    public More more;

    public class More{

        @SerializedName("txt")
        public String info;
    }
}
