package com.example.lend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import org.parceler.Parcels;

public class ProfileActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    public TextView profileName;
    public LendUser lendUser;
    public FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        lendUser = (LendUser) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        ratingBar = findViewById(R.id.ratingBar);
        profileName = findViewById(R.id.profile_name);
        profileName.setText(lendUser.getUsername());
    }
}
