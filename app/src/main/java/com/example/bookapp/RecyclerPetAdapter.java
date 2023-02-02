package com.example.bookapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Utility.ConnectionUtility;
import com.google.firebase.firestore.FirebaseFirestore;

public class RecyclerPetAdapter extends RecyclerView.Adapter<RecyclerPetAdapter.viewHolder> {

    String pets[] = {"lion", "dog", "cat", "piggy", "shark", "cow", "sloth", "fox", "turtle", "crocodile"};

    Context context;

    int petsImages[];

    int petChosenMessages[] = {R.string.lion_chosen, R.string.dog_chosen, R.string.cat_chosen,
            R.string.piggy_chosen, R.string.shark_chosen, R.string.cow_chosen, R.string.sloth_chosen, R.string.fox_chosen,
            R.string.turtle_chosen, R.string.crocodile_chosen};

    int petDescriptions[] = {R.string.lion_description, R.string.dog_description, R.string.cat_description,
                    R.string.piggy_description, R.string.shark_description, R.string.cow_description, R.string.sloth_description, R.string.fox_description,
                    R.string.turtle_description, R.string.crocodile_description};

    int levelReq;

    ConnectionUtility connectionUtility = new ConnectionUtility();

    public RecyclerPetAdapter(Context context, int data[]) {
        this.context = context;
        this.petsImages = data;
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

        levelReq = position + 1;
        holder.petImage.setImageResource(petsImages[position]);
        holder.levelReq.setText(context.getString(R.string.level_req) + ": " + levelReq);
        holder.petDescription.setText(context.getString(petDescriptions[position]));

        holder.petLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connectionUtility.isConnected(context)) {
                    connectionUtility.showNoInternetConnectionAlert(context);
                } else {
                    chosePet(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return petsImages.length;
    }

    private void updatePet(String pet) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SharedPreferences preferences = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
        String userId = preferences.getString("id", "");
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("pet", pet);
        editor.apply();

        db.collection("users").document(userId)
                .update(
                        "pet", pet
                );
    }

    private void chosePet(int position) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SharedPreferences preferences = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
        String userId = preferences.getString("id", "");
        int userLevel = preferences.getInt("level", 1);
        SharedPreferences.Editor editor = preferences.edit();

        levelReq = position + 1;
        if (levelReq > userLevel) {
            Toast.makeText(context, context.getString(R.string.level_too_low) + " " + context.getString(R.string.your_level) + " " + userLevel + " " + context.getString(R.string.level_req) + ": " + levelReq, Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder petChangeDialog = new AlertDialog.Builder(context, R.style.CustomAlertDialogTheme);
        petChangeDialog.setTitle(context.getString(R.string.choose_pet));

        petChangeDialog.setPositiveButton(context.getString(R.string.choose), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String pet = pets[position];

                updatePet(pet);
                Toast.makeText(context, context.getString(petChosenMessages[position]), Toast.LENGTH_SHORT).show();
                openMain();

            }
        });
        petChangeDialog.show();
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

    public void openMain() {
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        context.startActivity(intent);
    }

}
