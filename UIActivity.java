package com.example.niceweather;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class UIActivity extends AppCompatActivity {

    RecyclerView recyclerView, recyclerViewHorisont;
    RecyclerView.Adapter programAdapter;
    RecyclerView.Adapter programAdapterHor;
    RecyclerView.LayoutManager layoutManager;
    LinearLayoutManager layoutManagerhor;
    public static double latitude = 0;
    public static double longtude = 0;
    public static String cityName = "";
    String[] Days = {"Today", "Tomorrow", "", "", "", "", ""};
    int[] nightTemp = new int[7];
    int[] dailyTemp = new int[7];
    String[] timings = new String[20];
    String[] temps = new String[20];
    String[] weathers = new String[20];
    EditText editTextText;
    TextView result, resultWeather;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "aa6d6e2565ad425afd5dcf8de5deaf2d";

    DecimalFormat df = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiactivity);
        recyclerView = findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        editTextText = findViewById(R.id.editTextText);
        layoutManagerhor = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHorisont = findViewById(R.id.recyclehor);
        recyclerViewHorisont.setHasFixedSize(true);
        recyclerViewHorisont.setLayoutManager(layoutManagerhor);
        result = findViewById(R.id.textView);
        resultWeather = findViewById(R.id.textView5);
    }

    public void getWeahterDetails(View view){
        String city = editTextText.getText().toString().split(",")[0].trim();
        String state = editTextText.getText().toString().split(",")[1].trim();
        String tempUrl = "";
        if (!city.equals("")){
            if (!state.equals("")){
                tempUrl = url + "?q=" + city + "," + state + "&appid=" + appid;
            }
            else{
                tempUrl = url + "?q=" + city + "&appid=" + appid;
            }
        }

       StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
                Log.d("response", response);
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject jsonObjectWeather = jsonResponse.getJSONObject("coord");
                    double lon = jsonObjectWeather.getDouble("lon");
                    double lat = jsonObjectWeather.getDouble("lat");
                    setTemp(lat, lon);
                    fillTimings(lat, lon);
//                    result.setText(city);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(getApplicationContext(), error.toString().trim(),Toast.LENGTH_SHORT).show();
           }
       });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
}

    public void setTemp(double lat, double len){
        String tempUrl = "https://api.openweathermap.org/data/2.5/onecall?lat=" + Double.toString(lat) + "&lon=" + Double.toString(len) + "&exclude=current,minutely,hourly,alerts&appid=" + appid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try{
                    for (int i = 0; i < 7; i++){
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("daily");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(i);
                        JSONObject jsonObjectTemp = jsonObjectWeather.getJSONObject("temp");
                        double min = jsonObjectTemp.getDouble("min") - 273.15;
                        nightTemp[i] = (int) min;
                        double max = jsonObjectTemp.getDouble("max") - 273.15;
                        dailyTemp[i] = (int) max;
                    }
                    fillDates();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @SuppressLint("SimpleDateFormat")
    public void fillDates(){
        Days[0] = "Today";
        Days[1] = "Tommorow";
        Calendar c = Calendar.getInstance();
        Days[0] += ", " + c.get(Calendar.DAY_OF_MONTH) + ", " + new SimpleDateFormat("MMM").format(c.getTime());
        c.add(Calendar.DATE, 1);
        Days[1] += ", " + c.get(Calendar.DAY_OF_MONTH) + ", " + new SimpleDateFormat("MMM").format(c.getTime());
        for (int i = 2; i < 7; i++) {
            c.add(Calendar.DATE, 1);
            Days[i] = new SimpleDateFormat("EEE").format(c.getTime())  + ", " + c.get(Calendar.DAY_OF_MONTH) + ", " + new SimpleDateFormat("MMM").format(c.getTime());
        }
    }


    public void fillTimings(double lat, double len){
        String tempUrl = "https://api.openweathermap.org/data/2.5/onecall?lat=" + Double.toString(lat) + "&lon=" + Double.toString(len) + "&exclude=alerts&appid=" + appid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try{

                    for (int i = 0; i < 7; i++){
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("daily");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(i);
                        JSONObject jsonObjectTemp = jsonObjectWeather.getJSONObject("temp");
                        double min = jsonObjectTemp.getDouble("min") - 273.15;
                        nightTemp[i] = (int) min;
                        double max = jsonObjectTemp.getDouble("max") - 273.15;
                        dailyTemp[i] = (int) max;
                    }

                    programAdapter = new RecyclerViewAdapter(UIActivity.this, Days,
                            nightTemp, dailyTemp);
                    recyclerView.setAdapter(programAdapter);
                    for (int i = 0; i < 40; i+=2){ // 0 2 4 6 8 10 12 14 16 18
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("hourly");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(i);
                        Date d = new Date(jsonObjectWeather.getLong("dt")*1000);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(d);
                        int tmp = cal.get(Calendar.HOUR_OF_DAY);
                        timings[i/2] = Integer.toString(tmp) + ":00";
                        double temp = jsonObjectWeather.getDouble("temp") - 273.15;
                        tmp = (int) temp;
                        temps[i/2] = tmp + "°C";
                        JSONArray jsonArray1 = jsonObjectWeather.getJSONArray("weather");
                        JSONObject jsonObjectWea = jsonArray1.getJSONObject(0);
                        String weather = jsonObjectWea.getString("main");
                        weathers[i/2] = weather;
                    }
                    programAdapterHor = new HorisontalRecyclerViewAdapter(UIActivity.this, timings,
                            weathers, temps);
                    recyclerViewHorisont.setAdapter(programAdapterHor);

                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject jsonObject = jsonResponse.getJSONObject("current");
                    cityName = jsonResponse.getString("timezone").replaceAll("[A-Z][a-z]*\\/", "");

                    Double floatTemp = jsonObject.getDouble("temp") - 273.15;
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String res = jsonObject1.getString("main");
                    resultWeather.setText(res + "\n\n" +floatTemp + "°C" );


                    result.setText(cityName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    float a;

    public void openMap(View view) {
        Intent intent = new Intent(UIActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    public void updateAPP(View view){
        setTemp(latitude, longtude);
        fillTimings(latitude, longtude);
    }
}
