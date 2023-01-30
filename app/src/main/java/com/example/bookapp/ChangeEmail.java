package com.example.bookapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bookapp.databinding.ActivityChangeEmailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ChangeEmail extends Drawer_base {

    ActivityChangeEmailBinding activityChangeEmailBinding;
    TextView tvEmail, tvPassword;
    MaterialButton changebtn;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChangeEmailBinding = activityChangeEmailBinding.inflate(getLayoutInflater());
        setContentView(activityChangeEmailBinding.getRoot());
        allocateActivityTitle(getString(R.string.change_email));

        changebtn = findViewById(R.id.changebtn);

        changebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeEmail();
            }
        });
    }

    public void changeEmail() {

        mAuth = FirebaseAuth.getInstance();

        tvEmail = findViewById(R.id.email);
        tvPassword = findViewById(R.id.password);

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String userId = preferences.getString("id", "");
        String userOldEmail = preferences.getString("email", "");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newEmail = tvEmail.getText().toString();
        String password = tvPassword.getText().toString();

        if (!newEmail.isEmpty() && !password.isEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {

                db.collection("users")
                        .whereEqualTo("email", newEmail)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    if (task.getResult().isEmpty()) {


                                        mAuth.signInWithEmailAndPassword(userOldEmail, password)
                                                .addOnCompleteListener(ChangeEmail.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {

                                                            db.collection("users").document(userId)
                                                                    .update(
                                                                            "email", newEmail
                                                                    );

                                                            SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = preferences.edit();
                                                            editor.putString("email", newEmail);
                                                            editor.apply();

                                                            user.updateEmail(newEmail)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(ChangeEmail.this, getString(R.string.email_changed), Toast.LENGTH_SHORT).show();
                                                                                openMain();
                                                                            }
                                                                        }
                                                                    });

                                                        } else {
                                                            Toast.makeText(getApplicationContext(), getString(R.string.wrong_password), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else
                                        Toast.makeText(getApplicationContext(), getString(R.string.email_exists), Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else
                Toast.makeText(getApplicationContext(), getString(R.string.email_valid), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(ChangeEmail.this, getString(R.string.fields_req), Toast.LENGTH_SHORT).show();
    }

    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}