package com.example.bookapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.databinding.ActivityYourBooksBinding;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class YourBooks extends Drawer_base {

    ActivityYourBooksBinding activityYourBooksBinding;
    RecyclerView recyclerView;
    TextView noUserBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityYourBooksBinding = ActivityYourBooksBinding.inflate(getLayoutInflater());
        setContentView(activityYourBooksBinding.getRoot());
        allocateActivityTitle("");

        noUserBooks = findViewById(R.id.no_user_books);

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                recyclerView = findViewById(R.id.recyclerView);

                String[] field = new String[1];
                field[0] = "userId";

                String[] data = new String[1];
                data[0] = preferences.getString("id", "");

                PutData putData = new PutData("https://grpcapi.bieda.it/LoginBook/getUserBooks.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();

                        String[] books = null;
                        books = result.split(";");

                        if (result == "") {
                            noUserBooks.setText(getString(R.string.no_user_books));
                        } else {

                            String titles[] = new String[books.length / 6], authors[] = new String[books.length / 6],
                                    pages[] = new String[books.length / 6], eans[] = new String[books.length / 6],
                                    ids[] = new String[books.length / 6], actualPage[] = new String[books.length / 6];

                            int j = 0;

                            for (int i = 0; i < books.length; i += 6) {
                                titles[j] = books[i];
                                authors[j] = books[i + 1];
                                pages[j] = books[i + 2];
                                eans[j] = books[i + 3];
                                ids[j] = books[i + 4];
                                actualPage[j] = books[i + 5];
                                j++;
                            }
                            RecyclerUserBooksAdapter adapter = new RecyclerUserBooksAdapter(YourBooks.this, titles, authors, pages, eans, ids, actualPage);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(YourBooks.this));
                        }
                    }
                }
            }
        });
    }
}