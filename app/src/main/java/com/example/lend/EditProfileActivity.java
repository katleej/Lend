package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.parceler.Parcels;


import java.util.HashMap;
import java.util.Map;

import java.io.FileNotFoundException;
import java.io.InputStream;


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
    public Button saveButton;
    public EditText changeName;
    public EditText changeDescription;

    private Uri imageUri;
    public String uploadedImageURL;
    public Button image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        lendUser = (LendUser) Parcels.unwrap(getIntent().getParcelableExtra("user"));

        setContentView(R.layout.activity_edit_profile);

        final int grey = getResources().getColor(R.color.light_grey);
        final int white = getResources().getColor(R.color.white);

        //Bring all the UI items
        ratingBar = findViewById(R.id.ratingBar);
        profileName = findViewById(R.id.edit_profile_name);
        profileImage = findViewById(R.id.edit_profile_image);
//        image = findViewById(R.id.add_image);
        Glide.with(getApplicationContext()).load(lendUser.getPhotoURL()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
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
        changeName = findViewById(R.id.edit_profile_name);
        changeDescription = findViewById(R.id.edit_profile_description);
        changeName.setFocusable(true);
        changeDescription.setFocusable(true);

        profileLocation = findViewById(R.id.profile_location);
        numReviews = findViewById(R.id.review_number);

        profileName.setText(lendUser.getUsername());
        yearJoined.setText(Integer.toString(lendUser.getYearJoined()));
        profileDescription.setText(lendUser.getDescription());
        numReviews.setText(Integer.toString(lendUser.getNumReviews()));
        ratingBar.setRating(lendUser.getRating());
        profileLocation.setText(lendUser.getCity());

        saveButton = findViewById(R.id.saveButton);
        String description = "description";
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lendUser.setDescription(changeDescription.getText().toString());
                lendUser.setUsername(changeName.getText().toString());
                Map<String,Object> userMap = new HashMap<>();
                userMap.put("description",changeDescription.getText().toString());
                userMap.put("username", changeName.getText().toString());
                db.collection("users").document(lendUser.getUsername()).update(userMap);
                changeName.setBackgroundColor(white);
                changeDescription.setBackgroundColor(white);
                changeName.setFocusable(false);
                changeDescription.setFocusable(false);
                Toast.makeText(getApplication(), "Your changes have been saved.", Toast.LENGTH_SHORT).show();
            }
        });

        changeName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    v.setBackgroundColor(grey);
                } else {// Instead of your Toast
                    v.setBackgroundColor(white);
                }
            }
        });

        changeDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    v.setBackgroundColor(grey);
                } else {// Instead of your Toast
                    v.setBackgroundColor(white);
                }
            }
        });






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
                        Glide.with(getApplicationContext()).load(uploadedImageURL).diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(profileImage);
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
