package com.example.schroll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class addGradesActivity extends AppCompatActivity {
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    CollectionReference students = fStore.collection("Students"), coursesRef = fStore.collection("Year1 Courses");
    DocumentReference grades;
    private Spinner spinner;
    EditText grade;
    String surName, studentID, course, courseNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grades);
        spinner = findViewById(R.id.spinnerStudent);
        grade = findViewById(R.id.gradeToPut);

        Intent intent = getIntent();
        course = intent.getStringExtra(MainActivity2.EXTRA_SPECIALTY2);

        displayStudentsOnSpinner();
        getNumberOfCourse();
    }


    public void displayStudentsOnSpinner() {

        students.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Students> studentsList = new ArrayList<>();
                    Students student1;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        surName = document.getString("Surname");
                        studentID = document.getId();

                        student1 = new Students(surName, studentID);
                        studentsList.add(student1);
                    }
                    ArrayAdapter<Students> adapter = new ArrayAdapter<Students>(getApplicationContext(), android.R.layout.simple_spinner_item, studentsList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                } else {
                    Toast.makeText(addGradesActivity.this, "Error getting StudentID", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void getNumberOfCourse() {

        coursesRef.whereEqualTo("Code name", course).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        courseNumber = document.getId();
                        courseNumber = courseNumber.substring(8, 10);
                    }
                } else {
                    Toast.makeText(addGradesActivity.this, "Error getting course number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setGrade(View v) {
        Students studentX = (Students) spinner.getSelectedItem();

        grades = fStore.collection("Grades").document(studentX.getStudentID());
        grades.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    String Grade = grade.getText().toString();
                    Map<String, Object> Put = new HashMap<>();
                    String S = "Grade " + courseNumber;
                    Put.put(S, Grade);

                    grades.set(Put, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(addGradesActivity.this, "Grade has been added successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addGradesActivity.this, "Failed to update the grade", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(addGradesActivity.this, "Grade reference not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}