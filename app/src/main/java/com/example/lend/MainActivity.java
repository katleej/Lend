package com.example.lend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.lend.Utils.userWrite;

public class MainActivity extends Activity {
    private FirebaseAuth mAuth;
    private FusedLocationProviderClient fusedLocationClient;
    private final String TAG = "hello";
    private String m_Text = "";
    private ViewFlipper viewFlipper;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button myButton;
    Button signUp;
    EditText etEmail;
    EditText etPassword;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setDisplayedChild(0);
        viewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            int count = 1;
            public void onAnimationStart(Animation animation) {
                if (count == 2) {
                    viewFlipper.stopFlipping();
                }
            }
            public void onAnimationRepeat(Animation animation) { }
            public void onAnimationEnd(Animation animation) {
                count++;
            }
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (mAuth.getCurrentUser() != null) {
            viewFlipper.setDisplayedChild(0);
            viewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
                int count = 1;
                public void onAnimationStart(Animation animation) {
                    if (count == 2) {
                        viewFlipper.stopFlipping();
                    }
                }
                public void onAnimationRepeat(Animation animation) { }
                public void onAnimationEnd(Animation animation) {
                    count++;
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }
            });
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        context = this;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        myButton = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.cab);
        etEmail = findViewById(R.id.emailField);
        etPassword = findViewById(R.id.passwordField);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    signIn(etEmail.getText().toString(), etPassword.getText().toString());

            }
        });

        TextView signup = findViewById(R.id.cab);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });



        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user){}


    public void signIn(String email, String password){
        //validate email and password
        if ((email.isEmpty() || (password.isEmpty())))  {
            Toast.makeText(MainActivity.this, "Please enter a username and password!",
                    Toast.LENGTH_SHORT).show();
        }
        else   {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Intent i = new Intent(MainActivity.this , DashboardActivity.class);
                            startActivityForResult(i , 1);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                }); }
    }

    public void getCurrentUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }


}
