/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */
        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);

        // DONE (4) Delete the dummy weather data. You will be getting REAL data from the Internet in this lesson.

        // DONE (3) Delete the for loop that populates the TextView with dummy data


        // DONE (9) Call loadWeatherData to perform the network request to get the weather
        loadWeatherData();
    }

    // DONE (8) Create a method that will get the user's preferred location and execute your new AsyncTask and call it loadWeatherData
    public void loadWeatherData(){
        String preferedLocation = SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchWeatherAsyncTask().execute(NetworkUtils.buildUrl(preferedLocation));

    }

    // DONE (5) Create a class that extends AsyncTask to perform network requests
    public class FetchWeatherAsyncTask extends AsyncTask<URL, Void, String>{
        // DONE (6) Override the doInBackground method to perform your network requests
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String result = null;
            try {
                result = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        // DONE (7) Override the onPostExecute method to display the results of the network request
        @Override
        protected void onPostExecute(String s) {
            if(s!=null && !s.isEmpty()){
                String[] simpleJsonWeatherData = null;
                try {
                    simpleJsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this,s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(String weatherDataItem : simpleJsonWeatherData) {
                    mWeatherTextView.append(weatherDataItem + "\n\n\n");
                }
            }
        }
    }
}