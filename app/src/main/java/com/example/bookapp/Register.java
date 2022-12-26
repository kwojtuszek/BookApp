package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView tvUsername = findViewById(R.id.username);
        TextView tvPassword = findViewById(R.id.password);
        TextView tvEmail = findViewById(R.id.email);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        MaterialButton registerbtn = findViewById(R.id.registerbtn);

        // Funkcja rejestracji
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                String username, password, email;
                username = tvUsername.getText().toString();
                password = tvPassword.getText().toString();
                email = tvEmail.getText().toString();

                if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        if (password.length() >= 6) {

                            // Rejestracja w Firebase Authentication
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information

                                                //Tworzenie dokumentu użytkownika
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("name", username);
                                                user.put("email", email);
                                                user.put("level", 1);
                                                user.put("rated", null);
                                                user.put("readed", null);
                                                user.put("reading", null);

                                                // Dodawanie użytkownika do Firestora
                                                db.collection("users")
                                                        .add(user)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Toast.makeText(getApplicationContext(), "działa", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(Register.this, Login.class));
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getApplicationContext(), "nie działa", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Toast.makeText(getApplicationContext(), "Nie działa", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else Toast.makeText(getApplicationContext(), "Hasło musi posiadać conajmniej 6 znaków!", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getApplicationContext(), "Wpisz poprawny adres email!", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getApplicationContext(), getString(R.string.fields_req), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Otwarcie aktywności logowania
    public void openLogin() {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

}