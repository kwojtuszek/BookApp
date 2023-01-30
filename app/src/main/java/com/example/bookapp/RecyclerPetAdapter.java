package com.example.bookapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

public class RecyclerPetAdapter extends RecyclerView.Adapter<RecyclerPetAdapter.viewHolder> {

    Context context;
    int pets[], levelReq;

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

        holder.petLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                SharedPreferences preferences = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
                String userId = preferences.getString("id", "");
                int userLevel = preferences.getInt("level", 1);
                SharedPreferences.Editor editor = preferences.edit();

                AlertDialog.Builder petChangeDialog = new AlertDialog.Builder(context, R.style.CustomAlertDialogTheme);
                petChangeDialog.setTitle(context.getString(R.string.choose_pet));

                switch (position) {

                    case 0:
                        petChangeDialog.setPositiveButton(context.getString(R.string.choose), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                editor.putString("pet", "lion");
                                editor.apply();

                                db.collection("users").document(userId)
                                        .update(
                                                "pet", "lion"
                                        );

                                Toast.makeText(context, context.getString(R.string.lion_chosen), Toast.LENGTH_SHORT).show();
                                openMain();
                            }
                        });
                        petChangeDialog.show();
                        break;

                    case 1:
                        levelReq = position + 1;
                        if (levelReq <= userLevel) {

                            petChangeDialog.setPositiveButton(context.getString(R.string.choose), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    editor.putString("pet", "dog");
                                    editor.apply();

                                    db.collection("users").document(userId)
                                            .update(
                                                    "pet", "dog"
                                            );

                                    Toast.makeText(context, context.getString(R.string.dog_chosen), Toast.LENGTH_SHORT).show();
                                    openMain();
                                }
                            });
                            petChangeDialog.show();

                        } else
                            Toast.makeText(context, context.getString(R.string.level_too_low) + " " + context.getString(R.string.your_level) + " " + userLevel + " " + context.getString(R.string.level_req) + ": " + levelReq, Toast.LENGTH_SHORT).show();

                        break;

                    case 2:
                        levelReq = position + 1;
                        if (levelReq <= userLevel) {

                            petChangeDialog.setPositiveButton(context.getString(R.string.choose), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    editor.putString("pet", "cat");
                                    editor.apply();

                                    db.collection("users").document(userId)
                                            .update(
                                                    "pet", "cat"
                                            );

                                    Toast.makeText(context, context.getString(R.string.cat_chosen), Toast.LENGTH_SHORT).show();
                                    openMain();
                                }
                            });
                            petChangeDialog.show();

                        } else
                            Toast.makeText(context, context.getString(R.string.level_too_low) + " " + context.getString(R.string.your_level) + " " + userLevel + " " + context.getString(R.string.level_req) + ": " + levelReq, Toast.LENGTH_SHORT).show();
                        break;

                    case 3:
                        levelReq = position + 1;
                        if (levelReq <= userLevel) {

                            petChangeDialog.setPositiveButton(context.getString(R.string.choose), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    editor.putString("pet", "piggy");
                                    editor.apply();

                                    db.collection("users").document(userId)
                                            .update(
                                                    "pet", "piggy"
                                            );

                                    Toast.makeText(context, context.getString(R.string.piggy_chosen), Toast.LENGTH_SHORT).show();
                                    openMain();
                                }
                            });
                            petChangeDialog.show();

                        } else
                            Toast.makeText(context, context.getString(R.string.level_too_low) + " " + context.getString(R.string.your_level) + " " + userLevel + " " + context.getString(R.string.level_req) + ": " + levelReq, Toast.LENGTH_SHORT).show();
                        break;

                    case 4:
                        levelReq = position + 1;
                        if (levelReq <= userLevel) {

                            petChangeDialog.setPositiveButton(context.getString(R.string.choose), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    editor.putString("pet", "shark");
                                    editor.apply();

                                    db.collection("users").document(userId)
                                            .update(
                                                    "pet", "shark"
                                            );

                                    Toast.makeText(context, context.getString(R.string.shark_chosen), Toast.LENGTH_SHORT).show();
                                    openMain();
                                }
                            });
                            petChangeDialog.show();

                        } else
                            Toast.makeText(context, context.getString(R.string.level_too_low) + " " + context.getString(R.string.your_level) + " " + userLevel + " " + context.getString(R.string.level_req) + ": " + levelReq, Toast.LENGTH_SHORT).show();
                        break;

                    case 5:
                        levelReq = position + 1;
                        if (levelReq <= userLevel) {

                            petChangeDialog.setPositiveButton(context.getString(R.string.choose), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    editor.putString("pet", "cow");
                                    editor.apply();

                                    db.collection("users").document(userId)
                                            .update(
                                                    "pet", "cow"
                                            );

                                    Toast.makeText(context, context.getString(R.string.cow_chosen), Toast.LENGTH_SHORT).show();
                                    openMain();
                                }
                            });
                            petChangeDialog.show();

                        } else
                            Toast.makeText(context, context.getString(R.string.level_too_low) + " " + context.getString(R.string.your_level) + " " + userLevel + " " + context.getString(R.string.level_req) + ": " + levelReq, Toast.LENGTH_SHORT).show();
                        break;

                    case 6:
                        levelReq = position + 1;
                        if (levelReq <= userLevel) {

                            petChangeDialog.setPositiveButton(context.getString(R.string.choose), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    editor.putString("pet", "sloth");
                                    editor.apply();

                                    db.collection("users").document(userId)
                                            .update(
                                                    "pet", "sloth"
                                            );

                                    Toast.makeText(context, context.getString(R.string.sloth_chosen), Toast.LENGTH_SHORT).show();
                                    openMain();
                                }
                            });
                            petChangeDialog.show();

                        } else
                            Toast.makeText(context, context.getString(R.string.level_too_low) + " " + context.getString(R.string.your_level) + " " + userLevel + " " + context.getString(R.string.level_req) + ": " + levelReq, Toast.LENGTH_SHORT).show();
                        break;

                    case 7:
                        levelReq = position + 1;
                        if (levelReq <= userLevel) {

                            petChangeDialog.setPositiveButton(context.getString(R.string.choose), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    editor.putString("pet", "fox");
                                    editor.apply();

                                    db.collection("users").document(userId)
                                            .update(
                                                    "pet", "fox"
                                            );

                                    Toast.makeText(context, context.getString(R.string.fox_chosen), Toast.LENGTH_SHORT).show();
                                    openMain();
                                }
                            });
                            petChangeDialog.show();

                        } else
                            Toast.makeText(context, context.getString(R.string.level_too_low) + " " + context.getString(R.string.your_level) + " " + userLevel + " " + context.getString(R.string.level_req) + ": " + levelReq, Toast.LENGTH_SHORT).show();
                        break;

                    case 8:
                        levelReq = position + 1;
                        if (levelReq <= userLevel) {

                            petChangeDialog.setPositiveButton(context.getString(R.string.choose), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    editor.putString("pet", "turtle");
                                    editor.apply();

                                    db.collection("users").document(userId)
                                            .update(
                                                    "pet", "turtle"
                                            );

                                    Toast.makeText(context, context.getString(R.string.turtle_chosen), Toast.LENGTH_SHORT).show();
                                    openMain();
                                }
                            });
                            petChangeDialog.show();

                        } else
                            Toast.makeText(context, context.getString(R.string.level_too_low) + " " + context.getString(R.string.your_level) + " " + userLevel + " " + context.getString(R.string.level_req) + ": " + levelReq, Toast.LENGTH_SHORT).show();
                        break;

                    case 9:
                        levelReq = position + 1;
                        if (levelReq <= userLevel) {

                            petChangeDialog.setPositiveButton(context.getString(R.string.choose), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    editor.putString("pet", "crocodile");
                                    editor.apply();

                                    db.collection("users").document(userId)
                                            .update(
                                                    "pet", "crocodile"
                                            );

                                    Toast.makeText(context, context.getString(R.string.crocodile_chosen), Toast.LENGTH_SHORT).show();
                                    openMain();
                                }
                            });
                            petChangeDialog.show();

                        } else
                            Toast.makeText(context, context.getString(R.string.level_too_low) + " " + context.getString(R.string.your_level) + " " + userLevel + " " + context.getString(R.string.level_req) + ": " + levelReq, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
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

    public void openMain() {
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        context.startActivity(intent);
    }

}
