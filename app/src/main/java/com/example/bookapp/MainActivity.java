package com.example.bookapp;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;

import org.checkerframework.checker.nullness.qual.NonNull;

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

        MaterialButton base = (MaterialButton) findViewById(R.id.base);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        tvwelcome.append(" " + preferences.getString("username", ""));

        base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("test")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Toast.makeText(getApplicationContext(), "Dziala: " + document.getString("mes"), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Nie działa", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}