package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.lend.Utils.db;

public class ListingsActivity extends AppCompatActivity {
    public ArrayList<Item> items;
    ArrayList<String> names;
    ItemAdapter adapter;
    FloatingActionButton fabAdd;
    Toolbar toolbar;
    SearchView searchview;
    FloatingActionButton categoryFilter;
    Spinner spinner;
    Button search;
    LendUser user;
    EditText searchbar;
    RecyclerView mList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reference = db.collection("items");
    private TextWatcher text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieve information of current user here
        db.collection("users")
                .whereEqualTo("id", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            Log.d("ABC", "user" + task.getResult().size());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                user = document.toObject(LendUser.class);
                            }
                        }
                    }
                });

        setContentView(R.layout.activity_listings);
        items = new ArrayList();
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
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
                Log.d("henlo2" , items.toString());

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        Log.d("henlo2" , items.toString());
        searchbar = (EditText) findViewById(R.id.searchbar);
        if (searchbar == null) {
            Log.d("henlo3" , "null searchbar");
        }
        setUpRV();
        Log.d("henlo2" , "outside textwatcher");
        CollectionReference q = db.collection("items");
        searchbar.addTextChangedListener(new TextWatcher() {

            @Override

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (searchbar == null) {
                    Log.d("henlo3" , "null searchbar");
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 0) {
                    Query q = db.collection("items").whereEqualTo("Booked", "false");
                    showAdapter(q , 0);
                } // This is used as if user erases the characters in the search field.
                else {
                     // name - the field for which you want to make search
                    showAdapter(reference.orderBy("Item Name").startAt(charSequence.toString().trim()).endAt(charSequence.toString().trim() + "\uf8ff") , 1);
                }
//                adapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable editable) {
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
                //user sent to settings
                Intent intent2 = new Intent(ListingsActivity.this, EditProfileActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent2.putExtra("user", Parcels.wrap(user));
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void showAdapter(Query q1 , int i) {
        if (i == 0)   {
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
        else {
            q1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        items.clear();
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

    public ArrayList<Item> setUpRV() {
        final RecyclerView recList = (RecyclerView) findViewById(R.id.recyclerView);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(ListingsActivity.this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        adapter = new ItemAdapter(getApplicationContext(), items);
        Log.d("inside RV" , items.toString());
        recList.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
        Log.d("XYZ", ((Integer) items.size()).toString());
        return items;
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


    public void filterName(String slatt)    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items")
                .whereEqualTo("Booked", "false")
                .whereEqualTo("Item Name", slatt)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            items.clear();
                            names = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("henlo", document.getId() + " => " + document.getData());
                                Map<String, Object> itemMap = document.getData();
                                String tempName = itemMap.get("Item Name").toString();
                                names.add(tempName);
                            }
                            Log.d("henlo" , items.toString());
                            setUpRV();
                        } else {
                            Log.d("henlo", "Error getting documents: ", task.getException());
                        }

                    }
                });

    }
    @Override
    public void onBackPressed() {
        adapter.notifyDataSetChanged();
    }


    public void onClickFilter(View view) {
        final Dialog rankDialog = new Dialog(getApplicationContext(), R.style.Theme_AppCompat_Light_Dialog);
        rankDialog.setContentView(R.layout.filter_dialog);
        rankDialog.setCancelable(true);
        Button updateButton = (Button) rankDialog.findViewById(R.id.filter);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankDialog.dismiss();
            }
        });
    }
}



//        text = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.d("henlo2" , "inside textwatcher");
//                String user_input = searchbar.getText().toString();
//                for (int i = 0; i < items.size(); i++) {
//                    if (!items.get(i).getItemName().contains(s)) {
//                        items.remove(i);
//                        Log.d("henlo2" , items.get(i).getItemName());
//                    }
//                    setUpRV();
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        };
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (searchbar.getText().toString().isEmpty())   {
//                    Toast.makeText(getApplication(), "Please enter a keyword!", Toast.LENGTH_SHORT).show();
//
//                }
//                else    {
//                    filterName(searchbar.getText().toString());
//                }
//            }
//        });

//    public void filter(String text){
//        ArrayList<String> temp = new ArrayList<String>();
//        for(String d: names){
//            //or use .equal(text) with you want equal match
//            //use .toLowerCase() for better matches
//            if(d.toLowerCase().contains(text.toLowerCase())){
//                temp.add(d);
//            }
//        }
//        //update recyclerview
//        adapter.updateList(temp);
//    }