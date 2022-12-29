package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextView tvUsername = findViewById(R.id.username);
        TextView tvPassword =  findViewById(R.id.password);

        TextView register = findViewById(R.id.register);
        TextView forgotpass = findViewById(R.id.forgotpass);

        MaterialButton login = findViewById(R.id.login);

        // Przejście do rejestracji
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPasswordReset();
            }
        });

        // Sprawdzenie czy nie użytkownik nie jest już zalogowany z wykorzystaniem SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String remember_value = preferences.getString("remember", "");
        if (remember_value.equals("true")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else if (remember_value.equals("false")) {
            Toast.makeText(Login.this, getString(R.string.please_login), Toast.LENGTH_SHORT).show();
        }

        // Funkcja odpowiadająca za logowanie
        login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String username, password;
                username = tvUsername.getText().toString();
                password = tvPassword.getText().toString();

                // Sprawdzenie czy wszystkie pola są wypełnione
                if (!username.equals("") && !password.equals("")) {

                    // Pobranie adresu email po nazwie użytkownika z Firestore
                    db.collection("users")
                            .whereEqualTo("username", username)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().isEmpty()) {
                                            Toast.makeText(getApplicationContext(), getString(R.string.bad_login), Toast.LENGTH_SHORT).show();
                                        } else {
                                            String email = "";
                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                email = document.getString("email");

                                                // Zapisanie danych użytkownika w SharedPreferences
                                                SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("remember", "true");
                                                editor.putString("username", username);
                                                editor.putString("email", document.getString("email"));
                                                editor.putString("id", document.getId());
                                                editor.apply();
                                            }

                                            // Logowanie użytkownika z wykorzystaniem Firebase Authentication
                                            mAuth.signInWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getApplicationContext(), getString(R.string.good_login), Toast.LENGTH_SHORT).show();
                                                                FirebaseUser user = mAuth.getCurrentUser();
                                                                openMain();
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), getString(R.string.bad_login), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }



    // Otwarcie aktywności rejestracjo
    public void openRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    // Otwarcie aktywności odpowiadającej za odzyskanie hasła
    public void openPasswordReset() {
        Intent intent = new Intent(getApplicationContext(), PasswordReset.class);
        startActivity(intent);
    }

    // Otwarcie aktywności po zalogowaniu
    public void openMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}