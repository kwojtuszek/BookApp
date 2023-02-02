package com.example.bookapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RateBook extends Drawer_base {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    MaterialButton ratebtn;
    RatingBar ratingBar;

    String userId, bookId;
    float userRate, oldUserRate, bookRate, checkIfFirstRate;
    int rates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_book);

        ratebtn = findViewById(R.id.ratebtn);
        ratingBar = findViewById(R.id.ratingbar);

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connectionUtility.isConnected(RateBook.this)) {
                    connectionUtility.showNoInternetConnectionAlert(RateBook.this);
                } else {
                    rateBook();
                }
            }
        });
    }

    public void rateBook() {

        userRate = ratingBar.getRating();

        if (userRate != 0) {
            SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
            SharedPreferences passBookId = getSharedPreferences("book_id", MODE_PRIVATE);
            userId = preferences.getString("id", "");
            bookId = passBookId.getString("bookId", "");


            DocumentReference userBooks = db.collection("users").document(userId);

            userBooks.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            checkIfFirstRate = document.getLong("book." + bookId + ".rate");
                            oldUserRate = document.getLong("book." + bookId + ".rate");

                            db.collection("users").document(userId)
                                    .update(
                                            "book." + bookId + ".rate", userRate
                                    );


                            if (checkIfFirstRate != 0) {
                                //Zmiana oceny

                                DocumentReference book = db.collection("books").document(bookId);

                                book.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {

                                                rates = (document.getLong("rates").intValue());
                                                bookRate = document.getLong("rate");

                                                bookRate = Math.round(((bookRate - oldUserRate) + userRate) / rates);

                                                db.collection("books").document(bookId)
                                                        .update(
                                                                "rate", bookRate,
                                                                "rates", rates
                                                        );

                                                openReadedBooks();

                                            } else {
                                                Toast.makeText(getApplicationContext(), getString(R.string.data_load_err) + userId, Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } else {
                                //Pierwsza ocena

                                DocumentReference book = db.collection("books").document(bookId);

                                book.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {

                                                rates = (document.getLong("rates").intValue()) + 1;
                                                bookRate = document.getLong("rate");

                                                bookRate = Math.round((bookRate + userRate) / rates);

                                                db.collection("books").document(bookId)
                                                        .update(
                                                                "rate", bookRate,
                                                                "rates", rates
                                                        );

                                                openYourBooks();

                                            } else {
                                                Toast.makeText(getApplicationContext(), getString(R.string.data_load_err) + userId, Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.data_load_err) + userId, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else Toast.makeText(getApplicationContext(), getString(R.string.rate_book_validate), Toast.LENGTH_SHORT).show();
    }

//  Funkcja dezaktywująca back button
    @Override
    public void onBackPressed() {

    }

    public void openYourBooks() {
        Intent intent = new Intent(getApplicationContext(), YourBooks.class);
        startActivity(intent);
    }

    public void openReadedBooks() {
        Intent intent = new Intent(getApplicationContext(), ReadedBooks.class);
        startActivity(intent);
    }
}
