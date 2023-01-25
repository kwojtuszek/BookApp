package com.example.bookapp;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RecyclerReadedBooksAdapter extends RecyclerView.Adapter<RecyclerReadedBooksAdapter.viewHolder> {

    String titles[];
    String reads[];
    String actualPage[];
    String author[];
    String ids[];
    String userRate[];
    Context context;

    public RecyclerReadedBooksAdapter(Context ctx, String data1[], String data2[], String data3[], String data4[], String[] data5, String[] data6) {
        context = ctx;
        titles = data1;
        reads = data2;
        ids = data3;
        actualPage = data4;
        author = data5;
        userRate = data6;
    }

    @NonNull
    @Override
    public RecyclerReadedBooksAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_recycler_readed_books_row, parent, false);
        return new RecyclerReadedBooksAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerReadedBooksAdapter.viewHolder holder, int position) {
        holder.title.setText(titles[position]);
        holder.reads.setText(context.getString(R.string.readed_row) + " " + reads[position] + " " + context.getString(R.string.amount));
        holder.actualPage.setText(actualPage[position]);
        holder.author.setText(author[position]);
        holder.id.setText(ids[position]);
        holder.ratingBar.setRating(Float.parseFloat(userRate[position]));

        holder.readedBooksLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder pageChangeDialog = new AlertDialog.Builder(context);
                pageChangeDialog.setTitle(context.getString(R.string.readed_book_alert));

                pageChangeDialog.setPositiveButton(context.getString(R.string.rate_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences passBookId = context.getSharedPreferences("book_id", context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = passBookId.edit();
                        editor.putString("bookId", ids[position]);
                        editor.apply();
                        openRateBook();
                    }
                    });

                pageChangeDialog.setNegativeButton(context.getString(R.string.read_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences preferences = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
                        String userId = preferences.getString("id", "");

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference user = db.collection("users").document(userId);

                        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {

                                        int ifAlreadyReading = Integer.parseInt(document.get("book." + ids[position] + ".page").toString());

                                        if (ifAlreadyReading != 0) {

                                            Toast.makeText(context, context.getString(R.string.already_reading), Toast.LENGTH_SHORT).show();
                                        } else {

                                            db.collection("users").document(userId)
                                                    .update(
                                                            "book." + ids[position] + ".page", 1
                                                    );

                                            Toast.makeText(context, context.getString(R.string.reading_again), Toast.LENGTH_SHORT).show();
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

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView title, reads, id, actualPage, author;
        ConstraintLayout readedBooksLayout;
        RatingBar ratingBar;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            reads = itemView.findViewById(R.id.reads);
            actualPage = itemView.findViewById(R.id.actual_page);
            actualPage.setVisibility(TextView.INVISIBLE);
            author = itemView.findViewById(R.id.author);
            id = itemView.findViewById(R.id.ids);
            id.setVisibility(TextView.INVISIBLE);
            readedBooksLayout = itemView.findViewById(R.id.readedBooksLayout);
            ratingBar = itemView.findViewById(R.id.bar);
            ratingBar.setIsIndicator(true);
            ratingBar.setFocusable(false);
        }
    }

    public void openRateBook() {

        Intent intent = new Intent(context.getApplicationContext(), RateBook.class);
        context.startActivity(intent);
    }

}

