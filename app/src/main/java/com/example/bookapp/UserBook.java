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

import com.example.bookapp.databinding.ActivityUserBookBinding;
import com.example.bookapp.databinding.ActivityYourBooksBinding;
import com.google.android.material.button.MaterialButton;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.w3c.dom.Text;

public class UserBook extends Drawer_base implements NumberPicker.OnValueChangeListener {

    String title, id, pages, actualPage;
    TextView tvTitle, tvPages, tvActualPage;
    MaterialButton acceptbtn, cancelbtn;
    ActivityUserBookBinding activityUserBookBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserBookBinding = ActivityUserBookBinding.inflate(getLayoutInflater());
        setContentView(activityUserBookBinding.getRoot());
        allocateActivityTitle("");

        tvTitle = findViewById(R.id.book_title);
        tvPages = findViewById(R.id.pages);
        tvActualPage = findViewById(R.id.actual_page);

        acceptbtn = findViewById(R.id.accept);
        cancelbtn = findViewById(R.id.cancel);

        getData();
        setData();

        NumberPicker numberPicker = findViewById(R.id.number_picker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(Integer.parseInt(pages));
        numberPicker.setValue(Integer.parseInt(actualPage));
        numberPicker.setOnValueChangedListener(this);

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {


                        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);

                        String[] field = new String[3];
                        field[0] = "userId";
                        field[1] = "bookId";
                        field[2] = "page";

                        String[] data = new String[3];
                        data[0] = preferences.getString("id", "");
                        data[1] = id;
                        data[2] = actualPage;

                        PutData putData = new PutData("https://grpcapi.bieda.it/LoginBook/updatePage.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if(result.equals("Update Success")){
                                    Toast.makeText(getApplicationContext(), getString(R.string.page_updated), Toast.LENGTH_SHORT).show();
                                    openYourBooks();
                                } else if (result.equals("Update Failed")) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.page_updated_err), Toast.LENGTH_SHORT).show();
                                    openYourBooks();
                                } else if(result.equals("Error: Database connection")) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.db_connection_err), Toast.LENGTH_SHORT).show();
                                    openYourBooks();
                                }
                            }
                        }
                    }
                });
            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openYourBooks();
            }
        });
    }



    private void getData() {
        if (getIntent().hasExtra("titles") && getIntent().hasExtra("ids") && getIntent().hasExtra("pages") &&
                getIntent().hasExtra("actualPage")) {

            title = getIntent().getStringExtra("titles");
            id = getIntent().getStringExtra("ids");
            pages = getIntent().getStringExtra("pages");
            actualPage = getIntent().getStringExtra("actualPage");

        } else {
            Toast.makeText(this, getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {

        tvTitle.setText(title);
        tvPages.setText(getString(R.string.pages) + " " + pages);
        tvActualPage.setText(getString(R.string.user_page) + " " + actualPage);
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {

        tvActualPage.setText(getString(R.string.user_page) + " " + i1);
        actualPage = String.valueOf(i1);

    }

    public void openYourBooks() {
        Intent intent = new Intent(this, YourBooks.class);
        startActivity(intent);
    }
}