package com.example.bookapp.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.bookapp.Login;
import com.example.bookapp.R;
import com.google.firebase.auth.FirebaseAuth;


public class ConnectionUtility {

    public static boolean isConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void showNoInternetConnectionAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialogTheme);
        builder.setTitle(context.getString(R.string.no_conncetion));
        builder.setMessage(context.getString(R.string.no_conncection_description));
        builder.setIcon(R.drawable.ic_baseline_wifi_off_24);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences preferences = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
                if (preferences.getString("remember", "").equals("true")) {
                    logout(context);
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void logout(Context context) {

        SharedPreferences preferences = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember", "false");
        editor.putString("username", "");
        editor.putString("email", "");
        editor.putString("id", "");
        editor.apply();
        Intent intent = new Intent(context.getApplicationContext(), Login.class);
        context.startActivity(intent);
        FirebaseAuth.getInstance().signOut();
    }


}
