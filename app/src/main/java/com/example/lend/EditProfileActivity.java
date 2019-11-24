package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
    public HashMap states;
    public String city;
    public String state;
    public Place newPlace;

    private Uri imageUri;
    public String uploadedImageURL;
    public Button image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        lendUser = (LendUser) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        Places.initialize(getApplicationContext(), "AIzaSyB7PN4NZcXwmlTvJ1K_NV6g4md9nGoKV30");
        PlacesClient placesClient = Places.createClient(this);

        states = new HashMap<String, String>();
        states.put("Alabama","AL");
        states.put("Alaska","AK");
        states.put("Alberta","AB");
        states.put("American Samoa","AS");
        states.put("Arizona","AZ");
        states.put("Arkansas","AR");
        states.put("Armed Forces (AE)","AE");
        states.put("Armed Forces Americas","AA");
        states.put("Armed Forces Pacific","AP");
        states.put("British Columbia","BC");
        states.put("California","CA");
        states.put("Colorado","CO");
        states.put("Connecticut","CT");
        states.put("Delaware","DE");
        states.put("District Of Columbia","DC");
        states.put("Florida","FL");
        states.put("Georgia","GA");
        states.put("Guam","GU");
        states.put("Hawaii","HI");
        states.put("Idaho","ID");
        states.put("Illinois","IL");
        states.put("Indiana","IN");
        states.put("Iowa","IA");
        states.put("Kansas","KS");
        states.put("Kentucky","KY");
        states.put("Louisiana","LA");
        states.put("Maine","ME");
        states.put("Manitoba","MB");
        states.put("Maryland","MD");
        states.put("Massachusetts","MA");
        states.put("Michigan","MI");
        states.put("Minnesota","MN");
        states.put("Mississippi","MS");
        states.put("Missouri","MO");
        states.put("Montana","MT");
        states.put("Nebraska","NE");
        states.put("Nevada","NV");
        states.put("New Brunswick","NB");
        states.put("New Hampshire","NH");
        states.put("New Jersey","NJ");
        states.put("New Mexico","NM");
        states.put("New York","NY");
        states.put("Newfoundland","NF");
        states.put("North Carolina","NC");
        states.put("North Dakota","ND");
        states.put("Northwest Territories","NT");
        states.put("Nova Scotia","NS");
        states.put("Nunavut","NU");
        states.put("Ohio","OH");
        states.put("Oklahoma","OK");
        states.put("Ontario","ON");
        states.put("Oregon","OR");
        states.put("Pennsylvania","PA");
        states.put("Prince Edward Island","PE");
        states.put("Puerto Rico","PR");
        states.put("Quebec","PQ");
        states.put("Rhode Island","RI");
        states.put("Saskatchewan","SK");
        states.put("South Carolina","SC");
        states.put("South Dakota","SD");
        states.put("Tennessee","TN");
        states.put("Texas","TX");
        states.put("Utah","UT");
        states.put("Vermont","VT");
        states.put("Virgin Islands","VI");
        states.put("Virginia","VA");
        states.put("Washington","WA");
        states.put("West Virginia","WV");
        states.put("Wisconsin","WI");
        states.put("Wyoming","WY");
        states.put("Yukon Territory","YT");

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
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditProfileActivity.this);
                LayoutInflater inflater = EditProfileActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setPositiveButton("update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("users").whereEqualTo("id", lendUser.getid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (newPlace == null) {
                                    Toast.makeText(EditProfileActivity.this, "Make sure to select a place first!", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot ds: task.getResult()) {
                                            Map<String, Object> userMap = new HashMap<>();
                                            userMap.put("lat", newPlace.getLatLng().latitude);
                                            userMap.put("latLocation", newPlace.getLatLng().latitude);
                                            userMap.put("long", newPlace.getLatLng().longitude);
                                            userMap.put("longLocation", newPlace.getLatLng().longitude);
                                            userMap.put("city", city + ", " + states.get(state));

                                            db.collection("users").document(lendUser.getUsername()).update(userMap);
                                        }
                                    }
                                }
                            }
                        });
                    }
                });

                dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                dialogBuilder.show();


                AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.acLocation);
                autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
                autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(@NonNull Place place) {
                        newPlace = place;
                        Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geo.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                            city = addresses.get(0).getLocality();
                            state = addresses.get(0).getAdminArea();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@NonNull Status status) {

                    }
                });

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
                        Glide.with(getApplicationContext()).load(uploadedImageURL).diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(profileImage);
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
