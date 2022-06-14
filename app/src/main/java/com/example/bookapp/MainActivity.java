package com.example.bookapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.example.bookapp.databinding.ActivityMainBinding;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;

import java.lang.reflect.Array;
import java.util.Arrays;


public class MainActivity extends Drawer_base {

    ActivityMainBinding activityMainBinding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        allocateActivityTitle("");

        TextView tvwelcome = (TextView) findViewById(R.id.main);

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        tvwelcome.append(" " + preferences.getString("username", ""));

    }
}