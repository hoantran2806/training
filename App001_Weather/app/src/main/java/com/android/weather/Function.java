package com.android.weather;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Function {

    private static final String TAG = "AAAAAAAAAAAAAAAAAA";
    private static final String OPEN_WEATHER_MAP_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";
    private static final String OPEN_WEATHER_MAP_API = "ff95a245fe9e2e8e9133581e2d4e7198";

    private static String setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId/100;
        String icon ="";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime >= sunrise && currentTime < sunset){
                icon = "&#xf00d;";
            }else{
                icon = "&#xf02e;";
            }
        }else{
            switch (id){
                case 2:
                    icon = "&#xf01e;";
                    break;
                case 3:
                    icon = "&#xf01c;";
                    break;
                case 5:
                    icon = "&#xf019;";
                    break;
                case 6:
                    icon = "&#xf01b;";
                    break;
                case 7:
                    icon = "&#xf014;";
                    break;
                case 8:
                    icon = "&#xf013;";
                    Log.i(TAG, "setWeatherIcon: ");
                    break;

            }
        }
        return icon;
    }
    private static JSONObject getWeatherJSON(String lat, String lon){
        try{
            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL,lat, lon));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("key_api",OPEN_WEATHER_MAP_API);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while((tmp = bufferedReader.readLine()) != null){
                json.append(tmp).append("\n");
            }
            bufferedReader.close();
            JSONObject data = new JSONObject(json.toString());
            if(data.getInt("cod") != 200){
                return null;
            }
            return data;
        }catch(Exception e){
            Log.e(TAG, "Error getWeatherJSON: ", e);
            return null;
        }
    }
    public static class placeIdTask extends AsyncTask< String, Void ,JSONObject>{
        public AsyncResponce delegate = null;
        public placeIdTask(AsyncResponce asyncResponce){
            delegate = asyncResponce;
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonWeather = null;
            try{
                jsonWeather = getWeatherJSON(params[0], params[1]);
            }catch (Exception e){
                Log.d("Error", "doInBackground: ", e);
            }
            return jsonWeather;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try{
                if(jsonObject != null){
                    JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = jsonObject.getJSONObject("main");
                    DateFormat df = DateFormat.getDateInstance();
                    String city = jsonObject.getString("name").toUpperCase(Locale.US)
                            + ", " + jsonObject.getJSONObject("sys").getString("country");
                    String description = details.getString("description").toUpperCase(Locale.US);
                    String temperature = String.format("%.2f", main.getDouble("temp")) + "°";
                    String humidity = main.getString("humidity") + "%";
                    String presure = main.getString("presure") + "hPa";
                    String updateOn = df.format(new Date(jsonObject.getLong("dt") *1000));
                    String iconText = setWeatherIcon(details.getInt("id"),
                            jsonObject.getJSONObject("sys").getLong("sunrise")*1000,
                            jsonObject.getJSONObject("sys").getLong("sunset") * 1000);
                }
            }catch (Exception e){
                Log.d("ERROR", "onPostExecute: ", e);
            }
        }
    }
    public interface AsyncResponce {
        void processFinish(String output1, String output2, String output3, String output4, String output5, String output6, String output7, String output8);
    }
}
