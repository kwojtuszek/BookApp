package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.example.bookapp.databinding.ActivityFindBookBinding;
import com.example.bookapp.databinding.ActivityYourBooksBinding;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

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

        allBooksAssigned = findViewById(R.id.no_user_books);

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

                PutData putData = new PutData("https://grpcapi.bieda.it/LoginBook/getNotUserBooks.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();

                        String[] books = null;
                        books = result.split(";");

                        if (result == "") {
                            allBooksAssigned.setText(getString(R.string.no_user_books));
                        } else {

                            String titles[] = new String[books.length / 5], authors[] = new String[books.length / 5],
                                    pages[] = new String[books.length / 5], eans[] = new String[books.length / 5],
                                    ids[] = new String[books.length / 5];

                            int j = 0;

                            for (int i = 0; i < books.length; i += 5) {
                                titles[j] = books[i];
                                authors[j] = books[i + 1];
                                pages[j] = books[i + 2];
                                eans[j] = books[i + 3];
                                ids[j] = books[i + 4];
                                j++;
                            }
                            RecyclerNotAssignedBooksAdapter adapter = new RecyclerNotAssignedBooksAdapter(FindBook.this, titles, authors, pages, eans, ids);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(FindBook.this));
                        }
                    }
                }
            }
        });

    }
}