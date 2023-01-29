package com.example.bookapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerPetAdapter extends RecyclerView.Adapter<RecyclerPetAdapter.viewHolder> {

    Context context;
    int pets[];

    public RecyclerPetAdapter(Context context, int data[]) {
        this.context = context;
        this.pets = data;
    }

    @NonNull
    @Override
    public RecyclerPetAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_recycler_pet_row, parent, false);
        return new RecyclerPetAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerPetAdapter.viewHolder holder, int position) {

        holder.petImage.setImageResource(pets[position]);

        int levelReq;
        switch (position) {

            case 0:
                levelReq = position + 1;
                holder.petDescription.setText(context.getString(R.string.lion_description));
                holder.levelReq.setText(context.getString(R.string.level_req) + ": " + levelReq);
                break;

            case 1:
                levelReq = position + 1;
                holder.petDescription.setText(context.getString(R.string.dog_description));
                holder.levelReq.setText(context.getString(R.string.level_req) + ": " + levelReq);
                break;

            case 2:
                levelReq = position + 1;
                holder.petDescription.setText(context.getString(R.string.cat_description));
                holder.levelReq.setText(context.getString(R.string.level_req) + ": " + levelReq);
                break;

            case 3:
                levelReq = position + 1;
                holder.petDescription.setText(context.getString(R.string.piggy_description));
                holder.levelReq.setText(context.getString(R.string.level_req) + ": " + levelReq);
                break;

            case 4:
                levelReq = position + 1;
                holder.petDescription.setText(context.getString(R.string.shark_description));
                holder.levelReq.setText(context.getString(R.string.level_req) + ": " + levelReq);
                break;

            case 5:
                levelReq = position + 1;
                holder.petDescription.setText(context.getString(R.string.cow_description));
                holder.levelReq.setText(context.getString(R.string.level_req) + ": " + levelReq);
                break;

            case 6:
                levelReq = position + 1;
                holder.petDescription.setText(context.getString(R.string.sloth_description));
                holder.levelReq.setText(context.getString(R.string.level_req) + ": " + levelReq);
                break;

            case 7:
                levelReq = position + 1;
                holder.petDescription.setText(context.getString(R.string.fox_description));
                holder.levelReq.setText(context.getString(R.string.level_req) + ": " + levelReq);
                break;

            case 8:
                levelReq = position + 1;
                holder.petDescription.setText(context.getString(R.string.turtle_description));
                holder.levelReq.setText(context.getString(R.string.level_req) + ": " + levelReq);
                break;

            case 9:
                levelReq = position + 1;
                holder.petDescription.setText(context.getString(R.string.crocodile_description));
                holder.levelReq.setText(context.getString(R.string.level_req) + ": " + levelReq);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return pets.length;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {


        ImageView petImage;
        TextView petDescription, levelReq;
        ConstraintLayout petLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            petImage = itemView.findViewById(R.id.pet_image);
            petDescription = itemView.findViewById(R.id.pet_description);
            levelReq = itemView.findViewById(R.id.level_req);
            petLayout = itemView.findViewById(R.id.petLayout);

        }
    }

}
