package com.example.lend;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback {

    DashAdapter adapter;
    public ArrayList<Item> items = new ArrayList();
    private GoogleMap mMap;
    LendUser user;
    Button viewFeaturedButton;
    Button viewLocationButton;
    FloatingActionButton mFab;
    Toolbar toolbar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = (Toolbar) findViewById(R.id.my_dash_toolbar);
        setSupportActionBar(toolbar);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                Log.d("offset value", Integer.toString(i));
                ImageView logo = findViewById(R.id.logo);
                if (i == -507) {
                    logo.setVisibility(View.VISIBLE);
                } else {
                    logo.setVisibility(View.INVISIBLE);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setButtons();
        filterCategory("All");
        initMapsFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public ArrayList<Item> setUpRV() {
        final RecyclerView recList = (RecyclerView) findViewById(R.id.recyclerview_dash);
        adapter = new DashAdapter(getApplicationContext(), items);
        Log.d("inside RV" , items.toString());
        recList.setAdapter(adapter);
        Log.d("XYZ", ((Integer) items.size()).toString());
        return items;
    }

    public void filterCategory(String slatt)    {
        if (slatt.equals("All"))   {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("items")
                    .whereEqualTo("Booked", "false")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("henlo", document.getId() + " => " + document.getData());
                                    Map<String, Object> itemMap = document.getData();
                                    Item temp = new Item();
                                    temp.setCategory(itemMap.get("Item Category").toString());
                                    temp.setItemDescription(itemMap.get("Item Description").toString());
                                    temp.setItemName(itemMap.get("Item Name").toString());
                                    temp.setPhotoURL(itemMap.get("Photo URL").toString());
                                    temp.setLender(itemMap.get("Lender ID").toString());
                                    temp.setPrice(itemMap.get("Item Price").toString());
                                    temp.setid(itemMap.get("ID").toString());
                                    items.add(temp);
                                }
                                Log.d("henlo" , items.toString());
                                setUpRV();
                            } else {
                                Log.d("henlo", "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }
        else    {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("items")
                    .whereEqualTo("Booked", "false")
                    .whereEqualTo("Item Category", slatt)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("henlo", document.getId() + " => " + document.getData());
                                    Map<String, Object> itemMap = document.getData();
                                    Item temp = new Item();
                                    temp.setCategory(itemMap.get("Item Category").toString());
                                    temp.setItemDescription(itemMap.get("Item Description").toString());
                                    temp.setItemName(itemMap.get("Item Name").toString());
                                    temp.setPhotoURL(itemMap.get("Photo URL").toString());
                                    temp.setLender(itemMap.get("Lender ID").toString());
                                    temp.setPrice(itemMap.get("Item Price").toString());
                                    temp.setid(itemMap.get("ID").toString());
                                    items.add(temp);
                                }
                                Log.d("henlo" , items.toString());
                                setUpRV();
                            } else {
                                Log.d("henlo", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void initMapsFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        db.collection("users")
                .whereEqualTo("id", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = FirebaseAuth.getInstance().getUid();
                                user = document.toObject(LendUser.class);
                                double userLat = Double.parseDouble(user.getLat());
                                double userLng = Double.parseDouble(user.getLng());
                                LatLng userLocation = new LatLng(userLat, userLng);
                                mMap.addMarker(new MarkerOptions().position(userLocation).title("User Location"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 11));
                            }
                        }
                    }
                });
        // Add all other user markers
        setOtherLocations();
    }


    public void setOtherLocations() {
        db.collection("cities")
                .whereEqualTo("capital", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user = document.toObject(LendUser.class);
                                if (user.getid() != FirebaseAuth.getInstance().getUid()) {
                                    double userLat = Double.parseDouble(user.getLat());
                                    double userLng = Double.parseDouble(user.getLng());
                                    LatLng userLocation = new LatLng(userLat, userLng);
                                    mMap.addMarker(new MarkerOptions().position(userLocation).title(user.getUsername()));
                                }
                            }
                        }
                    }
                });
    }



    public void setButtons() {
        viewFeaturedButton = findViewById(R.id.viewMoreFeaturedButton);
        viewLocationButton = findViewById(R.id.viewMoreLocButton);
        mFab = findViewById(R.id.fab);
        viewFeaturedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ListingsActivity.class);
                startActivity(intent);
            }
        });
        viewLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ListingsActivity.class);
                startActivity(intent);
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.my_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent3 = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.my_bookings_button:
                Intent intent = new Intent(DashboardActivity.this, BookingsListActivity.class);
                startActivity(intent);
                return true;
            case R.id.my_settings:
                //user sent to settings
                Intent intent2 = new Intent(DashboardActivity.this, EditProfileActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent2.putExtra("user", Parcels.wrap(user));
                startActivity(intent2);
                return true;
            case R.id.my_postings:
                Intent intent4 = new Intent(DashboardActivity.this, DashboardActivity.class);
                startActivity(intent4);

            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
