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

    int[] levelThresholds = {10, 20, 30, 40, 50, 60, 70, 80, 90, 91};

    Map<String, String> petMesseages = new HashMap<>();
    Map<String, Integer> petImages = new HashMap<>();

    ActivityMainBinding activityMainBinding;
    ProgressBar progress_bar;
    int userLevel, userReads;
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
        petMesseages = getPetMessages();
        petImages = getPetImages();


        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        userLevel = preferences.getInt("level", 0);
        userReads = preferences.getInt("readed", 0);
        pet = preferences.getString("pet", "");


        tvWelcome.append(" " + preferences.getString("username", ""));
        tvYourLevel.append(" " + userLevel);

        tvYourReadedBooks.setText(getYourReadedBooksText(userReads));

        progress_bar.setProgress(getExperienceByLevel(userReads));

        tvNextLevel.setText(getNextLevelText(userLevel, userReads));

        imPet.setImageResource(petImages.get(pet));

        imPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), petMesseages.get(pet), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getExperienceByLevel(int userReads) {

        int levelStartReads;
        for (int i = 0; i < levelThresholds.length; i++) {
            if (userReads < levelThresholds[i]) {
                if (i == 0) {
                    levelStartReads = 0;
                } else {
                    levelStartReads = levelThresholds[i - 1];
                }
                return (userReads - levelStartReads) * 100 / 10;
            }
        }
        return 100;
    }

    private String getNextLevelText(int userLevel, int userReads) {

        if (userLevel == 10) {
            return getString(R.string.max_level);
        } else {
            int nextLevelReads = levelThresholds[userLevel - 1];
            int readsToNextLevel = nextLevelReads - userReads;
            if (readsToNextLevel < 5) {
                if (readsToNextLevel == 1) {
                    return readsToNextLevel + " " + getString(R.string.level_req_1);
                } else {
                    return readsToNextLevel + " " + getString(R.string.level_req_2);
                }
            } else {
                return readsToNextLevel + " " + getString(R.string.level_req_3);
            }
        }
    }

    public String getYourReadedBooksText(int userReads) {

        String yourReadedBooks;
        int lastDigit = userReads % 10;

        if (userReads == 0) {
            yourReadedBooks = getString(R.string.no_readed_books);
        } else if (userReads == 1) {
            yourReadedBooks = (getString(R.string.you_readed) + " " + userReads + " " + getString(R.string.books_one));
        } else if (lastDigit < 5 && lastDigit > 1) {
            yourReadedBooks = (getString(R.string.you_readed) + " " + userReads + " " + getString(R.string.books_below_5));
        } else {
            yourReadedBooks = getString(R.string.you_readed) + (" " + userReads + " " + getString(R.string.books));
        }
        return yourReadedBooks;
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

    public Map getPetImages() {

        petImages.put("lion", R.drawable.lion);
        petImages.put("dog", R.drawable.doggo);
        petImages.put("cat", R.drawable.kitty);
        petImages.put("piggy", R.drawable.piggy);
        petImages.put("shark", R.drawable.shark);
        petImages.put("cow", R.drawable.cow);
        petImages.put("sloth", R.drawable.sloth);
        petImages.put("fox", R.drawable.fox);
        petImages.put("turtle", R.drawable.turtle);
        petImages.put("crocodile", R.drawable.crocodile);

        return petImages;
    }

    public Map getPetMessages() {

        petMesseages.put("lion", getString(R.string.lion_click));
        petMesseages.put("dog", getString(R.string.dog_click));
        petMesseages.put("cat", getString(R.string.cat_click));
        petMesseages.put("piggy", getString(R.string.piggy_click));
        petMesseages.put("shark", getString(R.string.shark_click));
        petMesseages.put("cow", getString(R.string.cow_click));
        petMesseages.put("sloth", getString(R.string.sloth_click));
        petMesseages.put("fox", getString(R.string.fox_click));
        petMesseages.put("turtle", getString(R.string.turtle_click));
        petMesseages.put("crocodile", getString(R.string.crocodile_click));

        return petMesseages;
    }
}