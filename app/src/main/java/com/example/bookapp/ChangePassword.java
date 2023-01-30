package com.example.bookapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bookapp.databinding.ActivityChangePasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePassword extends Drawer_base {

    ActivityChangePasswordBinding activityChangePasswordBinding;
    TextView tvNewPassword, tvOldPassword;
    MaterialButton changebtn;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChangePasswordBinding = activityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(activityChangePasswordBinding.getRoot());
        allocateActivityTitle(getString(R.string.change_password));


        changebtn = findViewById(R.id.changebtn);

        changebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changePassword();
            }
        });
    }

    public void changePassword() {

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String userEmail = preferences.getString("email", "");

        tvNewPassword = findViewById(R.id.new_password);
        tvOldPassword = findViewById(R.id.old_password);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = tvNewPassword.getText().toString();
        String oldPassword = tvOldPassword.getText().toString();

        if (!newPassword.isEmpty() && !oldPassword.isEmpty()) {
            if (newPassword.length() >= 6) {

                mAuth.signInWithEmailAndPassword(userEmail, oldPassword)
                        .addOnCompleteListener(ChangePassword.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    user.updatePassword(newPassword)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(ChangePassword.this, getString(R.string.password_changed), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ChangePassword.this, getString(R.string.pass_lenght), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ChangePassword.this, getString(R.string.fields_req), Toast.LENGTH_SHORT).show();
        }
    }

    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}