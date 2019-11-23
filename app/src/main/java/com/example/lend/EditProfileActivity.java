package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.parceler.Parcels;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

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
    private Uri imageUri;
    public String uploadedImageURL;
    public Button image;

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
        image = findViewById(R.id.add_image);
        Glide.with(getApplicationContext()).load(lendUser.getPhotoURL()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImage);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                    startActivityForResult(intent, 1046);
                }
            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1046) {
            try {
                imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                new CloudStorage().upload(imageUri, new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
//                        image.setImageBitmap(selectedImage);
                        Log.d("HHHHHHHHHHH", s);
                        uploadedImageURL = s;
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        lendUser.setUsername(lendUser.getUsername());
                        lendUser.setLat(lendUser.getLat());
                        lendUser.setLng(lendUser.getLng());
                        lendUser.setmBorrowedItemList(lendUser.getmBorrowedItemList());
                        lendUser.setmLendedItemList(lendUser.getmLendedItemList());
                        lendUser.setYearJoined(lendUser.getYearJoined());
                        lendUser.setDescription(lendUser.getDescription());
                        lendUser.setid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        lendUser.setRating(lendUser.getRating());
                        lendUser.setNumReviews(lendUser.getNumReviews());
                        lendUser.setCity(lendUser.getCity());
                        lendUser.setPhotoURL(uploadedImageURL);
                        db.collection("users").document(lendUser.getUsername()).set(lendUser);
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "file not found", Toast.LENGTH_SHORT).show();
            }

        }

        else {
            Toast.makeText(this, "Incorrect requestcode", Toast.LENGTH_SHORT).show();
        }
        Glide.with(getApplicationContext()).load(lendUser.getPhotoURL()).diskCacheStrategy(DiskCacheStrategy.ALL).into(profileImage);
    }


}
