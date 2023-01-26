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
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Drawer_base {
    String[] titles;
    String[] pages = null;
    String[] ids = null;
    String[] actualPage = null;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

//        base.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
//                String userId = preferences.getString("id", "");
//
//                DocumentReference user = db.collection("users").document(userId);
//
//                CollectionReference collection = db.collection("books");
//                Query query = collection;
//                AggregateQuery countQuery = query.count();
//                countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        AggregateQuerySnapshot snapshot = task.getResult();
//                        long size = snapshot.getCount();
//
//                        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    DocumentSnapshot document = task.getResult();
//                                    if (document.exists()) {
//
//                                        Map<String, Object> userBooks = new HashMap<>();
//                                        userBooks = (Map<String, Object>) document.get("book");
//                                        Object[] booksIds = userBooks.keySet().toArray();
//
//                                        Map<String, Object> booksData = new HashMap<>();
//                                        ArrayList check = new ArrayList<>();
//
//                                        for (int j = 0; j < booksIds.length; j++) {
//                                            booksData = (Map<String, Object>) userBooks.get(booksIds[j]);
//
//                                            if (Integer.parseInt(String.valueOf(booksData.get("page"))) != 0) {
//                                                check.add(booksIds[j]);
//                                            }
//                                        }
//
//                                        String[] titles = new String[(int) size], pages = new String[(int) size],
//                                                ids = new String[(int) size], rates = new String[(int) size],
//                                                authors = new String[(int) size];
//
//                                        db.collection("books")
//                                                .get()
//                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                        if (task.isSuccessful()) {
//
//                                                            int indicator = 0;
//
//                                                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                                                boolean isReading = false;
//
//                                                                for (int i = 0; i < check.size(); i++) {
//                                                                    if (String.valueOf(check.get(i)).equals(document.getId())) {
//                                                                        isReading = true;
//                                                                    }
//                                                                }
//
//                                                                if (!isReading) {
//
//                                                                    titles[indicator] = document.getString("name");
//                                                                    authors[indicator] = document.getString("author");
//                                                                    ids[indicator] = document.getId();
//                                                                    rates[indicator] = document.getLong("rate").toString();
//                                                                    pages[indicator] = document.getLong("pages").toString();
//                                                                }
//                                                            }
//                                                        } else {
//                                                            Toast.makeText(getApplicationContext(), "Nie dziala 2", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    }
//                                                });
//
//                                        RecyclerReadingBooksAdapter adapter = new RecyclerReadingBooksAdapter(FindBook.this, titles, pages, ids, rates, authors);
//                                        recyclerView.setAdapter(adapter);
//                                        recyclerView.setLayoutManager(new LinearLayoutManager(FindBook.this));
//
//
//                                    } else {
//                                        Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
//                                    }
//                                } else {
//                                    Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//
//                    } else {
//                    }
//                });


//                    SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
//                    String userId = preferences.getString("id", "");
//
//
//                    DocumentReference user = db.collection("users").document(userId);
//
//                    user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                if (document.exists()) {
//
//                                    Map<String, Object> userBooks = new HashMap<>();
//                                    userBooks = (Map<String, Object>) document.get("book");
//                                    Object[] booksIds = userBooks.keySet().toArray();
//
//                                    Map<String, Object> booksData = new HashMap<>();
//
//                                    titles = new String[booksIds.length];
//                                    pages = new String[booksIds.length];
//                                    ids = new String[booksIds.length];
//                                    actualPage = new String[booksIds.length];
//
//                                    int i;
//
//                                    for (i = 0; i <= 1; i++) {
//
//                                        booksData = (Map<String, Object>) userBooks.get(booksIds[i]);
//                                        actualPage[i] = String.valueOf(booksData.get("page"));
//                                        ids[i] = String.valueOf(booksIds[i]);
//
//                                        DocumentReference book = db.collection("books").document(String.valueOf(booksIds[i]));
//
//                                        int finalI = i;
//                                        book.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                if (task.isSuccessful()) {
//                                                    DocumentSnapshot document = task.getResult();
//                                                    if (document.exists()) {
//
//                                                        titles[finalI] = document.getString("name");
//
////                                                        Toast.makeText(getApplicationContext(), "To ma byc 1 " + titles[finalI], Toast.LENGTH_SHORT).show();
//
//                                                        pages[finalI] = "dupa";
//
//
//
//                                                    } else {
//                                                        Toast.makeText(getApplicationContext(), "Nie działa" + userId, Toast.LENGTH_SHORT).show();
//                                                    }
//                                                } else {
//                                                    Toast.makeText(getApplicationContext(), "Nie działa333", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//                                    }
//                                    if (i >= booksIds.length) {
//                                        Toast.makeText(getApplicationContext(), "Dupa " + titles[1], Toast.LENGTH_SHORT).show();
//                                    }
//                                } else {
//                                    Toast.makeText(getApplicationContext(), "Nie działa" + userId, Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Nie działa333", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//
//                    Toast.makeText(getApplicationContext(), "Giga test " + titles[1], Toast.LENGTH_SHORT).show();


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

//            }
//        });
//    }

    }
}