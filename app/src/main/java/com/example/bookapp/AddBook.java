package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookapp.databinding.ActivityAddBookBinding;
import com.example.bookapp.databinding.ActivityYourBooksBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.HashMap;
import java.util.Map;

public class AddBook extends Drawer_base {

    ActivityAddBookBinding activityAddBookBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddBookBinding = activityAddBookBinding.inflate(getLayoutInflater());
        setContentView(activityAddBookBinding.getRoot());
        allocateActivityTitle("");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextView tvBookName = findViewById(R.id.book_name);
        TextView tvBookAuthor =  findViewById(R.id.book_author);
        TextView tvBookPages = findViewById(R.id.book_pages);
        TextView tvPublisher =  findViewById(R.id.publisher);
        TextView tvYear = findViewById(R.id.year);

        MaterialButton addBookbtn = findViewById(R.id.addBookbtn);

        // Funkcja odpowiadająca za dodanie książki do bazy i przypisanie jej uzytkownikowi
        addBookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, author, pages, publisher, year;

//                final String[] id = new String[1];
//                SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
//                id[0] = preferences.getString("id", "");

                name = tvBookName.getText().toString();
                author = tvBookAuthor.getText().toString();
                pages = tvBookPages.getText().toString();
                publisher = tvPublisher.getText().toString();
                year = tvYear.getText().toString();


                if (!name.isEmpty() && !author.isEmpty() && !pages.isEmpty() && !publisher.isEmpty() && !year.isEmpty()) {
                    if (TextUtils.isDigitsOnly(pages)) {
                    db.collection("books")
                            .whereEqualTo("name", name)
                            .whereEqualTo("publisher", publisher)
                            .whereEqualTo("year", year)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().isEmpty()) {

                                            Map<String, Object> book = new HashMap<>();
                                            book.put("name", name);
                                            book.put("author", author);
                                            book.put("pages", pages);
                                            book.put("publisher", publisher);
                                            book.put("year", year);

                                            db.collection("books")
                                                    .add(book)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(getApplicationContext(), getString(R.string.book_added), Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getApplicationContext(), getString(R.string.book_add_err), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else Toast.makeText(getApplicationContext(), getString(R.string.book_exists), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    } else Toast.makeText(getApplicationContext(), "Strony to liczby", Toast.LENGTH_SHORT).show();
                } else  Toast.makeText(getApplicationContext(), getString(R.string.fields_req), Toast.LENGTH_SHORT).show();


//                if (!name.equals("") && !author.equals("") && !pages.equals("")) {
//                    Handler handler = new Handler(Looper.getMainLooper());
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            String[] field = new String[5];
//                            field[0] = "name";
//                            field[1] = "author";
//                            field[2] = "pages";
//                            field[3] = "ean";
//                            field[4] = "userId";
//                            //Creating array for data
//                            String[] data = new String[5];
//                            data[0] = name;
//                            data[1] = author;
//                            data[2] = pages;
//                            data[3] = ean;
//                            data[4] = id[0];
//
//                            PutData putData = new PutData("https://grpcapi.bieda.it/LoginBook/addBook.php", "POST", field, data);
//                            if (putData.startPut()) {
//                                if (putData.onComplete()) {
//                                    String result = putData.getResult();
//                                    if (result.equals("Book Added and Assigned")){
//                                        Toast.makeText(getApplicationContext(), getString(R.string.book_added), Toast.LENGTH_SHORT).show();
//                                    } else if (result.equals("Book Added but Not Assigned")) {
//                                        Toast.makeText(getApplicationContext(), getString(R.string.book_added_not_assigned), Toast.LENGTH_SHORT).show();
//                                    }  else if (result.equals("Book Add Failed")) {
//                                        Toast.makeText(getApplicationContext(), getString(R.string.book_add_err), Toast.LENGTH_SHORT).show();
//                                    } else if (result.equals("Book Exists")) {
//                                        Toast.makeText(getApplicationContext(), getString(R.string.book_exists), Toast.LENGTH_SHORT).show();
//                                    } else if (result.equals("Error: Database connection")) {
//                                        Toast.makeText(getApplicationContext(), getString(R.string.db_connection_err), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }
//                        }
//                    });
//                } else {
//                    Toast.makeText(getApplicationContext(), getString(R.string.fields_req), Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }
}