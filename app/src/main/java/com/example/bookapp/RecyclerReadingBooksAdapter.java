package com.example.bookapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerReadingBooksAdapter extends RecyclerView.Adapter<RecyclerReadingBooksAdapter.viewHolder> {

    String titles[];
    String pages[];
    String actualPage[];
    String author[];
    String ids[];
    Context context;

    public RecyclerReadingBooksAdapter(Context ctx, String data1[], String data2[], String data3[], String data4[], String[] data5) {
        context = ctx;
        titles = data1;
        pages = data2;
        ids = data3;
        actualPage = data4;
        author = data5;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_recycler_user_books_row, parent, false);
        return new RecyclerReadingBooksAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerReadingBooksAdapter.viewHolder holder, int position) {
        holder.title.setText(titles[position]);
        holder.pages.setText(context.getString(R.string.pages) + " " + pages[position]);
        holder.actualPage.setText(context.getString(R.string.user_page) + " " + actualPage[position]);
        holder.author.setText(author[position]);
        holder.id.setText(ids[position]);

        holder.yourBooksLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserBook.class);
                intent.putExtra("titles", titles[position]);
                intent.putExtra("ids", ids[position]);
                intent.putExtra("pages", pages[position]);
                intent.putExtra("actualPage", actualPage[position]);
                intent.putExtra("author", author[position]);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return titles.length;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView title, pages, id, actualPage, author;
        ConstraintLayout yourBooksLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            pages = itemView.findViewById(R.id.pages);
            actualPage = itemView.findViewById(R.id.actual_page);
            author = itemView.findViewById(R.id.author);
            id = itemView.findViewById(R.id.ids);
            id.setVisibility(TextView.INVISIBLE);
            yourBooksLayout = itemView.findViewById(R.id.yourBooksLayout);
        }
    }
}
