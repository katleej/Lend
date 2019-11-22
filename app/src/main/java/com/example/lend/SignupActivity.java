package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.lend.Utils.userWrite;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    public EditText etName;
    public EditText etEmail;
    public EditText etPassword;
    public Button btnSave;
    public Place userPlace;
    public Fragment placeAutoComplete;
    Map<String, String> states;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Places.initialize(getApplicationContext(), "AIzaSyB7PN4NZcXwmlTvJ1K_NV6g4md9nGoKV30");
        PlacesClient placesClient = Places.createClient(this);
        mAuth = FirebaseAuth.getInstance();

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });

        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.acLocation);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                userPlace = place;
                String cityName = "";
                String countryName = "";
                Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geo.getFromLocation(userPlace.getLatLng().latitude, userPlace.getLatLng().longitude, 1);
                    cityName = addresses.get(0).getLocality();
                    countryName = states.get(addresses.get(0).getAdminArea());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("henlo" , cityName + ", " + countryName);

            }
            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }


    public void createAccount(String email, String password){
        //validate whether or not they are emails and passwords

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Success! Welcome to Lend", Toast.LENGTH_SHORT).show();
                            LendUser user = new LendUser(getApplicationContext());
                            user.setUsername(etName.getText().toString());
                            user.setLat(((Double) userPlace.getLatLng().latitude).toString());
                            user.setLng(((Double) userPlace.getLatLng().longitude).toString());
                            int year = Calendar.getInstance().get(Calendar.YEAR);
                            user.setYearJoined(year);
                            user.setDescription("No description ");
                            user.setid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            user.setRating(0);
                            user.setNumReviews(0);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users").document(user.getUsername()).set(user);
                            Intent i = new Intent(SignupActivity.this , ListingsActivity.class);
                            startActivityForResult(i , 1);
                        } else {
                            // If sign in fails, display a message to the user.
                            Exception e = task.getException();
                            Toast.makeText(SignupActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
