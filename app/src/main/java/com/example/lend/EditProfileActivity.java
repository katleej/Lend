package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.parceler.Parcels;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth auth;
    private RatingBar ratingBar;
    public TextView profileName;
    public LendUser lendUser;
    public String uid;
    public FirebaseUser fbUser;
    public CircleImageView profileImage;
    public TextView yearJoined;
    public TextView profileDescription;
    public TextView profileLocation;
    public TextView numReviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        lendUser = (LendUser) Parcels.unwrap(getIntent().getParcelableExtra("user"));

//        db.collection("users")
//                .whereEqualTo("username", username)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                lendUser = document.toObject(LendUser.class);
//
//                            }
//                        }
//                    }
//                });
        setContentView(R.layout.activity_edit_profile);

        //Bring all the UI items
        ratingBar = findViewById(R.id.ratingBar);
        profileName = findViewById(R.id.edit_profile_name);
        profileImage = findViewById(R.id.edit_profile_image);
        profileDescription = findViewById(R.id.edit_profile_description);
        yearJoined = findViewById(R.id.profile_year_joined);

        profileLocation = findViewById(R.id.profile_location);
        numReviews = findViewById(R.id.review_number);

        profileName.setText(lendUser.getUsername());
        yearJoined.setText(Integer.toString(lendUser.getYearJoined()));
        profileDescription.setText(lendUser.getDescription());
        numReviews.setText(Integer.toString(lendUser.getNumReviews()));
        ratingBar.setRating(lendUser.getRating());
    }
}
