package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookapp.databinding.ActivityAssignBookBinding;
import com.google.android.material.button.MaterialButton;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class AssignBook extends Drawer_base {

    String title, id, pages, ean, author;
    TextView tvTitle, tvPages, tvEan, tvAuthor;
    MaterialButton assignbtn;
    ActivityAssignBookBinding activityAssignBookBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAssignBookBinding = ActivityAssignBookBinding.inflate(getLayoutInflater());
        setContentView(activityAssignBookBinding.getRoot());
        allocateActivityTitle("");

        tvTitle = findViewById(R.id.book_title);
        tvPages = findViewById(R.id.pages);
        tvEan = findViewById(R.id.ean);
        tvAuthor = findViewById(R.id.author);

        assignbtn = findViewById(R.id.assign);

        getData();
        setData();

        assignbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);

                        String[] field = new String[2];
                        field[0] = "userId";
                        field[1] = "bookId";

                        String[] data = new String[2];
                        data[0] = preferences.getString("id", "");
                        data[1] = id;

                        PutData putData = new PutData("https://grpcapi.bieda.it/LoginBook/assignBook.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if(result.equals("Book Assigned")){
                                    Toast.makeText(getApplicationContext(), getString(R.string.book_assigned), Toast.LENGTH_SHORT).show();
                                    openFindBook();
                                } else if (result.equals("Book Assigned failed")) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.book_assigned_err), Toast.LENGTH_SHORT).show();
                                    openFindBook();
                                } else if(result.equals("Error: Database connection")) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.db_connection_err), Toast.LENGTH_SHORT).show();
                                    openFindBook();
                                }
                            }
                        }
                    }
                });
            }
        });

    }


    private void getData() {
        if (getIntent().hasExtra("titles") && getIntent().hasExtra("ids") && getIntent().hasExtra("pages") &&
                getIntent().hasExtra("author") && getIntent().hasExtra("ean")) {

            title = getIntent().getStringExtra("titles");
            id = getIntent().getStringExtra("ids");
            pages = getIntent().getStringExtra("pages");
            author = getIntent().getStringExtra("author");
            ean = getIntent().getStringExtra("ean");

        } else {
            Toast.makeText(this, getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {

        tvTitle.setText(title);
        tvPages.setText(getString(R.string.pages) + " " + pages);
        tvAuthor.setText(getString(R.string.author) + " " + author);
        tvEan.setText("EAN: " + ean);
    }


    public void openFindBook() {
        Intent intent = new Intent(this, FindBook.class);
        startActivity(intent);
    }
}