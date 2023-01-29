package com.example.bookapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bookapp.databinding.ActivityChangeUsernameBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ChangeUsername extends Drawer_base {

    ActivityChangeUsernameBinding activityChangeUsernameBinding;
    TextView tvUsername, tvPassword;
    MaterialButton changebtn;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChangeUsernameBinding = activityChangeUsernameBinding.inflate(getLayoutInflater());
        setContentView(activityChangeUsernameBinding.getRoot());
        allocateActivityTitle("");

        changebtn = findViewById(R.id.changebtn);

        changebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeUsername();

            }
        });
    }

    public void changeUsername() {

        mAuth = FirebaseAuth.getInstance();

        tvUsername = findViewById(R.id.username);
        tvPassword = findViewById(R.id.password);

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String userId = preferences.getString("id", "");
        String userEmail = preferences.getString("email", "");

        String username = tvUsername.getText().toString();
        String password = tvPassword.getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {

            db.collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                if (task.getResult().isEmpty()) {


                                    mAuth.signInWithEmailAndPassword(userEmail, password)
                                            .addOnCompleteListener(ChangeUsername.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {

                                                        db.collection("users").document(userId)
                                                                .update(
                                                                        "username", username
                                                                );

                                                        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = preferences.edit();
                                                        editor.putString("username", username);
                                                        editor.apply();


                                                        Toast.makeText(ChangeUsername.this, getString(R.string.username_changed), Toast.LENGTH_SHORT).show();
                                                        openMain();

                                                    } else {
                                                        Toast.makeText(getApplicationContext(), getString(R.string.wrong_password), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else
                                    Toast.makeText(getApplicationContext(), getString(R.string.user_exists), Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(ChangeUsername.this, getString(R.string.fields_req), Toast.LENGTH_SHORT).show();
        }
    }

    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
