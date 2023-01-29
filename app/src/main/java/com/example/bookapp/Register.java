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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextView tvUsername, tvPassword, tvEmail;
    MaterialButton registerbtn;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerbtn = findViewById(R.id.registerbtn);

        // Funkcja rejestracji
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();
            }
        });
    }

    public void register() {

        mAuth = FirebaseAuth.getInstance();

        tvUsername = findViewById(R.id.username);
        tvPassword = findViewById(R.id.password);
        tvEmail = findViewById(R.id.email);

        String username, password, email;
        username = tvUsername.getText().toString();
        password = tvPassword.getText().toString();
        email = tvEmail.getText().toString();
        boolean checkUsername = false;

        if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty()) {

            db.collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                if (task.getResult().isEmpty()) {

                                    // Walidacja adresu email
                                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                        if (password.length() >= 6) {

                                            // Rejestracja w Firebase Authentication
                                            mAuth.createUserWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()) {
                                                                // Sign in success, update UI with the signed-in user's information

                                                                // Tworzenie zagnieżdzonych obiektów Mapy w Firestore
                                                                Map<String, Object> book = new HashMap<>();

                                                                //Tworzenie dokumentu użytkownika
                                                                Map<String, Object> user = new HashMap<>();
                                                                user.put("username", username);
                                                                user.put("email", email);
                                                                user.put("level", 1);
                                                                user.put("book", book);
                                                                user.put("readed", 0);
                                                                user.put("notifyTime", 8);
                                                                user.put("pet", "lion");

                                                                // Dodawanie użytkownika do Firestora
                                                                db.collection("users")
                                                                        .add(user)
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {
                                                                                Toast.makeText(getApplicationContext(), getString(R.string.good_register), Toast.LENGTH_SHORT).show();
                                                                                openLogin();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(getApplicationContext(), getString(R.string.register_err), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            } else {
                                                                // If sign in fails, display a message to the user.
                                                                Toast.makeText(getApplicationContext(), getString(R.string.email_exists), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                        } else
                                            Toast.makeText(getApplicationContext(), getString(R.string.pass_lenght), Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(getApplicationContext(), getString(R.string.email_valid), Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getApplicationContext(), getString(R.string.user_exists), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.register_err), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.fields_req), Toast.LENGTH_SHORT).show();
    }

    // Otwarcie aktywności logowania
    public void openLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

}