package com.example.schroll;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class addStudentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    EditText studentName, studentSurname, studentAge, studentPhone, studentAddress, studentEmail, studentPassword;
    Spinner spinner;
    DocumentReference studentProfileRef, userAccessRef, gradesRef, courseRef;
    Button registerStudentButton;
    String classSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        studentName = findViewById(R.id.studentName);
        studentSurname = findViewById(R.id.studentSurname);
        studentAge = findViewById(R.id.studentAge);
        studentPhone = findViewById(R.id.studentPhone);
        studentAddress = findViewById(R.id.studentAddress);
        studentEmail = findViewById(R.id.studentEmail);
        studentPassword = findViewById(R.id.studentPassword);
        registerStudentButton = findViewById(R.id.registerStudentButton);
        spinner = findViewById(R.id.spinnerClass);

        ArrayAdapter<CharSequence> spinnerClassAdapter = ArrayAdapter.createFromResource(this, R.array.Classes, android.R.layout.simple_spinner_item);
        spinnerClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerClassAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.classSelected = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "No Class is selected", Toast.LENGTH_SHORT).show();
    }

    public void registerStudent(View view){
        String studentname, studentsurname, studentage, studentemail, studentpassword, studentphone, studentaddress;
        int Studentage;

        studentname = studentName.getText().toString();
        studentsurname = studentSurname.getText().toString().trim();
        studentage = studentAge.getText().toString();
       if(studentage.equals("")){ Studentage = 0;} else{Studentage = Integer.parseInt(studentage);}
        studentphone = studentPhone.getText().toString().trim();
        studentaddress = studentAddress.getText().toString();
        studentemail = studentEmail.getText().toString().trim();
        studentpassword = studentPassword.getText().toString().trim();

        if ( !studentemail.equals("") && !studentpassword.equals("") ) {
            fAuth.createUserWithEmailAndPassword(studentemail, studentpassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    FirebaseUser user = fAuth.getCurrentUser();
                    String userID = user.getUid();

                    studentProfileRef = fStore.collection("Students").document(userID);
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("Name", studentname);
                    userInfo.put("Surname", studentsurname);
                    userInfo.put("Age", Studentage);
                    userInfo.put("Class", classSelected);
                    userInfo.put("Classroom", classSelected.substring(0, 1));
                    userInfo.put("Year", classSelected.substring(4, 5));
                    userInfo.put("Address", studentaddress);
                    userInfo.put("Phone", studentphone);
                    studentProfileRef.set(userInfo);

                    userAccessRef = fStore.collection("Users").document(userID);
                    Map<String, String> userAccess = new HashMap<>();
                    userAccess.put("Type", "Student");
                    userAccessRef.set(userAccess);

                    gradesRef = fStore.collection("Grades").document(userID);

                    Map<String, String> userGrades = new HashMap<>();
                    for (int i = 1; i < 8; i++) {
                        String I = String.valueOf(i);
                        userGrades.put("Grade 0" + I, "**");

                        courseRef = fStore.collection("Year" + classSelected.substring(4, 5) + " Courses").document("Matiere 0" + I);
                        courseRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                                if (documentSnapshot.exists()) {
                                    userGrades.put("Grade " + documentSnapshot.getString("Code name"), "**");
                                    gradesRef.set(userGrades);
                                } else {
                                    Toast.makeText(addStudentActivity.this, "Could not find relevant course", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    Toast.makeText(addStudentActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                    finish();

                    // The last issue im facing/trying to solve is that after when i create an account for a student, if i exit the app and come back it will sign-in directly to that student's account and not into admin account
                }
            });
        }
        else if (studentemail.equals("") && studentpassword.equals("")) {

            Toast.makeText(addStudentActivity.this, "You must write the email and/or the password", Toast.LENGTH_SHORT).show();
        }
    }
}