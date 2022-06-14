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

public class RecyclerUserBooksAdapter extends RecyclerView.Adapter<RecyclerUserBooksAdapter.viewHolder> {

    String titles[];
    String authors[];
    String pages[];
    String actualPage[];
    String eans[];
    String ids[];
    Context context;

    public RecyclerUserBooksAdapter(Context ctx, String data1[], String data2[], String data3[], String data4[], String data5[], String data6[]) {
        context = ctx;
        titles = data1;
        authors = data2;
        pages = data3;
        eans = data4;
        ids = data5;
        actualPage = data6;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_recycler_user_books_row, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.title.setText(context.getString(R.string.title) + " " + titles[position]);
        holder.author.setText(context.getString(R.string.author) + " " + authors[position]);
        holder.pages.setText(context.getString(R.string.pages) + " " + pages[position]);
        holder.actualPage.setText(context.getString(R.string.user_page) + " " + actualPage[position]);
        holder.ean.setText("EAN: " + eans[position]);
        holder.id.setText(ids[position]);

        holder.yourBooksLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserBook.class);
                intent.putExtra("titles", titles[position]);
                intent.putExtra("ids", ids[position]);
                intent.putExtra("pages", pages[position]);
                intent.putExtra("actualPage", actualPage[position]);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView title, pages, author, ean, id, actualPage;
        ConstraintLayout yourBooksLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            pages = itemView.findViewById(R.id.pages);
            actualPage = itemView.findViewById(R.id.actual_page);
            author = itemView.findViewById(R.id.author);
            ean = itemView.findViewById(R.id.eans);
            id = itemView.findViewById(R.id.ids);
            id.setVisibility(TextView.INVISIBLE);
            yourBooksLayout = itemView.findViewById(R.id.yourBooksLayout);
        }
    }
}
