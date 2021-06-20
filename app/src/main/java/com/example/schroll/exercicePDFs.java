package com.example.schroll;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shockwave.pdfium.PdfDocument;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class exercicePDFs extends AppCompatActivity implements OnLoadCompleteListener,OnPageChangeListener {
    private static final String KEY_GRADE ="Grade";
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseStorage fStorage =FirebaseStorage.getInstance();
    StorageReference fStorageRef;
    private CollectionReference students = fStore.collection("Students");
    PDFView pdfView;
    Integer pageNumber = 0;
    Handler mHandler,handler;
    EditText gradeHW;
    TextView textView;
    FirebaseAuth fAuth;
    DocumentReference userRef;
    String studentID, title, userID, course;
    int pdf_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice_p_d_fs);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdf_index = getIntent().getIntExtra("pos" , 0);
        mHandler = new Handler();
        handler = new Handler();
        title = pdf_list.documentArrayList.get(pdf_index).getData();
        userID = fAuth.getCurrentUser().getUid();
        gradeHW = findViewById(R.id.hwGrade);
        textView = findViewById(R.id.textView17);

        Intent intent = getIntent();
        course = intent.getStringExtra(pdf_list.EXTRA_COURSE);

        findStudentID(title);

        fStorageRef = fStorage.getReference().child("Pdf Uploads/" +course +"/");
        final long ONE_MEGABYTE = 1024 * 1024;

        fStorageRef.child(pdf_list.documentArrayList.get(pdf_index).getData()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                pdfView.fromBytes(bytes).load();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"download unsuccessful",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    public void findStudentID(String title){
        title = title.substring(0, title.length() - 4 );

        students.whereEqualTo("Surname",title ).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        studentID = document.getId();
                    }
                } else {
                    Toast.makeText(exercicePDFs.this, "Error getting document", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setGradeHW(View v) {
        String grade = gradeHW.getText().toString();

        Map<String, Object> Loc = new HashMap<>();
        String S = KEY_GRADE +" "+ course;
        Loc.put(S, grade);
        userRef = fStore.collection("Grades").document("" +studentID);
        userRef.set(Loc, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(exercicePDFs.this, "Grade Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}