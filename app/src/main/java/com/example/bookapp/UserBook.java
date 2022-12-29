package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookapp.databinding.ActivityUserBookBinding;
import com.example.bookapp.databinding.ActivityYourBooksBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.w3c.dom.Text;

import java.util.ArrayList;
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

        final EditText lastPageInput = new EditText(UserBook.this);
        lastPageInput.setInputType(InputType.TYPE_CLASS_NUMBER);

        pageChangeDialog.setView(lastPageInput);

        pageChangeDialog.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (Integer.parseInt(lastPageInput.getText().toString()) < Integer.parseInt(pages)) {

                    SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                    String userId = preferences.getString("id", "");

                    actualPage = lastPageInput.getText().toString();

                    db.collection("users").document(userId)
                            .update(
                                    "book." + id + ".page", actualPage
                            );

                    setData();
                } else Toast.makeText(getApplicationContext(), getString(R.string.actual_page_err), Toast.LENGTH_SHORT).show();
            }
        });

        pageChangeDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        });

        pageChangeDialog.show();
    }

    public void bookFinished() {

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String userId = preferences.getString("id", "");

        DocumentReference user = db.collection("users").document(userId);

        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        int reads;
                        Map<String, Object> userBooks = new HashMap<>();

                        userBooks = (Map<String, Object>) document.get("book");
                        Map<String, Object> booksData = new HashMap<>();
                        booksData = (Map<String, Object>) userBooks.get(id);

                        reads = (Integer.parseInt(String.valueOf(booksData.get("reads")))) + 1;

                        db.collection("users").document(userId)
                                .update(
                                        "book." + id + ".page", 0,
                                        "book." + id + ".reads", reads
                                );

                        if (Integer.parseInt(String.valueOf(booksData.get("rate"))) != 0) {
                            Toast.makeText(getApplicationContext(), "Losowe", Toast.LENGTH_SHORT).show();
                        } else {
                            openYourBooks();
                        }
                    } else
                        Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openYourBooks() {
        Intent intent = new Intent(getApplicationContext(), YourBooks.class);
        startActivity(intent);
    }

}