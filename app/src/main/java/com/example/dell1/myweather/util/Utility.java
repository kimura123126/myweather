package com.example.dell1.myweather.util;

import android.text.TextUtils;

import com.example.dell1.myweather.db.City;
import com.example.dell1.myweather.db.County;
import com.example.dell1.myweather.db.Province;
import com.example.dell1.myweather.gson.AQI;
import com.example.dell1.myweather.gson.ForecastWeather;
import com.example.dell1.myweather.gson.LifeIndex;
import com.example.dell1.myweather.gson.NowWeather;
import com.example.dell1.myweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DELL1 on 2019/2/2.
 */

public class Utility {
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces = new JSONArray(response);
                for(int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }


    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                for(int i=0;i<allCities.length();i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties= new JSONArray(response);
                for(int i=0;i<allCounties.length();i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Weather JsonToWeather(String content) {
        return new Gson().fromJson(content, Weather.class);
    }

    public static ForecastWeather handleForecastResponse(String response) {
        try {
            String content = handleJsonResponse(response);
            return new Gson().fromJson(content, ForecastWeather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NowWeather handleNowResponse(String response) {
        try {
            String content = handleJsonResponse(response);
            return new Gson().fromJson(content, NowWeather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AQI handleAQIResponse(String response) {
        try {
            String content = handleJsonResponse(response);
            AQI aqi = new Gson().fromJson(content, AQI.class);
            return aqi;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LifeIndex handleLifeIndexResponse(String response) {
        try {
            String content = handleJsonResponse(response);
            return new Gson().fromJson(content, LifeIndex.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

   /* public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }*/
  /* public static String handleJsonResponse(String response) throws JSONException {//书上代码为handleWeatherResponse
       JSONObject jsonObject = new JSONObject(response);
       JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
       String content = jsonArray.getJSONObject(0).toString();
         return new Gson().fromJson(content,Weather.class);
     //  return content;//参考代码
   }*/
   public static String handleJsonResponse(String response) throws JSONException {

           JSONObject jsonObject = new JSONObject(response);
           JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
           String content = jsonArray.getJSONObject(0).toString();
           return content;

   }


}
