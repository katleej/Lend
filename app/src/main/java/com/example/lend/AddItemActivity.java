package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileNotFoundException;
import java.io.InputStream;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static com.example.lend.Utils.itemWrite;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText view_price, view_name, view_description;
    private Spinner view_category;
    private int price;
    private Item item;
    private String name, description, category;
    private Button image;
    private Button save;
    private Button cancel;
    public static final int PICK_IMAGE = 1;
    private final String TAG = "Hello";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private LendUser currUser;
    private Uri imageUri;
    public String uploadedImageURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        user = FirebaseAuth.getInstance().getCurrentUser();
        view_price = (EditText) findViewById(R.id.price);
        view_name = (EditText) findViewById(R.id.name);
        view_description = (EditText) findViewById(R.id.description);
        view_category = (Spinner) findViewById(R.id.category);
        Places.initialize(getApplicationContext(), "AIzaSyB7PN4NZcXwmlTvJ1K_NV6g4md9nGoKV30");
        PlacesClient placesClient = Places.createClient(this);
        image = findViewById(R.id.add_image);
        save = findViewById(R.id.add_item_post);
        cancel = findViewById(R.id.add_item_cancel);
        image.setOnClickListener(this);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

        db.collection("users")
                .whereEqualTo("id", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            Log.d("ABC", "user" + task.getResult().size());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                currUser = document.toObject(LendUser.class);
                            }
                        }
                    }
                });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.writing_menu, menu);
//        return true;
//    }

    @Override
    public void onClick(View view) {
            switch(view.getId()) {
                case R.id.add_image:
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                        startActivityForResult(intent, 1046);
                    }
                    break;
                case R.id.add_item_cancel:
                    finish();
                    break;
                case R.id.add_item_post:
                    Item item = new Item();
                    String name = view_name.getText().toString();
                    String description = view_description.getText().toString();
                    String category = view_category.getSelectedItem().toString();
                    String photoURL = uploadedImageURL;
                    if (view_price.getText().toString() == null || name == null || description == null || category == null || photoURL == null) {
                        Toast.makeText(getApplicationContext(), "Make sure to fill out all the fields!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    int price = Integer.parseInt(view_price.getText().toString().trim());
                    //int start = 3 + photoURL.indexOf("%");
                    //int end = photoURL.indexOf("?");
                    //photoURL = uploadedImageURL.substring(start, end);
                    try {
                        item.setItemName(name);
                        item.setLenderName(currUser.getUsername());
                        item.setItemDescription(description);
                        item.setCategory(category);
                        item.setPrice(((Integer) price).toString());
                        item.setPhotoURL(photoURL);
                        itemWrite(user.getUid(), item.getLenderName(), item.getItemName() , item.getItemDescription(), item.getPrice(), item.getCategory(), item.getPhotoURL(), "false");
                    }
                    catch (NullPointerException e)  {
                        Log.d("henlo" , "some nullpointerexception");
                    }
                    Intent j = new Intent(AddItemActivity.this, ListingsActivity.class);
                    startActivityForResult(j , 1);
                    break;
            }
        }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_delivery:
                if (checked)
                    break;
            case R.id.radio_pickup:
                if (checked)
                    break;
            case R.id.radio_both:
                if (checked)
                    break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1046) {
            try {
                final TextView textView = findViewById(R.id.imageResource);
                imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                new CloudStorage().upload(imageUri, new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
//                        image.setImageBitmap(selectedImage);
                        Log.d("HHHHHHHHHHH", s);
                        uploadedImageURL = s;
                        textView.setText(uploadedImageURL);
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
