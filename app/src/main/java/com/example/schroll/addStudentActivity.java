package com.example.schroll;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addStudentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    EditText studentName, studentSurname, studentAge, studentPhone, studentAddress, studentEmail, studentPassword;
    Spinner spinner;
    CollectionReference studentRef;
    DocumentReference studentProfileRef, userAccessRef;
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
        Toast.makeText(this, "NothingSelected", Toast.LENGTH_SHORT).show();
    }

    public void registerStudent(View view){
        String studentname, studentsurname, studentage, studentemail, studentpassword, studentphone, studentaddress;
        int Studentage;
        studentRef = fStore.collection("Students");
        studentname = studentName.getText().toString();
        studentsurname = studentSurname.getText().toString().trim();
        studentage = studentAge.getText().toString();
       if(studentage.equals("")){ Studentage = 0;} else{Studentage = Integer.parseInt(studentage);}
        studentphone = studentPhone.getText().toString().trim();
        studentaddress = studentAddress.getText().toString();
        studentemail = studentEmail.getText().toString().trim();
        studentpassword = studentPassword.getText().toString().trim();

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
                userInfo.put("Classroom", classSelected.substring(0,1));
                userInfo.put("Year", classSelected.substring(4,5));
                userInfo.put("Address", studentaddress);
                userInfo.put("Phone", studentphone);
                studentProfileRef.set(userInfo);

                userAccessRef = fStore.collection("Users").document(userID);
                Map<String, String> userAccess = new HashMap<>();
                userAccess.put("Type", "Student");
                userAccessRef.set(userAccess);
                Toast.makeText(addStudentActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                finish();
                FirebaseAuth.getInstance().signOut();
            }
        });
    }

}