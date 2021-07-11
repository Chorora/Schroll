package com.example.schroll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class editHomeworkDetailsActivity extends AppCompatActivity {

    EditText deadLine, hwDescription;

    FirebaseFirestore fStore;
    DocumentReference documentReference;
    CollectionReference yearXcourses;
    String pathName, pathNum, classRoom, Year;
    boolean notFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_homework_details);
        fStore = FirebaseFirestore.getInstance();

        deadLine = findViewById(R.id.deadLine);
        hwDescription = findViewById(R.id.hwDescription);

        Intent intent = getIntent();
        classRoom = intent.getStringExtra(chooseClassroomActivity.EXTRA_CLASSROOM);
        Year = classRoom.substring(6,7);
        pathName = intent.getStringExtra(chooseClassroomActivity.EXTRA_COURSE);

    yearXcourses = fStore.collection("Year" +Year +" Courses");
    yearXcourses.whereEqualTo("Code name", pathName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    pathNum = document.getId();
                    pathNum = pathNum.substring(9, 10);
                    notFound = true;
                }
            } else {
                Toast.makeText(editHomeworkDetailsActivity.this, "Error getting document", Toast.LENGTH_SHORT).show();
            }
        }
    });
}


    public void updateHomeWork(View v){
        String deadline = deadLine.getText().toString();
        String description = hwDescription.getText().toString();
        documentReference = fStore.collection("Home Works").document("" +classRoom);

        Map<String, String> Loc = new HashMap<>();
        Loc.put("HW 0" +pathNum, description);
        Loc.put("HW 0" +pathNum +" END", deadline);

        documentReference.set(Loc, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(editHomeworkDetailsActivity.this, "HomeWork Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}