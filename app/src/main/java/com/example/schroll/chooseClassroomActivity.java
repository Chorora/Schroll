package com.example.schroll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class chooseClassroomActivity extends AppCompatActivity {
    public static final String EXTRA_CLASSROOM = "EXTRA_CLASSROOM";
    public static final String EXTRA_COURSE = "EXTRA_COURSE";

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    DocumentReference classXRef, classXRef2;
    TextView classRoom1, classRoom2, classRoom3, classRoom4;
    CardView cardView1,cardView2, cardView3, cardView4;
    String userID ,course, i_but_on_String_XD ,j_but_on_string;
    int limited = 5;
    String[]  classRoomX = new String[limited];
    String[]  classRoomXD = new String[limited];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_classroom);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        classRoom1 = findViewById(R.id.classRoom1);
        classRoom2 = findViewById(R.id.classRoom2);
        classRoom3 = findViewById(R.id.classRoom3);
        classRoom4 = findViewById(R.id.classRoom4);
        cardView1 = findViewById(R.id.cardView12);
        cardView2 = findViewById(R.id.cardView13);
        cardView3 = findViewById(R.id.cardView14);
        cardView4 = findViewById(R.id.cardView15);
        Intent intent = getIntent();
        course = intent.getStringExtra(MainTeachersActivity.EXTRA_SPECIALTY2);

        classXRef2 = fStore.collection("Users").document(userID);

        classXRef2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                for (int j = 1; j < limited; j++) {
                    int finalJ = j;
                    j_but_on_string = Integer.toString(finalJ);
                    classRoomXD[finalJ] = documentSnapshot.getString("Class 0" + j_but_on_string);

                    if(classRoomXD[finalJ].equals("")){
                        classRoomXD[finalJ] = null;
                    }
                    displayClassesAvailable(classRoomXD[finalJ]);
                }
            }
        });

    }

    private void displayClassesAvailable(String classRoomXD){
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
                                classRoomX[finalI] = document.getString("Code");

                                if (classRoomX[finalI].equals(classRoomXD)) {

                                    cardView1.setVisibility(View.VISIBLE);
                                    classRoom1.setText(document.getString("Name"));
                                    classRoom1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            passToEdit(classRoomX[finalI]);
                                        }
                                    });
                                }
                            } else if (finalI == 2) {
                                classRoomX[finalI] = document.getString("Code");

                                //Check if the classroom in db storage matches one of the classrooms that this teacher is responsible for
                                if (classRoomX[finalI].equals(classRoomXD)) {
                                    cardView2.setVisibility(View.VISIBLE);
                                    classRoom2.setText(document.getString("Name"));
                                    classRoom2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            passToEdit(classRoomX[finalI]);
                                        }
                                    });
                                }
                            } else if (finalI == 3) {
                                classRoomX[finalI] = document.getString("Code");

                                //Check if the classroom in db storage matches one of the classrooms that this teacher is responsible for
                                if (classRoomX[finalI].equals(classRoomXD)) {
                                    cardView3.setVisibility(View.VISIBLE);
                                    classRoom3.setText(document.getString("Name"));
                                    classRoom3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            passToEdit(classRoomX[finalI]);
                                        }
                                    });
                                }
                            } else if (finalI == 4) {
                                classRoomX[finalI] = document.getString("Code");

                                //Check if the classroom in db storage matches one of the classrooms that this teacher is responsible for
                                if (classRoomX[finalI].equals(classRoomXD)) {
                                    cardView4.setVisibility(View.VISIBLE);
                                    classRoom4.setText(document.getString("Name"));
                                    classRoom4.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            passToEdit(classRoomX[finalI]);
                                        }
                                    });
                                }
                            }
                        }
                    } else {
                        Toast.makeText(chooseClassroomActivity.this, "Loading classrooms failed !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void passToEdit(String classRoomX) {
        Intent intent = new Intent(this, editHomeworkDetailsActivity.class);
        intent.putExtra(EXTRA_CLASSROOM, classRoomX);
        intent.putExtra(EXTRA_COURSE, course);
        startActivity(intent);
    }
}