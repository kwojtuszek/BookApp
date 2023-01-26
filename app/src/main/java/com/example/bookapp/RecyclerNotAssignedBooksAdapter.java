package com.example.bookapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RecyclerNotAssignedBooksAdapter extends RecyclerView.Adapter<RecyclerNotAssignedBooksAdapter.viewHolder> {


    String titles[];
    String authors[];
    String pages[];
    String rate[];
    String ids[];
    Context context;

    public RecyclerNotAssignedBooksAdapter(Context ctx, String data1[], String data2[], String data3[], String data4[], String data5[]) {
        context = ctx;
        titles = data1;
        ids = data2;
        pages = data3;
        rate = data4;
        authors = data5;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_recycler_not_assigned_book_row, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.title.setText(titles[position]);
        holder.author.setText(authors[position]);
        holder.pages.setText(context.getString(R.string.pages) + " " + pages[position]);
        if (!rate[position].equals("0")) {
            holder.ratingBar.setRating(Float.parseFloat(rate[position]));
        } else {
            holder.rate.setText(context.getString(R.string.no_rate));
        }
        holder.id.setText(ids[position]);

        holder.findBookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder pageChangeDialog = new AlertDialog.Builder(context);
                pageChangeDialog.setTitle(context.getString(R.string.assign_book_alert));

                pageChangeDialog.setPositiveButton(context.getString(R.string.read_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences preferences = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
                        String userId = preferences.getString("id", "");

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference user = db.collection("users").document(userId);
                        DocumentReference book = db.collection("books").document(ids[position]);

                        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {

                                        if (document.get("book." + ids[position] + ".page") != null) {

                                            db.collection("users").document(userId)
                                                    .update(
                                                            "book." + ids[position] + ".page", 1
                                                    );

                                            Toast.makeText(context, context.getString(R.string.reading_again), Toast.LENGTH_SHORT).show();

                                            openYourBooks();

                                        } else {

                                            // Dane do przypisania w użytkowniku dla książki
                                            Map<String, Object> bookData = new HashMap<>();
                                            bookData.put("rate", 0);
                                            bookData.put("page", 1);
                                            bookData.put("reads", 0);

                                            // Klucz mapy do znalezienia przypisanych danych
                                            Map<String, Object> preparedBook = new HashMap<>();
                                            preparedBook.put(ids[position], bookData);

                                            // Wskazanie mapy w której przypisana książka i jej dane mają się zapisać
                                            Map<String, Object> assignBook = new HashMap<>();
                                            assignBook.put("book", preparedBook);

                                            // Przypisanie danych
                                            db.collection("users").document(userId)
                                                    .set(assignBook, SetOptions.merge());

                                            Toast.makeText(context, context.getString(R.string.book_assigned), Toast.LENGTH_SHORT).show();

                                            openYourBooks();
                                        }
                                    } else {
                                        Toast.makeText(context, context.getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, context.getString(R.string.data_load_err), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

                pageChangeDialog.show();
            }
        });
}

    @Override
    public int getItemCount() {
        return titles.length;
    }

public class viewHolder extends RecyclerView.ViewHolder {

    TextView title, pages, author, rate, id;
    ConstraintLayout findBookLayout;
    RatingBar ratingBar;

    public viewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        pages = itemView.findViewById(R.id.pages);
        author = itemView.findViewById(R.id.author);
        rate = itemView.findViewById(R.id.rate);
        id = itemView.findViewById(R.id.ids);
        id.setVisibility(TextView.INVISIBLE);
        findBookLayout = itemView.findViewById(R.id.findBooksLayout);
        ratingBar = itemView.findViewById(R.id.bar);
        ratingBar.setIsIndicator(true);
        ratingBar.setFocusable(false);
    }
}

    public void openYourBooks() {

        Intent intent = new Intent(context.getApplicationContext(), YourBooks.class);
        context.startActivity(intent);
    }
}
