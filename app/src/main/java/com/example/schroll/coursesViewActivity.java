package com.example.schroll;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class coursesViewActivity extends AppCompatActivity {
    public static final String EXTRA_COURSE2 = "EXTRA_COURSE2";
    public static final String EXTRA_CLASSCODE2 = "EXTRA_CLASSCODE2";

    public static ArrayList<courseModel > documentsArrayList;
    RecyclerView recyclerView;
    FirebaseStorage documents;
    ProgressBar progress;
    String uri, courseName, classCode;
    int j;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_view);
        progress=findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        courseName = intent.getStringExtra(Course_01_Activity.EXTRA_COURSE);
        classCode = intent.getStringExtra(Course_01_Activity.EXTRA_CLASSCODE);
        courseList();
    }

    public void courseList(){
        documents = FirebaseStorage.getInstance();
        StorageReference listRef = documents.getReference().child("Course Uploads/" +classCode +"/" +courseName +"/");
        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        System.out.println(listResult.getItems());
                        String[] array = new String[listResult.getItems().size()];
                        for(j = 0; j < listResult.getItems().size(); j++)
                        {
                            String title;
                            title = listResult.getItems().get(j).getName();
                            array[j] = title;
                            System.out.println(j);
                            courseModel Model = new courseModel();
                            documentsArrayList = new ArrayList<>();
                            System.out.println(j);
                            String dt= listResult.getItems().get(j).getName();
                            listResult.getItems()
                                    .get(j).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                            @Override
                                                public void onSuccess(Uri uri) {
                                                    Uri data = Uri.parse(uri.toString());
                                                    Model.setName(title);
                                                    Model.setData(dt);
                                                    documentsArrayList.add(Model);
                                                    System.out.println("Done");
                                                    recyclerView = findViewById(R.id.recyclerview2);
                                                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                    courseAdapter  adapter = new courseAdapter (getApplicationContext(), documentsArrayList);
                                                    recyclerView.setAdapter(adapter);
                                                    adapter.setOnItemClickListener(new courseAdapter.OnItemClickListener() {

                                                        @Override
                                                        public void onItemClick(int pos, View v) {
                                                            Intent intent = new Intent(getApplicationContext(), courseViewActivity.class);
                                                            intent.putExtra("pos", pos);
                                                            intent.putExtra(EXTRA_COURSE2, courseName);
                                                            intent.putExtra(EXTRA_CLASSCODE2, classCode);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    progress.setVisibility(View.GONE);
                                            }
                                    }
                            );
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"download unsuccessful",Toast.LENGTH_LONG).show();
            }
        });

    }
    }