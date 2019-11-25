package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.parceler.Parcels;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    public ImageView pencil;
    public ImageView editImage;
    public HashMap states;
    public String city;
    public String state;
    public Place newPlace;
    public CoordinatorLayout coordinatorLayout;


    private Uri imageUri;
    public String uploadedImageURL;
    public Button image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        lendUser = (LendUser) Parcels.unwrap(getIntent().getParcelableExtra("user"));


        Toast.makeText(getApplication(), "Tap to make changes!", Toast.LENGTH_LONG).show();


        setContentView(R.layout.activity_edit_profile);

        final int grey = getResources().getColor(R.color.light_grey);
        final int white = getResources().getColor(R.color.white);

        //Bring all the UI items
        ratingBar = findViewById(R.id.ratingBar);
        profileName = findViewById(R.id.edit_profile_name);
        profileImage = findViewById(R.id.edit_profile_image);
        editImage = findViewById(R.id.edit_profile_image);
        Glide.with(getApplicationContext()).load(lendUser.getPhotoURL()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImage);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                    startActivityForResult(intent, 1046);
                }
            }
        });

        pencil = findViewById(R.id.edit_location);
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
        ratingBar.setRating((float) lendUser.getRating());
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



        profileLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ABC", "i have been clicked");
                Intent intent = new Intent(EditProfileActivity.this, ChangeLocationActivity.class);
                intent.putExtra("user", Parcels.wrap(lendUser));
                startActivityForResult(intent, 1200);
            }
        });

        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ABC", "i have been clicked");
                Intent intent = new Intent(EditProfileActivity.this, ChangeLocationActivity.class);
                intent.putExtra("user", Parcels.wrap(lendUser));
                startActivityForResult(intent, 1200);
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
                        Log.d("HHHHHHHHHHH", s);
                        uploadedImageURL = s;
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Glide.with(getApplicationContext()).load(uploadedImageURL).diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(profileImage);
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
        if (requestCode == 1200 && resultCode == 200) {
            profileLocation.setText(data.getExtras().getString("city"));
        }

        else {
            Toast.makeText(this, "Incorrect requestcode", Toast.LENGTH_SHORT).show();
        }
        Glide.with(getApplicationContext()).load(lendUser.getPhotoURL()).diskCacheStrategy(DiskCacheStrategy.ALL).into(profileImage);
    }


}
