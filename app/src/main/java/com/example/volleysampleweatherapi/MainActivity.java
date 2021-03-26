package com.example.volleysampleweatherapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button b1, b2, b3;
    EditText et;
    String cityID="";
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.btn1);
        b2 = (Button) findViewById(R.id.btn2);
        b3 = (Button) findViewById(R.id.btn3);
        et = (EditText) findViewById(R.id.et1);
        lv = (ListView) findViewById(R.id.list);
        NetworkRequests networkRequests = new NetworkRequests(MainActivity.this);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                networkRequests.getCityID(et.getText().toString(), new NetworkRequests.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "SOMETHING WRONG" ,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String cityID) {
                        Toast.makeText(MainActivity.this, "Returned City ID for "+et.getText().toString()+" city"+" which is: "+cityID ,Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkRequests.getForecastByID(et.getText().toString(), new NetworkRequests.ForeCastByIDListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "something wrong", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(ArrayList<WeatherForeCastModel> weatherForeCastModel) {
                        //Toast.makeText(MainActivity.this, weatherForeCastModel.toString() , Toast.LENGTH_LONG).show();
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherForeCastModel);
                        lv.setAdapter(arrayAdapter);
                    }
                });
            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                networkRequests.getForcastByCityName(et.getText().toString(), new NetworkRequests.getForcastByCity() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "something wrong", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(ArrayList<WeatherForeCastModel> weatherForeCastModel) {
                        //Toast.makeText(MainActivity.this, weatherForeCastModel.toString() , Toast.LENGTH_LONG).show();
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherForeCastModel);
                        lv.setAdapter(arrayAdapter);
                    }
                });

            }
        });
    }
}