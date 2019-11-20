package com.example.lend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import org.parceler.Parcels;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    public TextView profileName;
    public LendUser lendUser;
    public FirebaseUser fbUser;
    public CircleImageView profileImage;
    public TextView yearJoined;
    public TextView profileDescription;
    public TextView profileLocation;
    public TextView numReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        lendUser = (LendUser) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        ratingBar = findViewById(R.id.ratingBar);
        profileName = findViewById(R.id.profile_name);
        profileImage = findViewById(R.id.profile_image);
        yearJoined = findViewById(R.id.profile_year_joined);
        profileDescription = findViewById(R.id.profile_description);
        profileLocation = findViewById(R.id.profile_location);
        numReviews = findViewById(R.id.review_number);

        ratingBar.setRating(lendUser.getRating());
        profileName.setText(lendUser.getUsername());
        yearJoined.setText(lendUser.getYearJoined());
        profileDescription.setText(lendUser.getDescription());
        numReviews = findViewById(lendUser.getNum_reviews());
    }
}
