package com.example.schroll;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shockwave.pdfium.PdfDocument;

import java.util.List;

public class courseViewActivity extends AppCompatActivity {


    PDFView lessonPDFView;
    Integer pageNumber = 0;
    String pdfFileName, title, userID, courseName, classCode;
    int lesson_index;
    Handler mHandler,handler;
    EditText gradeHW;
    TextView textView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);

        lessonPDFView = (PDFView) findViewById(R.id.pdfView2);
        lesson_index = getIntent().getIntExtra("pos" , 0);
        mHandler = new Handler();
        handler = new Handler();
        title = coursesViewActivity.documentsArrayList.get(lesson_index).getData();
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        gradeHW = findViewById(R.id.hwGrade);
        textView = findViewById(R.id.textView17);

        Intent intent = getIntent();
        courseName = intent.getStringExtra(coursesViewActivity.EXTRA_COURSE2);
        classCode = intent.getStringExtra(coursesViewActivity.EXTRA_CLASSCODE2);

        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
        StorageReference mmFirebaseStorageRef = mFirebaseStorage.getReference().child("Course Uploads/" +classCode +"/" + courseName+"/");
        final long ONE_MEGABYTE = 1024 * 1024;

        mmFirebaseStorageRef.child(coursesViewActivity.documentsArrayList.get(lesson_index).getData()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                lessonPDFView.fromBytes(bytes).load();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"download unsuccessful",Toast.LENGTH_LONG).show();
            }
        });
    }


    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }


    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = lessonPDFView.getDocumentMeta();
        printBookmarksTree(lessonPDFView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

}