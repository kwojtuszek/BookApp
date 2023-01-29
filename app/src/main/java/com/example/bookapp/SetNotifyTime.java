package com.example.bookapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.bookapp.databinding.ActivitySetNotifyTimeBinding;
import com.google.android.material.button.MaterialButton;

public class SetNotifyTime extends Drawer_base {

    int notifyTime;
    ActivitySetNotifyTimeBinding activitySetNotifyTimeBinding;
    MaterialButton setbtn;
    NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySetNotifyTimeBinding = activitySetNotifyTimeBinding.inflate(getLayoutInflater());
        setContentView(activitySetNotifyTimeBinding.getRoot());
        allocateActivityTitle("");

        setData();

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {

            }
        });

        setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setNotifyTime(numberPicker.getValue());
                openMain();
            }
        });
    }

    public void setData() {

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        notifyTime = preferences.getInt("notifyTime", 8);

        setbtn = findViewById(R.id.setbtn);

        numberPicker = findViewById(R.id.number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(24);
        numberPicker.setValue(notifyTime);
    }

    public void setNotifyTime(int time) {

        Toast.makeText(getApplicationContext(), getString(R.string.notify_time_info) + " " + time, Toast.LENGTH_SHORT).show();

        if (time == 24) {
            time = 0;
        }

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("notifyTime", time);
        editor.apply();

    }

    public void openMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
