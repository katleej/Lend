package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ListingsActivity extends AppCompatActivity {
    ArrayList<Item> items;
    ItemAdapter adapter;
    FloatingActionButton fabAdd;
    Toolbar toolbar;
    FloatingActionButton categoryFilter;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        items = new ArrayList();

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
/*
        db.collection("items")
<<<<<<< HEAD
//                .whereEqualTo("Booked", false)
=======
                .whereEqualTo("Booked", "false")
>>>>>>> 5e5d1bf0cdba6773942003e3da76939bf0afb215
//                .whereEqualTo("Item Category", "Electronic Appliances")
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
                                temp.setID(itemMap.get("ID").toString());
                                items.add(temp);
                            }
                            Log.d("henlo" , items.toString());
                            setUpRV();
                        } else {
                            Log.d("henlo", "Error getting documents: ", task.getException());
                        }

                    }
                });
*/
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListingsActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String selectedCategory = spinner.getSelectedItem().toString();
                items.clear();
                filterCategory(selectedCategory);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.my_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent3 = new Intent(ListingsActivity.this, MainActivity.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.my_bookings_button:
                Intent intent = new Intent(ListingsActivity.this, BookingsListActivity.class);
                startActivity(intent);
                return true;
            case R.id.my_settings:
                Intent intent2 = new Intent(ListingsActivity.this, ViewProfileActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setUpRV() {
        final RecyclerView recList = (RecyclerView) findViewById(R.id.recyclerView);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(ListingsActivity.this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        adapter = new ItemAdapter(getApplicationContext(), items);
        recList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d("XYZ", ((Integer) items.size()).toString());
    }

    public void filterCategory(String slatt)    {
        if (slatt.equals("Show All"))   {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("items")
                    .whereEqualTo("Booked", "false")
//                .whereEqualTo("Item Category", "Electronic Appliances")
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
    @Override
    public void onBackPressed() {
        adapter.notifyDataSetChanged();
    }
}
