package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView tvUsername = (TextView) findViewById(R.id.username);
        TextView tvPassword = (TextView) findViewById(R.id.password);

        TextView register = (TextView) findViewById(R.id.register);

        MaterialButton login = (MaterialButton) findViewById(R.id.login);

        // Przejście do rejestracji
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });

        // Sprawdzenie czy nie użytkownik nie jest już zalogowany z wykorzystaniem SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String remember_value = preferences.getString("remember", "");
        if(remember_value.equals("true")){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else if(remember_value.equals("false")){
            Toast.makeText(Login.this, getString(R.string.please_login), Toast.LENGTH_SHORT).show();
        }

        // Funkcja odpowiadająca za logowanie
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String username, password;
                final String[] id = new String[1];
                username = tvUsername.getText().toString();
                password = tvPassword.getText().toString();

                if(!username.equals("") && !password.equals("")) {
                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = password;
                            PutData putData = new PutData("https://grpcapi.bieda.it/LoginBook/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    //End ProgressBar (Set visibility to GONE)
                                    if(result.equals("Login Success")){
                                        PutData checkId = new PutData("https://grpcapi.bieda.it/LoginBook/checkUserId.php",  "POST", field, data);
                                        if (checkId.startPut()) {
                                            if (checkId.onComplete()) {
                                                id[0] = checkId.getResult();
                                            }
                                        }

                                        openMain();

                                        Toast.makeText(getApplicationContext(), getString(R.string.good_login), Toast.LENGTH_SHORT).show();
                                        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("remember", "true");
                                        editor.putString("username", username);
                                        editor.putString("id", id[0]);
                                        editor.apply();
                                        finish();
                                    } else if(result.equals("Username or Password wrong")){
                                        Toast.makeText(getApplicationContext(), getString(R.string.bad_login), Toast.LENGTH_SHORT).show();
                                    } else if(result.equals("Error: Database connection")) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.db_connection_err), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.fields_req), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    // Otwarcie aktywności rejestracjo
    public void openRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    // Otwarcie aktywności po zalogowaniu
    public void openMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}