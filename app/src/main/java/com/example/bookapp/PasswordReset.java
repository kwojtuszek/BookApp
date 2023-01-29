package com.example.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordReset extends AppCompatActivity {

    TextView tvEmail;
    MaterialButton resetbtn;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        resetbtn = findViewById(R.id.resetbtn);

        tvEmail = findViewById(R.id.email);

        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetPassword();
            }
        });
    }

    public void resetPassword() {

        tvEmail = findViewById(R.id.email);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String emailAddress = tvEmail.getText().toString();

        if (!emailAddress.isEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                mAuth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PasswordReset.this, getString(R.string.email_sent), Toast.LENGTH_SHORT).show();
                                    openLogin();
                                } else {
                                    Toast.makeText(PasswordReset.this, getString(R.string.email_not_exist), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else Toast.makeText(PasswordReset.this, getString(R.string.email_valid), Toast.LENGTH_SHORT).show();
        } else Toast.makeText(PasswordReset.this, getString(R.string.fields_req), Toast.LENGTH_SHORT).show();
    }

    // Otwarcie aktywności logowania
    public void openLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
