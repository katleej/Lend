package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import java.util.Arrays;

import static com.example.lend.Utils.userWrite;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    public EditText etName;
    public EditText etEmail;
    public EditText etPassword;
    public Button btnSave;
    public Place userPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
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
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                userPlace = place;
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
//                            user.setLat(userPlace.getLatLng().latitude);
//                            user.setLat(userPlace.getLatLng().longitude);
                            userWrite(etName.getText().toString(), userPlace);
                        } else {
                            // If sign in fails, display a message to the user.
                            Exception e = task.getException();
                            Toast.makeText(SignupActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser user) {
        Intent mainIntent = new Intent(SignupActivity.this, ListingsActivity.class);
        startActivity(mainIntent);
    }
}
