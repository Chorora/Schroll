package com.example.schroll;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class loginActivity extends AppCompatActivity {

    public static final String EXTRA_SPECIALTY = "EXTRA_SPECIALTY";
    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    Button loginButton;
    EditText email, password;
    FirebaseAuth firebaseAuth, fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    TextView forgetText;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;
    String specialty;
    String userID, Type;
    StorageReference storageReference;
    DocumentReference typeOfUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String UiD = user.getUid();

            typeOfUserRef = fStore.collection("Users").document(UiD);
            typeOfUserRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {

                        Type = documentSnapshot.getString("Type");

                        switch (Type) {
                            case "Student":
                                startActivity(new Intent(getApplicationContext(), MainStudentsActivity.class));
                                finish();
                                break;

                            case "Teacher":
                                specialty = documentSnapshot.getString("Specialty");
                                Intent intent = new Intent(getApplicationContext(), MainTeachersActivity.class);
                                intent.putExtra(EXTRA_SPECIALTY, specialty);
                                startActivity(intent);
                                finish();
                                break;

                            case "Admin":
                                startActivity(new Intent(getApplicationContext(), MainAdminActivity.class));
                                finish();
                                break;
                        }
                        Toast.makeText(loginActivity.this, "You successfully logged in", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(loginActivity.this, "User's document hasn't been found", Toast.LENGTH_SHORT).show();
                        signMeOut();
                    }
                }

            });
        } else {
            login();
        }
    }

    public void signMeOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), loginActivity.class));
        finish();
    }

    protected void login() {
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginbutton);
        forgetText = findViewById(R.id.forgetText);
        email.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        forgetText.setVisibility(View.VISIBLE);
        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validate credentials
                if (email.getText().toString().isEmpty()) {
                    email.setError("Email is missing");
                    return;
                }

                if (password.getText().toString().isEmpty()) {
                    password.setError("Password is missing");
                }

                //to login the user
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        dependUser();

                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(loginActivity.this, "Email or password is invalid", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    public void dependUser() {
        userID = fAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        DocumentReference documentReference = fStore.collection("Users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (documentSnapshot.exists()) {

                    if (documentSnapshot.contains("Type")) {

                        Type = documentSnapshot.getString("Type");

                        if (!Type.equals("")) {
                            switch (Type) {

                                case "Student":
                                    startActivity(new Intent(getApplicationContext(), MainStudentsActivity.class));
                                    finish();
                                    break;

                                case "Teacher":
                                    specialty = documentSnapshot.getString("Specialty");
                                    Intent intent = new Intent(getApplicationContext(), MainTeachersActivity.class);
                                    intent.putExtra(EXTRA_TYPE, Type);
                                    intent.putExtra(EXTRA_SPECIALTY, specialty);
                                    startActivity(intent);
                                    finish();
                                    break;

                                case "Admin":
                                    startActivity(new Intent(getApplicationContext(), MainAdminActivity.class));
                                    finish();
                                    break;
                            }
                            Toast.makeText(loginActivity.this, "You successfully logged in", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(loginActivity.this, "Type field is empty", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(loginActivity.this, "Type field isn't found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(loginActivity.this, "User's document hasn't been found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void forgetText(View view) {
        view = inflater.inflate(R.layout.reset_pop, null);
        View finalView = view;
        reset_alert.setTitle("Reset Forgot Password ?")
                .setMessage("Enter your Email to get password reset link")
                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //validate email
                        EditText email = finalView.findViewById(R.id.emailReset);
                        if (email.getText().toString().isEmpty()) {
                            email.setError("Required Field");
                            return;
                        }
                        //send the reset link
                        firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(loginActivity.this, "Reset Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(loginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }).setNegativeButton("Cancel", null)
                .setView(view)
                .create().show();
    }

}