package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookapp.databinding.ActivityAddBookBinding;
import com.example.bookapp.databinding.ActivityYourBooksBinding;
import com.google.android.material.button.MaterialButton;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class AddBook extends Drawer_base {

    ActivityAddBookBinding activityAddBookBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddBookBinding = activityAddBookBinding.inflate(getLayoutInflater());
        setContentView(activityAddBookBinding.getRoot());
        allocateActivityTitle("");

        TextView tvBookName = (TextView) findViewById(R.id.book_name);
        TextView tvBookAuthor = (TextView) findViewById(R.id.book_author);
        TextView tvBookPages = (TextView) findViewById(R.id.book_pages);
        TextView tvBookEan = (TextView) findViewById(R.id.ean);

        MaterialButton addBookbtn = (MaterialButton) findViewById(R.id.addBookbtn);

        // Funkcja odpowiadająca za dodanie książki do bazy i przypisanie jej uzytkownikowi
        addBookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, author, pages, ean;
                final String[] id = new String[1];
                SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                id[0] = preferences.getString("id", "");
                name = tvBookName.getText().toString();
                author = tvBookAuthor.getText().toString();
                pages = tvBookPages.getText().toString();
                ean = tvBookEan.getText().toString();

                if (!name.equals("") && !author.equals("") && !pages.equals("")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[5];
                            field[0] = "name";
                            field[1] = "author";
                            field[2] = "pages";
                            field[3] = "ean";
                            field[4] = "userId";
                            //Creating array for data
                            String[] data = new String[5];
                            data[0] = name;
                            data[1] = author;
                            data[2] = pages;
                            data[3] = ean;
                            data[4] = id[0];

                            PutData putData = new PutData("https://grpcapi.bieda.it/LoginBook/addBook.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (result.equals("Book Added and Assigned")){
                                        Toast.makeText(getApplicationContext(), getString(R.string.book_added), Toast.LENGTH_SHORT).show();
                                    } else if (result.equals("Book Added but Not Assigned")) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.book_added_not_assigned), Toast.LENGTH_SHORT).show();
                                    }  else if (result.equals("Book Add Failed")) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.book_add_err), Toast.LENGTH_SHORT).show();
                                    } else if (result.equals("Book Exists")) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.book_exists), Toast.LENGTH_SHORT).show();
                                    } else if (result.equals("Error: Database connection")) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.db_connection_err), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.fields_req), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}