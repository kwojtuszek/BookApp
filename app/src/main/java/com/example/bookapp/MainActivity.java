package com.example.bookapp;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Drawer_base {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView tvWelcome, tvYourLevel, tvYourReadedBooks, tvNextLevel;
    ImageView imPet;

    ActivityMainBinding activityMainBinding;
    ProgressBar progress_bar;
    int experience, userLevel, userReads;
    String pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        allocateActivityTitle(getString(R.string.main_page));

        notificationChannel();
        calendar();
        startView();
    }

    public void startView() {

        tvWelcome = findViewById(R.id.main);
        tvYourLevel = findViewById(R.id.level);
        tvYourReadedBooks = findViewById(R.id.readed_books);
        tvNextLevel = findViewById(R.id.next_level);
        progress_bar = findViewById(R.id.bar);
        imPet = findViewById(R.id.pet_image);


        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        userLevel = preferences.getInt("level", 0);
        userReads = preferences.getInt("readed", 0);
        pet = preferences.getString("pet", "");


        tvWelcome.append(" " + preferences.getString("username", ""));
        tvYourLevel.append(" " + userLevel);
        tvYourReadedBooks.append(" " + userReads + " " + getString(R.string.books));

        progress_bar.setProgress(countExperienceBar(userLevel, userReads));

        imPet.setImageResource(setPetImage(pet));

        imPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPetMessage(pet);
            }
        });



    }

    public int countExperienceBar(int userLevel, int userReads) {

        switch (userLevel) {
            case 1:
                experience = userReads * 100 / 10;
                tvNextLevel.setText((10 - userReads) + " " + getString(R.string.level_req_1));
                break;
            case 2:
                experience = userReads * 100 / 20;
                tvNextLevel.setText((20 - userReads) + " " + getString(R.string.level_req_1));
                break;
            case 3:
                experience = userReads * 100 / 30;
                tvNextLevel.setText((30 - userReads) + " " + getString(R.string.level_req_1));
                break;
            case 4:
                experience = userReads * 100 / 40;
                tvNextLevel.setText((40 - userReads) + " " + getString(R.string.level_req_1));
                break;
            case 5:
                experience = userReads * 100 / 50;
                tvNextLevel.setText((50 - userReads) + " " + getString(R.string.level_req_1));
                break;
            case 6:
                experience = userReads * 100 / 60;
                tvNextLevel.setText((60 - userReads) + " " + getString(R.string.level_req_1));
                break;
            case 7:
                experience = userReads * 100 / 70;
                tvNextLevel.setText((70 - userReads) + " " + getString(R.string.level_req_1));
                break;
            case 8:
                experience = userReads * 100 / 80;
                tvNextLevel.setText((80 - userReads) + " " + getString(R.string.level_req_1));
                break;
            case 9:
                experience = userReads * 100 / 90;
                tvNextLevel.setText((90 - userReads) + " " + getString(R.string.level_req_1));
                break;
            case 10:
                tvNextLevel.setText(getString(R.string.max_level));
        }
        return experience;
    }

    public void notificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Notification", getString(R.string.reminder), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(getString(R.string.reminder_description));

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void calendar() {

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        int notifyTime = preferences.getInt("notifyTime", 8);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, notifyTime);
        calendar.set(Calendar.SECOND, 0);

        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent = new Intent(MainActivity.this, Broadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    public int setPetImage(String pet) {

        int imageSrc = 0;

        switch (pet) {

            case "lion":
                imageSrc = R.drawable.lion;
                break;

            case "dog":
                imageSrc = R.drawable.doggo;
                break;

            case "cat":
                imageSrc = R.drawable.kitty;
                break;

            case "piggy":
                imageSrc = R.drawable.piggy;
                break;

            case "shark":
                imageSrc = R.drawable.shark;
                break;

            case "cow":
                imageSrc = R.drawable.cow;
                break;

            case "sloth":
                imageSrc = R.drawable.sloth;
                break;

            case "fox":
                imageSrc = R.drawable.fox;
                break;

            case "turtle":
                imageSrc = R.drawable.turtle;
                break;

            case "crocodile":
                imageSrc = R.drawable.crocodile;
                break;
        }

        return imageSrc;

    }

    public void setPetMessage(String pet) {

        switch (pet) {

            case "lion":
                Toast.makeText(getApplicationContext(), getString(R.string.lion_click), Toast.LENGTH_SHORT).show();
                break;

            case "dog":
                Toast.makeText(getApplicationContext(), getString(R.string.dog_click), Toast.LENGTH_SHORT).show();
                break;

            case "cat":
                Toast.makeText(getApplicationContext(), getString(R.string.cat_click), Toast.LENGTH_SHORT).show();
                break;

            case "piggy":
                Toast.makeText(getApplicationContext(), getString(R.string.piggy_click), Toast.LENGTH_SHORT).show();
                break;

            case "shark":
                Toast.makeText(getApplicationContext(), getString(R.string.shark_click), Toast.LENGTH_SHORT).show();
                break;

            case "cow":
                Toast.makeText(getApplicationContext(), getString(R.string.cow_click), Toast.LENGTH_SHORT).show();
                break;

            case "sloth":
                Toast.makeText(getApplicationContext(), getString(R.string.sloth_click), Toast.LENGTH_SHORT).show();
                break;

            case "fox":
                Toast.makeText(getApplicationContext(), getString(R.string.fox_click), Toast.LENGTH_SHORT).show();
                break;

            case "turtle":
                Toast.makeText(getApplicationContext(), getString(R.string.turtle_click), Toast.LENGTH_SHORT).show();
                break;

            case "crocodile":
                Toast.makeText(getApplicationContext(), getString(R.string.crocodile_click), Toast.LENGTH_SHORT).show();
                break;
        }

    }
}