package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookapp.databinding.ActivityFindBookBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FindBook extends Drawer_base {

    ActivityFindBookBinding activityFindBookBinding;
    RecyclerView recyclerView;
    TextView allBooksAssigned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFindBookBinding = ActivityFindBookBinding.inflate(getLayoutInflater());
        setContentView(activityFindBookBinding.getRoot());
        allocateActivityTitle("");

        EditText search = findViewById(R.id.book_name);

        startView();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                findBook(search.getText().toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void startView() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        allBooksAssigned = findViewById(R.id.no_user_books);
        recyclerView = findViewById(R.id.recyclerView);

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String userId = preferences.getString("id", "");

        DocumentReference user = db.collection("users").document(userId);

        CollectionReference collection = db.collection("books");
        Query query = collection;
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                long size = snapshot.getCount();

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

                                ArrayList<String> arrayNames = new ArrayList<String>();
                                ArrayList<String> arrayPages = new ArrayList<String>();
                                ArrayList<String> arrayIds = new ArrayList<String>();
                                ArrayList<String> arrayRates = new ArrayList<String>();
                                ArrayList<String> arrayAuthors = new ArrayList<String>();

                                db.collection("books")
                                        .orderBy("nameSearch")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        boolean isReading = false;

                                                        for (int i = 0; i < check.size(); i++) {
                                                            if (String.valueOf(check.get(i)).equals(document.getId())) {
                                                                isReading = true;
                                                            }
                                                        }

                                                        if (!isReading) {

                                                            arrayNames.add(document.getString("name"));
                                                            arrayAuthors.add(document.getString("author"));
                                                            arrayIds.add(document.getId());
                                                            arrayRates.add(document.getLong("rates").toString());
                                                            arrayPages.add(document.getLong("pages").toString());
                                                        }

                                                        RecyclerNotAssignedBooksAdapter adapter = new RecyclerNotAssignedBooksAdapter(FindBook.this, arrayNames, arrayPages, arrayIds, arrayRates, arrayAuthors);
                                                        recyclerView.setAdapter(adapter);
                                                        recyclerView.setLayoutManager(new LinearLayoutManager(FindBook.this));
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });



                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {
            }
        });
    }


    public void findBook(String name) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        allBooksAssigned = findViewById(R.id.no_user_books);
        recyclerView = findViewById(R.id.recyclerView);

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String userId = preferences.getString("id", "");

        DocumentReference user = db.collection("users").document(userId);

        CollectionReference collection = db.collection("books");
        Query query = collection;
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                long size = snapshot.getCount();

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

                                ArrayList<String> arrayNames = new ArrayList<String>();
                                ArrayList<String> arrayPages = new ArrayList<String>();
                                ArrayList<String> arrayIds = new ArrayList<String>();
                                ArrayList<String> arrayRates = new ArrayList<String>();
                                ArrayList<String> arrayAuthors = new ArrayList<String>();

                                db.collection("books")
                                        .orderBy("nameSearch")
                                        .whereGreaterThanOrEqualTo("nameSearch", name)
                                        .whereLessThan("nameSearch", name + "\uf8ff")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        boolean isReading = false;

                                                        for (int i = 0; i < check.size(); i++) {
                                                            if (String.valueOf(check.get(i)).equals(document.getId())) {
                                                                isReading = true;
                                                            }
                                                        }

                                                        if (!isReading) {

                                                            arrayNames.add(document.getString("name"));
                                                            arrayAuthors.add(document.getString("author"));
                                                            arrayIds.add(document.getId());
                                                            arrayRates.add(document.getLong("rates").toString());
                                                            arrayPages.add(document.getLong("pages").toString());

                                                        }

                                                        RecyclerNotAssignedBooksAdapter adapter = new RecyclerNotAssignedBooksAdapter(FindBook.this, arrayNames, arrayPages, arrayIds, arrayRates, arrayAuthors);
                                                        recyclerView.setAdapter(adapter);
                                                        recyclerView.setLayoutManager(new LinearLayoutManager(FindBook.this));
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });



                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {
            }
        });
    }
}