package com.example.dell1.myweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell1.myweather.gson.AQI;
import com.example.dell1.myweather.gson.Forecast;
import com.example.dell1.myweather.gson.ForecastWeather;
import com.example.dell1.myweather.gson.LifeIndex;
import com.example.dell1.myweather.gson.NowWeather;
import com.example.dell1.myweather.gson.Weather;
import com.example.dell1.myweather.service.AutoUpdateService;
import com.example.dell1.myweather.util.HttpUtil;
import com.example.dell1.myweather.util.Utility;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;

    private Button navButton;

    public SwipeRefreshLayout swipeRefresh;

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    private Weather weather;

    private ImageView bingPicImg;

    private ForecastWeather forecastWeather;

    private NowWeather nowWeather;

    private LifeIndex lifeIndex;

    private AQI aqi;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        //初始化控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText =  (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        bingPicImg = (ImageView)findViewById(R.id.bing_pic_img);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navButton = (Button)findViewById(R.id.nav_button);
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);

        final String weatherId;

        if(weatherString != null){
         //   Weather weather = Utility.handleWeatherResponse(weatherString);
              Weather weather = Utility.JsonToWeather(weatherString);
              weatherId = weather.basic.weatherId;
              showWeatherInfo(weather);
        }else{
             weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });

        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    public void requestWeather(final String weatherId) {
        // String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";
        // String weatherUrl = "https://api.heweather.com/x3/weather?cityid=" + weatherId + "&key=92d39e652ac34b5daf208d0ca0d7a3d8";
        //   String weatherUrl ="https://api.heweather.com/s6/weather/now?location=" + weatherId + "&username=HE1902031920171175&t=1477455132&sign=92d39e652ac34b5daf208d0ca0d7a3d8";
        //   String weatherUrl = "https://free-api.heweather.com/s6/weather/now?&location=" + weatherId + "&key=92d39e652ac34b5daf208d0ca0d7a3d8";
        //  String weatherUrl = "https://free-api.heweather.com/s6/weather/forecast?location=" + weatherId + "&key=92d39e652ac34b5daf208d0ca0d7a3d8";
        //  String weatherUrl  = "https://free-api.heweather.com/s6/weather?key=92d39e652ac34b5daf208d0ca0d7a3d8&location=" + weatherId;
        //  String weatherUrl  = "https://free-api.heweather.net/s6/weather?key=92d39e652ac34b5daf208d0ca0d7a3d8&location=" + weatherId;

        String forecastUrl = "https://free-api.heweather.net/s6/weather/forecast?location=" +
                weatherId + "&key=92d39e652ac34b5daf208d0ca0d7a3d8";
        String nowUrl = "https://free-api.heweather.net/s6/weather/now?location=" +
                weatherId + "&key=92d39e652ac34b5daf208d0ca0d7a3d8";
        String AQIUrl = "https://free-api.heweather.net/s6/air/now?location=" +
                weatherId + "&key=92d39e652ac34b5daf208d0ca0d7a3d8";
        String LifeUrl = "https://free-api.heweather.net/s6/weather/lifestyle?location=" +
                weatherId + "&key=92d39e652ac34b5daf208d0ca0d7a3d8";

        requestAndUpdate(forecastUrl, Define.FORECAST);
        requestAndUpdate(nowUrl, Define.NOW);
        requestAndUpdate(AQIUrl, Define.AQI);
        requestAndUpdate(LifeUrl, Define.LIFESTYLE);
        loadBingPic();
    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }



    private void requestAndUpdate(String url, final int type) {
        HttpUtil.sendOkHttpRequest(url, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();

                switch (type) {
                    case Define.FORECAST:
                        forecastWeather = Utility.handleForecastResponse(responseText);
                        break;
                    case Define.NOW:
                        nowWeather = Utility.handleNowResponse(responseText);
                        break;
                    case Define.AQI:
                        aqi = Utility.handleAQIResponse(responseText);
                        break;
                    case Define.LIFESTYLE:
                        lifeIndex = Utility.handleLifeIndexResponse(responseText);
                        break;
                    default:
                }
                checkCount();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败1",
                                Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

        });
    }

    synchronized private void checkCount() {
        count++;
        if (count == 4) {
            updateUIAndData();
        }
    }

    private void updateUIAndData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (forecastWeather != null && nowWeather != null &&
                        aqi != null && lifeIndex != null &&
                        "ok".equals(forecastWeather.status) && "ok".equals(nowWeather.status) &&
                        "ok".equals(lifeIndex.status)) {

                    // assemble Weather bean
                    weather = new Weather(forecastWeather, nowWeather, aqi, lifeIndex);

                    SharedPreferences.Editor editor = PreferenceManager
                            .getDefaultSharedPreferences(WeatherActivity.this)
                            .edit();
                    editor.putString("weather", new Gson().toJson(weather).toString());
                    editor.apply();
                    showWeatherInfo(weather);
                } else {
                    Log.d("forecastWeather",forecastWeather+"");
                    Log.d("nowWeather",nowWeather+"");
                    Log.d("aqi",aqi+"");
                    Log.d("lifeIndex ",lifeIndex +"");
                    Log.d("forecastWeather.status",forecastWeather.status+"");
                    Log.d("nowWeather.status",nowWeather.status+"");
                    Log.d("lifeIndex.status",lifeIndex.status+"");




                    Toast.makeText(WeatherActivity.this, "获取天气信息失败2",
                            Toast.LENGTH_SHORT).show();

                }
                swipeRefresh.setRefreshing(false);
            }
        });
    }





      /*  HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather != null && "ok".equals(weather.status)){
                               SharedPreferences.Editor editor
                                       = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败2",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }*/

    private void showWeatherInfo(Weather weather){
        String cityName = weather.basic.cityName;
        String updateTime = weather.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature+"℃";
        String weatherInfo = weather.now.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for(Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);//不是degree_text
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.info);
            maxText.setText(forecast.max);
            minText.setText(forecast.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi != null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }else {
            aqiText.setText("环保部监测站点未覆盖当前地区");
            pm25Text.setText("环保部监测站点未覆盖当前地区");
        }
        String comfort = "舒适度："+weather.comfort.info;
        String carWash = "洗车指数："+weather.carWash.info;
        String sport = "运动建议："+weather.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
       Intent intent = new Intent(this, AutoUpdateService.class);
       startService(intent);

    }

}
