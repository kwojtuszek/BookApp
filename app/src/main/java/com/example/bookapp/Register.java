package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView tvUsername =(TextView) findViewById(R.id.username);
        TextView tvPassword =(TextView) findViewById(R.id.password);
        TextView tvEmail =(TextView) findViewById(R.id.email);

        MaterialButton registerbtn =(MaterialButton) findViewById(R.id.registerbtn);

        // Funkcja rejestracji
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String username, password, email;
                username = tvUsername.getText().toString();
                password = tvPassword.getText().toString();
                email = tvEmail.getText().toString();

                if(!username.equals("") && !password.equals("") && !email.equals("")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[3];
                            field[0] = "username";
                            field[1] = "password";
                            field[2] = "email";
                            //Creating array for data
                            String[] data = new String[3];
                            data[0] = username;
                            data[1] = password;
                            data[2] = email;
                            PutData putData = new PutData("https://grpcapi.bieda.it/LoginBook/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    //End ProgressBar (Set visibility to GONE)
                                    if(result.equals("Sign Up Success")){
                                        openLogin();
                                        Toast.makeText(getApplicationContext(), getString(R.string.good_signup), Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else if(result.equals("Sign up Failed")){
                                        Toast.makeText(getApplicationContext(), getString(R.string.user_exists), Toast.LENGTH_SHORT).show();
                                    } else if(result.equals("User Already Exists")){
                                        Toast.makeText(getApplicationContext(), getString(R.string.user_exists), Toast.LENGTH_SHORT).show();
                                    } else if(result.equals("Error: Database connection")) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.db_connection_err), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(), getString(R.string.fields_req), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Otwarcie aktywności logowania
    public void openLogin() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

}