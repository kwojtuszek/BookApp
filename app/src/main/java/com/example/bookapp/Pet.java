package com.example.bookapp;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.databinding.ActivityPetBinding;

public class Pet extends Drawer_base {

    ActivityPetBinding activityPetBinding;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPetBinding = activityPetBinding.inflate(getLayoutInflater());
        setContentView(activityPetBinding.getRoot());
        allocateActivityTitle(getString(R.string.pet));


        int [] pets = {R.drawable.lion, R.drawable.doggo, R.drawable.kitty,
                R.drawable.piggy, R.drawable.shark, R.drawable.cow, R.drawable.sloth,
                R.drawable.fox, R.drawable.turtle, R.drawable.crocodile};

        recyclerView = findViewById(R.id.recyclerView);

        RecyclerPetAdapter adapter = new RecyclerPetAdapter(Pet.this, pets);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Pet.this));
    }
}
