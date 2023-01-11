package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookapp.databinding.ActivityUserBookBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserBook extends Drawer_base {

    String title, id, pages, actualPage, author;
    TextView tvTitle, tvPagePages, tvAuthor;
    int progress;
    MaterialButton readedbtn;
    ProgressBar progress_bar;
    ActivityUserBookBinding activityUserBookBinding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserBookBinding = ActivityUserBookBinding.inflate(getLayoutInflater());
        setContentView(activityUserBookBinding.getRoot());
        allocateActivityTitle("");

        tvTitle = findViewById(R.id.book_title);
        tvPagePages= findViewById(R.id.page_pages);
        tvAuthor= findViewById(R.id.author);
        progress_bar = findViewById(R.id.progress_bar);
        readedbtn = findViewById(R.id.readedbtn);

        getData();
        setData();

        tvPagePages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageChange();
            }
        });

        readedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookFinished();

            }
        });
    }

    private void getData() {
        if (getIntent().hasExtra("titles") && getIntent().hasExtra("ids") && getIntent().hasExtra("pages") &&
                getIntent().hasExtra("actualPage") && getIntent().hasExtra("author")) {

            title = getIntent().getStringExtra("titles");
            id = getIntent().getStringExtra("ids");
            pages = getIntent().getStringExtra("pages");
            actualPage = getIntent().getStringExtra("actualPage");
            author = getIntent().getStringExtra("author");

        } else {
            Toast.makeText(this, getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {

        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvPagePages.setText(actualPage + "/" + pages);

        progress = (Integer.parseInt(actualPage)*100)/Integer.parseInt(pages);

        progress_bar.setProgress(progress);

    }

    public void pageChange() {

        AlertDialog.Builder pageChangeDialog = new AlertDialog.Builder(UserBook.this);
        pageChangeDialog.setTitle(getString(R.string.page_question));

        EditText lastPageInput = new EditText(UserBook.this);
        lastPageInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        lastPageInput.setText(actualPage, TextView.BufferType.EDITABLE);

        pageChangeDialog.setView(lastPageInput);

        pageChangeDialog.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (lastPageInput.getText().toString().isEmpty() || Integer.parseInt(lastPageInput.getText().toString()) == 0) {
                    dialogInterface.cancel();
                } else {
                if (Integer.parseInt(lastPageInput.getText().toString()) < Integer.parseInt(pages)) {

                    SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                    String userId = preferences.getString("id", "");

                    actualPage = lastPageInput.getText().toString();

                    db.collection("users").document(userId)
                            .update(
                                    "book." + id + ".page", Integer.parseInt(actualPage)
                            );

                    setData();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.actual_page_err), Toast.LENGTH_SHORT).show();
            }
            }
        });

//        pageChangeDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                dialogInterface.cancel();
//            }
//        });

        pageChangeDialog.show();
    }

    public void bookFinished() {

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String userId = preferences.getString("id", "");
        int readed = preferences.getInt("readed", 0) + 1;
        int userLevel = preferences.getInt("level", 1);

        int actualLevel = levelUp(userLevel, readed);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("readed", readed);
        editor.putInt("level", actualLevel);
        editor.apply();

        DocumentReference user = db.collection("users").document(userId);

        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        int bookReads = document.getLong("book." + id + ".reads").intValue() + 1;

                        db.collection("users").document(userId)
                                .update(
                                        "book." + id + ".page", 0,
                                        "book." + id + ".reads", bookReads,
                                        "level", actualLevel,
                                        "readed", readed
                                );

                        if (document.getLong("book." + id + ".rate") != 0) {
                            openYourBooks();
                        } else {
                            openRateBook();
                        }
                    } else
                        Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int levelUp(int actualLevel, int readed) {

        switch (readed) {
            case 10:
                actualLevel = 2;
                break;
            case 20:
                actualLevel = 3;
                break;
            case 30:
                actualLevel = 4;
                break;
            case 40:
                actualLevel = 5;
                break;
            case 50:
                actualLevel = 6;
                break;
            case 60:
                actualLevel = 7;
                break;
            case 70:
                actualLevel = 8;
                break;
            case 80:
                actualLevel = 9;
                break;
            case 100:
                actualLevel = 10;
                break;
        }

        return actualLevel;
    }



    public void openYourBooks() {
        Intent intent = new Intent(getApplicationContext(), YourBooks.class);
        startActivity(intent);
    }

    public void openRateBook() {
        SharedPreferences passBookId = getSharedPreferences("book_id", MODE_PRIVATE);
        SharedPreferences.Editor editor = passBookId.edit();
        editor.putString("bookId", id);
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), RateBook.class);
        startActivity(intent);
    }

}