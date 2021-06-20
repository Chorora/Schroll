package com.example.schroll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class chooseClassroomActivity2 extends AppCompatActivity {
    public static final String EXTRA_CLASSROOM = "EXTRA_CLASSROOM";
    public static final String EXTRA_COURSE = "EXTRA_COURSE";
    TextView classRoom1, classRoom2, classRoom3, classRoom4;
    FirebaseFirestore fStore;
    DocumentReference classXRef;
    String course, i_but_on_String_XD;
    int limited = 5;
    String[] classRoomX = new String[limited];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_classroom2);
        fStore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        course = intent.getStringExtra(MainActivity2.EXTRA_SPECIALTY2);

        classRoom1 = findViewById(R.id.ClassRoom1);
        classRoom2 = findViewById(R.id.ClassRoom2);
        classRoom3 = findViewById(R.id.ClassRoom3);
        classRoom4 = findViewById(R.id.ClassRoom4);

        for (int i = 1; i < limited; i++) {
            i_but_on_String_XD = Integer.toString(i);
            classXRef = fStore.collection("Classrooms").document("Class 0" + i_but_on_String_XD);
            int finalI = i;
            classXRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            if (finalI == 1) {
                                classRoom1.setText(document.getString("Name"));
                                classRoomX[finalI] = document.getString("Code");
                                classRoom1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        passToEdit(classRoomX[finalI]);
                                    }
                                });
                            }

                            if (finalI == 2) {
                                classRoom2.setText(document.getString("Name"));
                                classRoomX[finalI] = document.getString("Code");
                                classRoom2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        passToEdit(classRoomX[finalI]);
                                    }
                                });
                            }

                            if (finalI == 3) {
                                classRoom3.setText(document.getString("Name"));
                                classRoomX[finalI] = document.getString("Code");
                                classRoom3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        passToEdit(classRoomX[finalI]);
                                    }
                                });
                            }

                            if (finalI == 4) {
                                classRoom4.setText(document.getString("Name"));
                                classRoomX[finalI] = document.getString("Code");
                                classRoom4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        passToEdit(classRoomX[finalI]);
                                    }
                                });
                            }
                        }
                    } else {
                        Toast.makeText(chooseClassroomActivity2.this, "Loading classrooms failed !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void passToEdit(String classRoomX) {
        Intent intent = new Intent(this, uploadCourseActivity.class);
        intent.putExtra(EXTRA_CLASSROOM, classRoomX);
        intent.putExtra(EXTRA_COURSE, course);
        startActivity(intent);
    }

}