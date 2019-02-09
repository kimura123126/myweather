package com.example.dell1.myweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL1 on 2019/2/3.
 */

/*public class Lifestyle {
    public String type;

    @SerializedName("txt")
    public String info;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


}*/
public class Lifestyle {

    //舒适指数
    @SerializedName("comf")
    public Comfort comfort;

    //洗车指数
    @SerializedName("cw")
    public CarWash carWash;

    //运动指数
    @SerializedName("sport")
    public  Sport sport;

    public class Comfort{

        @SerializedName("txt")
        public String info;
    }

    public class CarWash{

        @SerializedName("txt")
        public String info;
    }

    public class Sport{

        @SerializedName("txt")
        public String info;
    }
}
