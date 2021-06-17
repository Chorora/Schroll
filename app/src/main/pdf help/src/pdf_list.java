package com.example.education;

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

public class pdf_list extends AppCompatActivity {
    public static ArrayList<pdfmodel > videoArrayList;
    RecyclerView recyclerView;
    FirebaseStorage videos;
    String uri;
    int j;
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);
        progress=findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        PDFList();
    }
    public void PDFList() {
        videos= FirebaseStorage.getInstance();
        StorageReference listRef = videos.getReference().child("pdfs");
        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        System.out.println(listResult.getItems());

                        for(j=0;j<listResult.getItems().size();j++)
                        {   String title;
                            title = listResult.getItems().get(j).getName();
                            System.out.println(j);
                            pdfmodel  videoModel  = new pdfmodel ();
                            videoArrayList = new ArrayList<>();
                            System.out.println(j);
                            String dt= listResult.getItems().get(j).getName();
                            listResult.getItems()
                                    .get(j).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                                   @Override
                                                                                                   public void onSuccess(Uri uri) {
                                                                                                       Uri data = Uri.parse(uri.toString());
                                                                                                       videoModel.setName(title);
                                                                                                       videoModel.setData(dt);
                                                                                                       videoArrayList.add(videoModel);
                                                                                                       System.out.println("done");
                                                                                                       recyclerView = findViewById(R.id.recyclerview1);
                                                                                                       recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                                                                                       recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                                                                       PdfAdapter  adapter = new PdfAdapter (getApplicationContext(), videoArrayList);
                                                                                                       recyclerView.setAdapter(adapter);
                                                                                                       adapter.setOnItemClickListener(new PdfAdapter.OnItemClickListener() {
                                                                                                           @Override
                                                                                                           public void onItemClick(int pos, View v) {
                                                                                                               Intent intent = new Intent(getApplicationContext(), exercicePDFs.class);
                                                                                                               intent.putExtra("pos", pos);
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