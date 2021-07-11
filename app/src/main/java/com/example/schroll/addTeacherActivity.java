package com.example.schroll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.HashMap;
import java.util.Map;

public class addTeacherActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    DocumentReference classesRef, teacherProfileRef;
    EditText teacherName, teacherSurname, teacherPhone, teacherAddress, teacherEmail, teacherPassword;
    CheckBox cbClass01, cbClass02, cbClass03, cbClass04;
    Button registerTeacherButton;
    Spinner spinner;
    String specialtySelected, mail = "admin@mail.com", password = "admin123";
    int j;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        teacherName = findViewById(R.id.teacherName);
        teacherSurname = findViewById(R.id.teacherSurname);
        teacherPhone = findViewById(R.id.teacherPhone);
        teacherAddress = findViewById(R.id.teacherAddress);
        teacherEmail = findViewById(R.id.teacherEmail);
        teacherPassword = findViewById(R.id.teacherPassword);
        registerTeacherButton = findViewById(R.id.registerTeacherButton);
        cbClass01 = findViewById(R.id.cbClass01);
        cbClass02 = findViewById(R.id.cbClass02);
        cbClass03 = findViewById(R.id.cbClass03);
        cbClass04 = findViewById(R.id.cbClass04);
        spinner = findViewById(R.id.spinnerClass2);

        ArrayAdapter<CharSequence> spinnerClassAdapter = ArrayAdapter.createFromResource(this, R.array.Specialties, android.R.layout.simple_spinner_item);
        spinnerClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerClassAdapter);
        spinner.setOnItemSelectedListener(this);

        for (j = 1; j < 5; j++) {
            String J = String.valueOf(j);
            classesRef = fStore.collection("Classrooms").document("Class 0" + J);
            classesRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, FirebaseFirestoreException error) {

                    switch (J) {
                        case "1":
                            cbClass01.setText("Class " + documentSnapshot.getString("Name"));
                            break;
                        case "2":
                            cbClass02.setText("Class " + documentSnapshot.getString("Name"));
                            break;
                        case "3":
                            cbClass03.setText("Class " + documentSnapshot.getString("Name"));
                            break;
                        case "4":
                            cbClass04.setText("Class " + documentSnapshot.getString("Name"));
                            break;
                    }
                }
            });
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.specialtySelected = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "No Class has been selected", Toast.LENGTH_SHORT).show();
    }

    public void registerTeacher(View v) {
        String teachername, teachersurname, teacheremail, teacherpassword, teacherphone, teacheraddress;

        teachername = teacherName.getText().toString();
        teachersurname = teacherSurname.getText().toString().trim();
        teacherphone = teacherPhone.getText().toString().trim();
        teacheraddress = teacherAddress.getText().toString();
        teacheremail = teacherEmail.getText().toString().trim();
        teacherpassword = teacherPassword.getText().toString().trim();

        if (!teacheremail.equals("") && !teacherpassword.equals("")) {
            if ((cbClass02.isChecked() || cbClass02.isChecked() || cbClass03.isChecked() || cbClass04.isChecked())) {
                fAuth.createUserWithEmailAndPassword(teacheremail, teacherpassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = fAuth.getCurrentUser();
                        String userID = user.getUid();
                        teacherProfileRef = fStore.collection("Users").document(userID);
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("Name", teachername);
                        userInfo.put("Surname", teachersurname);
                        userInfo.put("Address", teacheraddress);
                        userInfo.put("Phone", teacherphone);
                        userInfo.put("Specialty", specialtySelected);
                        String type = "Teacher";
                        userInfo.put("Type", type);

                        if (cbClass01.isChecked()) {
                            // to trim the class name and form it as class code when i store it in the database
                            String X = cbClass01.getText().toString().substring(6,7);
                            String Y = cbClass01.getText().toString().substring(10,11);
                            userInfo.put("Class 01", "Class " +X +"_" +Y);
                        }
                         if (!cbClass01.isChecked()) {
                            userInfo.put("Class 01", "");
                        }
                         if (cbClass02.isChecked()) {
                             String X = cbClass02.getText().toString().substring(6,7);
                             String Y = cbClass02.getText().toString().substring(10,11);
                            userInfo.put("Class 02", "Class " +X +"_" +Y);
                        }
                          if (!cbClass02.isChecked()) {
                            userInfo.put("Class 02", "");
                        }
                         if (cbClass03.isChecked()) {
                             String X = cbClass03.getText().toString().substring(6,7);
                             String Y = cbClass03.getText().toString().substring(10,11);
                            userInfo.put("Class 03", "Class " +X +"_" +Y);
                        }
                         if (!cbClass03.isChecked()) {
                            userInfo.put("Class 03", "");
                        }
                         if (cbClass04.isChecked()) {
                             String X = cbClass04.getText().toString().substring(6,7);
                             String Y = cbClass04.getText().toString().substring(10,11);
                            userInfo.put("Class 04", "Class " +X +"_" +Y);
                        }
                         if (!cbClass04.isChecked()) {
                            userInfo.put("Class 04", "");
                        }

                        teacherProfileRef.set(userInfo);

                        Toast.makeText(addTeacherActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                    }

                });

                reSignTheAdmin();

            } else if (!cbClass01.isChecked() && !cbClass02.isChecked() && !cbClass03.isChecked() && !cbClass04.isChecked()) {

                Toast.makeText(addTeacherActivity.this, "You must choose at least class", Toast.LENGTH_SHORT).show();
            }
        } else if (teacheremail.equals("") && teacherpassword.equals("")) {

            Toast.makeText(addTeacherActivity.this, "You must write the email and/or the password", Toast.LENGTH_SHORT).show();
        }
    }

    private void reSignTheAdmin() {
        fAuth.signInWithEmailAndPassword(mail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                startActivity(new Intent(getApplicationContext(), MainAdminActivity.class));
                finish();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addTeacherActivity.this, "Failed to reLogin the admin", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

