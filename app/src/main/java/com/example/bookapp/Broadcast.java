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

public class Broadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences preferences = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
        String pet = preferences.getString("pet", "");
        String contentText = setContentText(pet, context);
        int largeIcon = setLargeIcon(pet);

        Intent repeating_Intent = new Intent(context, MainActivity.class);
        repeating_Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, repeating_Intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notification")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), largeIcon), 128, 128, false))
                .setContentTitle(context.getString(R.string.reminder))
                .setContentText(contentText)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());
    }


    public String setContentText(String pet, Context context) {

        String message = "";

        switch (pet) {

            case "lion":
                message = context.getString(R.string.lion_notify);
                break;

            case "dog":
                message = context.getString(R.string.dog_notify);
                break;

            case "cat":
                message = context.getString(R.string.cat_notify);
                break;

            case "piggy":
                message = context.getString(R.string.piggy_notify);
                break;

            case "shark":
                message = context.getString(R.string.shark_notify);
                break;

            case "cow":
                message = context.getString(R.string.cow_notify);
                break;

            case "sloth":
                message = context.getString(R.string.sloth_notify);
                break;

            case "fox":
                message = context.getString(R.string.fox_notify);
                break;

            case "turtle":
                message = context.getString(R.string.turtle_notify);
                break;

            case "crocodile":
                message = context.getString(R.string.crocodile_notify);
                break;
        }

        return message;
    }

    public int setLargeIcon(String pet) {

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

}