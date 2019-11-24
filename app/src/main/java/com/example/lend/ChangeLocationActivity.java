package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.CollectionReference;
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

public class ChangeLocationActivity extends AppCompatActivity {
    Place newPlace;
    String city;
    String state;
    LendUser user;
    FirebaseFirestore db;
    Map<String, String> states;

    Button btnCancel;
    Button btnUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);
        user = (LendUser) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        db = FirebaseFirestore.getInstance();

        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);

        Places.initialize(getApplicationContext(), "AIzaSyB7PN4NZcXwmlTvJ1K_NV6g4md9nGoKV30");
        PlacesClient placesClient = Places.createClient(this);

        states = new HashMap<String, String>();
        states.put("Alabama", "AL");
        states.put("Alaska", "AK");
        states.put("Alberta", "AB");
        states.put("American Samoa", "AS");
        states.put("Arizona", "AZ");
        states.put("Arkansas", "AR");
        states.put("Armed Forces (AE)", "AE");
        states.put("Armed Forces Americas", "AA");
        states.put("Armed Forces Pacific", "AP");
        states.put("British Columbia", "BC");
        states.put("California", "CA");
        states.put("Colorado", "CO");
        states.put("Connecticut", "CT");
        states.put("Delaware", "DE");
        states.put("District Of Columbia", "DC");
        states.put("Florida", "FL");
        states.put("Georgia", "GA");
        states.put("Guam", "GU");
        states.put("Hawaii", "HI");
        states.put("Idaho", "ID");
        states.put("Illinois", "IL");
        states.put("Indiana", "IN");
        states.put("Iowa", "IA");
        states.put("Kansas", "KS");
        states.put("Kentucky", "KY");
        states.put("Louisiana", "LA");
        states.put("Maine", "ME");
        states.put("Manitoba", "MB");
        states.put("Maryland", "MD");
        states.put("Massachusetts", "MA");
        states.put("Michigan", "MI");
        states.put("Minnesota", "MN");
        states.put("Mississippi", "MS");
        states.put("Missouri", "MO");
        states.put("Montana", "MT");
        states.put("Nebraska", "NE");
        states.put("Nevada", "NV");
        states.put("New Brunswick", "NB");
        states.put("New Hampshire", "NH");
        states.put("New Jersey", "NJ");
        states.put("New Mexico", "NM");
        states.put("New York", "NY");
        states.put("Newfoundland", "NF");
        states.put("North Carolina", "NC");
        states.put("North Dakota", "ND");
        states.put("Northwest Territories", "NT");
        states.put("Nova Scotia", "NS");
        states.put("Nunavut", "NU");
        states.put("Ohio", "OH");
        states.put("Oklahoma", "OK");
        states.put("Ontario", "ON");
        states.put("Oregon", "OR");
        states.put("Pennsylvania", "PA");
        states.put("Prince Edward Island", "PE");
        states.put("Puerto Rico", "PR");
        states.put("Quebec", "PQ");
        states.put("Rhode Island", "RI");
        states.put("Saskatchewan", "SK");
        states.put("South Carolina", "SC");
        states.put("South Dakota", "SD");
        states.put("Tennessee", "TN");
        states.put("Texas", "TX");
        states.put("Utah", "UT");
        states.put("Vermont", "VT");
        states.put("Virgin Islands", "VI");
        states.put("Virginia", "VA");
        states.put("Washington", "WA");
        states.put("West Virginia", "WV");
        states.put("Wisconsin", "WI");
        states.put("Wyoming", "WY");
        states.put("Yukon Territory", "YT");

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


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(100);
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("users").whereEqualTo("id", user.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot ds : task.getResult()) {
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("latitude", newPlace.getLatLng().latitude);
                                userMap.put("longitude", newPlace.getLatLng().longitude);
                                userMap.put("city", city + ", " + states.get(state));
                                db.collection("users").document(user.getUsername()).update(userMap);
                            }
                            Intent intent = new Intent();
                            intent.putExtra("city", city + ", " + states.get(state));
                            setResult(200, intent);
                            finish();
                        }
                    }
                });

            }

        });

    }
}




