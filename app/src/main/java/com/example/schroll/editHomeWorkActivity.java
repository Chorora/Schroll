package com.example.schroll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class editHomeWorkActivity extends AppCompatActivity {

    EditText deadLine, hwDescription;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference documentReference;
    String pathName, pathNum, classRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_home_work);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        deadLine = findViewById(R.id.deadLine);
        hwDescription = findViewById(R.id.hwDescription);

        Intent intent = getIntent();
        classRoom = intent.getStringExtra(chooseClassroomActivity.EXTRA_CLASSROOM);
        pathName = intent.getStringExtra(chooseClassroomActivity.EXTRA_COURSE);

        if(pathName.matches("Math")) {pathNum = "1";}
        if(pathName.matches("Science")) {pathNum = "2";}
        if(pathName.matches("Physics")) {pathNum = "3";}
        if(pathName.matches("Art")) {pathNum = "4";}
        if(pathName.matches("Islamic")) {pathNum = "5";}
        if(pathName.matches("Arabic")) {pathNum = "6";}
        if(pathName.matches("English")) {pathNum = "7";}
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
                        Toast.makeText(editHomeWorkActivity.this, "HomeWork Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}