package com.example.bookapp;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.userRate.setText(context.getString(R.string.user_rate) + " " + userRate[position]);

        holder.readedBooksLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder pageChangeDialog = new AlertDialog.Builder(context);
                pageChangeDialog.setTitle(context.getString(R.string.rate));

                pageChangeDialog.setPositiveButton("Mleczko", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                    });

                pageChangeDialog.setNegativeButton("Czytaj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                    }
                });

                pageChangeDialog.show();

//                Intent intent = new Intent(context, ReadedBooks.class);
//                intent.putExtra("titles", titles[position]);
//                intent.putExtra("ids", ids[position]);
//                intent.putExtra("reads", reads[position]);
//                intent.putExtra("actualPage", actualPage[position]);
//                intent.putExtra("author", author[position]);
//                intent.putExtra("userRate", userRate[position]);
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView title, reads, id, actualPage, author, userRate;
        ConstraintLayout readedBooksLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            reads = itemView.findViewById(R.id.reads);
            actualPage = itemView.findViewById(R.id.actual_page);
            actualPage.setVisibility(TextView.INVISIBLE);
            author = itemView.findViewById(R.id.author);
            id = itemView.findViewById(R.id.ids);
            id.setVisibility(TextView.INVISIBLE);
            userRate = itemView.findViewById(R.id.user_rate);
            readedBooksLayout = itemView.findViewById(R.id.readedBooksLayout);
        }
    }

}

