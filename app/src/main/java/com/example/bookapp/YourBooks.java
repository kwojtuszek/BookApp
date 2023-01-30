package com.example.bookapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.databinding.ActivityYourBooksBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YourBooks extends Drawer_base {

    ActivityYourBooksBinding activityYourBooksBinding;
    RecyclerView recyclerView;
    TextView noUserBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityYourBooksBinding = ActivityYourBooksBinding.inflate(getLayoutInflater());
        setContentView(activityYourBooksBinding.getRoot());
        allocateActivityTitle(getString(R.string.user_books));

        startView();
    }

    public void startView() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        noUserBooks = findViewById(R.id.no_user_books);
        recyclerView = findViewById(R.id.recyclerView);

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
                        ArrayList check = new ArrayList<>();

                        for (int j = 0; j < booksIds.length; j++) {
                            booksData = (Map<String, Object>) userBooks.get(booksIds[j]);

                            if (Integer.parseInt(String.valueOf(booksData.get("page"))) != 0) {
                                check.add(booksIds[j]);
                            }
                        }

                        String[] titles = new String[check.size()], pages = new String[check.size()],
                                ids = new String[check.size()], actualPage = new String[check.size()],
                                author = new String[check.size()];

                        if (check.size() != 0) {

                            for (int i = 0; i < check.size(); i++) {

                                    booksData = (Map<String, Object>) userBooks.get(check.get(i));

                                    actualPage[i] = String.valueOf(booksData.get("page"));
                                    ids[i] = String.valueOf(check.get(i));

                                    DocumentReference book = db.collection("books").document(String.valueOf(check.get(i)));

                                    int finalI = i;
                                    book.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {

                                                    titles[finalI] = document.getString("name");
                                                    pages[finalI] = document.getLong("pages").toString();
                                                    author[finalI] = document.getString("author");

                                                    RecyclerReadingBooksAdapter adapter = new RecyclerReadingBooksAdapter(YourBooks.this, titles, pages, ids, actualPage, author);
                                                    recyclerView.setAdapter(adapter);
                                                    recyclerView.setLayoutManager(new LinearLayoutManager(YourBooks.this));

                                                } else {
                                                    Toast.makeText(getApplicationContext(), getString(R.string.data_load_err) + userId, Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                        } else noUserBooks.setText(getString(R.string.no_user_books));
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}