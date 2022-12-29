package com.example.bookapp;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;



public class MainActivity extends Drawer_base {
    String [] titles;
    String[] pages = null;
    String [] ids = null;
    String [] actualPage = null;

    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        allocateActivityTitle("");

        TextView tvwelcome = findViewById(R.id.main);

        MaterialButton base = findViewById(R.id.base);


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String userId = preferences.getString("id", "");
        tvwelcome.append(" " + preferences.getString("username", ""));

            base.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                    String userId = preferences.getString("id", "");


                    DocumentReference user = db.collection("users").document(userId);

                    user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                    Map<String, Object> userBooks = new HashMap<>();
                                    userBooks = (Map<String, Object>) document.get("book");
                                    Object[] booksIds = userBooks.keySet().toArray();

                                    Map<String, Object> booksData = new HashMap<>();

                                    titles = new String[booksIds.length];
                                    pages = new String[booksIds.length];
                                    ids = new String[booksIds.length];
                                    actualPage = new String[booksIds.length];

                                    int i;

                                    for (i = 0; i <= 1; i++) {

                                        booksData = (Map<String, Object>) userBooks.get(booksIds[i]);
                                        actualPage[i] = String.valueOf(booksData.get("page"));
                                        ids[i] = String.valueOf(booksIds[i]);

                                        DocumentReference book = db.collection("books").document(String.valueOf(booksIds[i]));

                                        int finalI = i;
                                        book.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {

                                                        titles[finalI] = document.getString("name");

//                                                        Toast.makeText(getApplicationContext(), "To ma byc 1 " + titles[finalI], Toast.LENGTH_SHORT).show();

                                                        pages[finalI] = "dupa";



                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Nie działa" + userId, Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Nie działa333", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    if (i >= booksIds.length) {
                                        Toast.makeText(getApplicationContext(), "Dupa " + titles[1], Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Nie działa" + userId, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Nie działa333", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    Toast.makeText(getApplicationContext(), "Giga test " + titles[1], Toast.LENGTH_SHORT).show();


//                    boolean check = false;
//
//                    DocumentReference user = db.collection("users").document(userId);
//
//                    user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                if (document.exists()) {
//                                    Map<String, Object> userBooks = new HashMap<>();
//                                    Map<String, Object> booksData = new HashMap<>();
//                                    userBooks = (Map<String, Object>) document.get("book");
//                                    Object[] ids = userBooks.keySet().toArray();
//                                    booksData = (Map<String, Object>) userBooks.get(ids[0]);
//                                    String pages = String.valueOf(booksData.get("page"));
//                                    Toast.makeText(getApplicationContext(), "Dziala: " + pages, Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(getApplicationContext(), "Nie działa" + userId, Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Nie działa333", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

//                    db.collection("test")
//                            .get()
//                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    if (task.isSuccessful()) {
//                                        if (task.getResult().isEmpty()) {
//                                            Toast.makeText(getApplicationContext(), "Walidacja gites", Toast.LENGTH_SHORT).show();
//                                        }
//                                        for (QueryDocumentSnapshot document : task.getResult()) {
//                                            Toast.makeText(getApplicationContext(), "Dziala: " + document.getString("mes"), Toast.LENGTH_SHORT).show();
//                                        }
//                                    } else {
//                                        Toast.makeText(getApplicationContext(), "Nie działa", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });

                }
            });
    }
}