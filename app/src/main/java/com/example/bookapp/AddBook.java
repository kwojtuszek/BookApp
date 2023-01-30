package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookapp.databinding.ActivityAddBookBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.HashMap;
import java.util.Map;

public class AddBook extends Drawer_base {

    ActivityAddBookBinding activityAddBookBinding;

    String name, author, pages, publisher, year, userId;
    TextView tvBookName, tvBookAuthor, tvBookPages, tvPublisher, tvYear;
    MaterialButton addBookbtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddBookBinding = activityAddBookBinding.inflate(getLayoutInflater());
        setContentView(activityAddBookBinding.getRoot());
        allocateActivityTitle("");

        tvBookName = findViewById(R.id.book_name);
        tvBookAuthor = findViewById(R.id.book_author);
        tvBookPages = findViewById(R.id.book_pages);
        tvPublisher = findViewById(R.id.publisher);
        tvYear = findViewById(R.id.year);

        addBookbtn = findViewById(R.id.addBookbtn);


        // Funkcja odpowiadająca za dodanie książki do bazy i przypisanie jej uzytkownikowi
        addBookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addBook();
            }
        });
    }

    public void addBook() {

        name = tvBookName.getText().toString();
        author = tvBookAuthor.getText().toString();
        pages = tvBookPages.getText().toString();
        publisher = tvPublisher.getText().toString();
        year = tvYear.getText().toString();


        // Pobranie id użytkownika z SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        userId = preferences.getString("id", "");


        // Waidacja czy wszystkie pola są wypełnine
        if (!name.isEmpty() && !author.isEmpty() && !pages.isEmpty() && !publisher.isEmpty() && !year.isEmpty()) {

            // Sprawdzenie czy numer strony i rok są liczbami
            if (TextUtils.isDigitsOnly(pages) && TextUtils.isDigitsOnly(year)) {

                // Sprawdzenie czy taka książka nie jest już w bazie
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

                                        // Pzzygotowanie obiektu książki
                                        Map<String, Object> book = new HashMap<>();
                                        book.put("name", name);
                                        book.put("nameSearch", name.toLowerCase());
                                        book.put("author", author);
                                        book.put("pages", Integer.parseInt(pages));
                                        book.put("publisher", publisher);
                                        book.put("year", year);
                                        book.put("rate", 0);
                                        book.put("rates", 0);

                                        // Dodanie książki
                                        db.collection("books")
                                                .add(book)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {

                                                        // Pobranie id dodanej książki
                                                        db.collection("books")
                                                                .whereEqualTo("name", name)
                                                                .whereEqualTo("author", author)
                                                                .whereEqualTo("publisher", publisher)
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            String bookId = "";
                                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                bookId = document.getId();
                                                                            }

                                                                            // Dane do przypisania w użytkowniku dla książki
                                                                            Map<String, Object> bookData = new HashMap<>();
                                                                            bookData.put("rate", 0);
                                                                            bookData.put("page", 1);
                                                                            bookData.put("reads", 0);

                                                                            // Klucz mapy do znalezienia przypisanych danych
                                                                            Map<String, Object> preparedBook = new HashMap<>();
                                                                            preparedBook.put(bookId, bookData);

                                                                            // Wskazanie mapy w której przypisana książka i jej dane mają się zapisać
                                                                            Map<String, Object> assignBook = new HashMap<>();
                                                                            assignBook.put("book", preparedBook);

                                                                            // Przypisanie danych
                                                                            db.collection("users").document(userId)
                                                                                    .set(assignBook, SetOptions.merge());

                                                                            Toast.makeText(getApplicationContext(), getString(R.string.book_added), Toast.LENGTH_SHORT).show();
                                                                            openYourBooks();

                                                                        } else {
                                                                            Toast.makeText(getApplicationContext(), getString(R.string.book_added_not_assigned), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), getString(R.string.book_add_err), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else
                                        Toast.makeText(getApplicationContext(), getString(R.string.book_exists), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else
                Toast.makeText(getApplicationContext(), getString(R.string.pages_validate), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.fields_req), Toast.LENGTH_SHORT).show();
    }

    public void openYourBooks() {

        Intent intent = new Intent(getApplicationContext(), YourBooks.class);
        startActivity(intent);
    }

}