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

public class RecyclerNotAssignedBooksAdapter extends RecyclerView.Adapter<RecyclerNotAssignedBooksAdapter.viewHolder> {


    String titles[];
    String authors[];
    String pages[];
    String eans[];
    String ids[];
    Context context;

    public RecyclerNotAssignedBooksAdapter (Context ctx, String data1[], String data2[], String data3[], String data4[], String data5[]) {
        context = ctx;
        titles = data1;
        authors = data2;
        pages = data3;
        eans = data4;
        ids = data5;
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
        holder.title.setText(context.getString(R.string.title) + " " + titles[position]);
        holder.author.setText(context.getString(R.string.author) + " " + authors[position]);
        holder.pages.setText(context.getString(R.string.pages) + " " + pages[position]);
        holder.ean.setText("EAN: " + eans[position]);
        holder.id.setText(ids[position]);

        holder.findBookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AssignBook.class);
                intent.putExtra("titles", titles[position]);
                intent.putExtra("author", authors[position]);
                intent.putExtra("ids", ids[position]);
                intent.putExtra("pages", pages[position]);
                intent.putExtra("ean", eans[position]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView title, pages, author, ean, id;
        ConstraintLayout findBookLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            pages = itemView.findViewById(R.id.pages);
            author = itemView.findViewById(R.id.author);
            ean = itemView.findViewById(R.id.eans);
            id = itemView.findViewById(R.id.ids);
            id.setVisibility(TextView.INVISIBLE);
            findBookLayout = itemView.findViewById(R.id.findBookLayout);
        }
    }
}
