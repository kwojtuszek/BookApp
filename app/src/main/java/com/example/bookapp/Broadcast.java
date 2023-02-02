package com.example.bookapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;

public class Broadcast extends BroadcastReceiver {

    Map<String, String> petNotifications = new HashMap<>();
    Map<String, Integer> petIcons = new HashMap<>();

    @Override
    public void onReceive(Context context, Intent intent) {

        petNotifications = getPetNotifications(context);
        petIcons = getPetIcons();

        SharedPreferences preferences = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
        String pet = preferences.getString("pet", "");

        Intent repeating_Intent = new Intent(context, MainActivity.class);
        repeating_Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, repeating_Intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notification")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), petIcons.get(pet)), 128, 128, false))
                .setContentTitle(context.getString(R.string.reminder))
                .setContentText(petNotifications.get(pet))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());
    }

    public Map getPetNotifications(Context context) {

        petNotifications.put("lion", context.getString(R.string.lion_notify));
        petNotifications.put("dog", context.getString(R.string.dog_notify));
        petNotifications.put("cat", context.getString(R.string.cat_notify));
        petNotifications.put("piggy", context.getString(R.string.piggy_notify));
        petNotifications.put("shark", context.getString(R.string.shark_notify));
        petNotifications.put("cow", context.getString(R.string.cow_notify));
        petNotifications.put("sloth", context.getString(R.string.sloth_notify));
        petNotifications.put("fox", context.getString(R.string.fox_notify));
        petNotifications.put("turtle", context.getString(R.string.turtle_notify));
        petNotifications.put("crocodile", context.getString(R.string.crocodile_notify));

        return petNotifications;
    }

    public Map getPetIcons() {

        petIcons.put("lion", R.drawable.lion);
        petIcons.put("dog", R.drawable.doggo);
        petIcons.put("cat", R.drawable.kitty);
        petIcons.put("piggy", R.drawable.piggy);
        petIcons.put("shark", R.drawable.shark);
        petIcons.put("cow", R.drawable.cow);
        petIcons.put("sloth", R.drawable.sloth);
        petIcons.put("fox", R.drawable.fox);
        petIcons.put("turtle", R.drawable.turtle);
        petIcons.put("crocodile", R.drawable.crocodile);

        return petIcons;
    }
}