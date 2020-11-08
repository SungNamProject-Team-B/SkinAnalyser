package com.Aha.pimple;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Http extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        new HttpAsyncTask().execute("https://goo.gl/eIXu9l");
    }
    private static class HttpAsyncTask extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {
            List<Weather> WeatherList = new ArrayList<>(); // 리스트들을 저장 해주는 코드
            String result = null;
            String strUrl = params[0];
            try {
                Request request = new Request.Builder()
                        .url(strUrl)
                        .build();

                Response response = client.newCall(request).execute();

                JSONArray jsonArray = new JSONArray(response.body().string());
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String country = jsonObject.getString("country");
                    String weather = jsonObject.getString("weather");
                    String temperature = jsonObject.getString("temperature");
                    Weather w = new Weather(country,weather,temperature);
                    WeatherList.add(w);    //이 리스트가 위에 코드 37번줄에 들어가게됨

                }
                Log.d(TAG,"doInBackground: " + WeatherList.toString());

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null ){
                Log.d(TAG, s);
            }
        }
    }
}
