package com.example.volleysampleweatherapi;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NetworkRequests {
    Context context;

    public NetworkRequests(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String response);
    }

    private static final String API = "https://www.metaweather.com/api/location/search/?query=";
    private static final String FORECAST_API = "https://www.metaweather.com/api/location/";

    public void getCityID(String cityName, VolleyResponseListener volleyResponseListener) {
        //RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = API + cityName;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                String cityID = "";
                try {
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityID = cityInfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(context, "City ID is :" +cityID, Toast.LENGTH_LONG).show();
                volleyResponseListener.onResponse(cityID);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                volleyResponseListener.onError("Wrong");

            }
        });
        //queue.add(jsonArrayRequest);
        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);

        //return cityName;
    }

    public interface ForeCastByIDListener {
        void onError(String message);

        void onResponse(ArrayList<WeatherForeCastModel> weatherForeCastModel);
    }



    public void getForecastByID(String cityID, ForeCastByIDListener weatherForeCastModel) {
        ArrayList<WeatherForeCastModel> arrayList = new ArrayList<>();
        String url = FORECAST_API + cityID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();

                try {
                    JSONArray consolated_weather_list = response.getJSONArray("consolidated_weather");

                    for (int i = 0; i<consolated_weather_list.length(); i++) {
                        WeatherForeCastModel first_day = new WeatherForeCastModel();
                        JSONObject first_day_from_api = consolated_weather_list.getJSONObject(i);
                        //first_day = new WeatherForeCastModel();


                        int id = first_day_from_api.getInt("id");
                        first_day.setId(id);

                        String weather_state_name = first_day_from_api.getString("weather_state_name");
                        first_day.setWeather_state_name(weather_state_name);

                        String weather_state_abbr = first_day_from_api.getString("weather_state_abbr");
                        first_day.setWeather_state_abbr(weather_state_abbr);

                        first_day.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                        first_day.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                        first_day.setCreated(first_day_from_api.getString("created"));
                        first_day.setApplicable_date(first_day_from_api.getString("applicable_date"));
                        first_day.setMin_temp(first_day_from_api.getInt("min_temp"));
                        first_day.setMax_temp(first_day_from_api.getInt("max_temp"));
                        first_day.setThe_temp(first_day_from_api.getInt("the_temp"));
                        first_day.setWind_speed(first_day_from_api.getLong("wind_speed"));
                        first_day.setWind_direction(first_day_from_api.getLong("wind_direction"));
                        first_day.setAir_pressure(first_day_from_api.getLong("air_pressure"));
                        first_day.setHumidity(first_day_from_api.getInt("humidity"));
                        first_day.setVisibility(first_day_from_api.getLong("visibility"));
                        first_day.setPredictability(first_day_from_api.getInt("predictability"));
                        arrayList.add(first_day);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                weatherForeCastModel.onResponse(arrayList);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "wrong?",Toast.LENGTH_LONG).show();
                weatherForeCastModel.onError("WRONG WRONG");
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    public interface getForcastByCity {
        void onError(String message);

        void onResponse(ArrayList<WeatherForeCastModel> weatherForeCastModel);
    }


    public void getForcastByCityName(String cityName, getForcastByCity cityByForcastName){
        getCityID(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String cityID) {
                getForecastByID(cityID, new ForeCastByIDListener() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(ArrayList<WeatherForeCastModel> weatherForeCastModel) {
                        cityByForcastName.onResponse(weatherForeCastModel);
                    }
                });
            }
        });

    }
}
