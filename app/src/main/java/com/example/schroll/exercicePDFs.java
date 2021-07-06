package com.example.schroll;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class exercicePDFs extends AppCompatActivity {
    private static final String KEY_GRADE = "Grade";
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseStorage fStorage = FirebaseStorage.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    StorageReference fStorageRef;
    private CollectionReference students = fStore.collection("Students");
    DocumentReference userRef, classXRef;
    PDFView pdfView;
    Handler mHandler, handler;
    EditText gradeHW;
    TextView textView;

    String studentID, title, course, j_but_on_string, userID = fAuth.getCurrentUser().getUid();
    int pdf_index;
    int j, limited = 5;
    String[] classRoomXD = new String[limited];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice_p_d_fs);
        pdfView = findViewById(R.id.pdfView);
        pdf_index = getIntent().getIntExtra("pos", 0);
        mHandler = new Handler();
        handler = new Handler();
        title = pdf_list.documentArrayList.get(pdf_index).getData();
        gradeHW = findViewById(R.id.hwGrade);
        textView = findViewById(R.id.textView17);
        Intent intent = getIntent();
        course = intent.getStringExtra(pdf_list.EXTRA_COURSE);

        findStudentID(title);

        classXRef = fStore.collection("Users").document(userID);
        classXRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                for (int j = 1; j < limited; j++) {
                    j_but_on_string = Integer.toString(j);
                    classRoomXD[j] = documentSnapshot.getString("Class 0" + j_but_on_string);

                    if (classRoomXD[j].equals("")) {
                        classRoomXD[j] = null;
                    }
                    displayPDF(classRoomXD[j]);
                }
                if (!documentSnapshot.exists()) {
                    Toast.makeText(exercicePDFs.this, "Wrong reference for classes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void displayPDF(String classRoomXD) {

        fStorageRef = fStorage.getReference().child("Pdf Uploads/" + classRoomXD + "/" + course + "/");
        final long ONE_MEGABYTE = 1024 * 1024;
        fStorageRef.child(pdf_list.documentArrayList.get(pdf_index).getData()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                pdfView.fromBytes(bytes).load();
            }
        });
    }

    public void findStudentID(String title) {
        title = title.substring(0, title.length() - 4);

        students.whereEqualTo("Surname", title).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        studentID = document.getId();
                    }
                } else {
                    Toast.makeText(exercicePDFs.this, "Error getting StudentID", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setGradeHW(View v) {
        String grade = gradeHW.getText().toString();
        Map<String, Object> Loc = new HashMap<>();
        String S = KEY_GRADE + " " + course;
        Loc.put(S, grade);
        userRef = fStore.collection("Grades").document(studentID);
        userRef.set(Loc, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(exercicePDFs.this, "Grade Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(exercicePDFs.this, "Failed to update the grade", Toast.LENGTH_SHORT).show();
            }
        });

    }
}