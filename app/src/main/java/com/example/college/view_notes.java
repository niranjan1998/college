package com.example.college;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class view_notes extends AppCompatActivity {

    Button uploadFile_btn, selectFile_btn;
    TextInputLayout file_names;
    EditText s_Filename;

    Uri pdfUri;
    ProgressDialog progressDialog;

    StorageReference storage;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);

        storage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference("books");

        selectFile_btn = findViewById(R.id.SelectFile_btn);
        uploadFile_btn = findViewById(R.id.uploadFile_btn);
        file_names = findViewById(R.id.file_names);
        s_Filename = findViewById(R.id.file_name);


        selectFile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check permission of file system
                if (ContextCompat.checkSelfPermission(view_notes.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPdf();
                } else {
                    ActivityCompat.requestPermissions(view_notes.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });

        uploadFile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdfUri != null) {
                    uploadFiles(pdfUri);
                } else {
                    Toast.makeText(view_notes.this, "File Not Selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        } else {
            Toast.makeText(view_notes.this, "please provide permissions..", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectPdf() {
        String s_textFile = s_Filename.getText().toString();
        if(s_textFile.trim().equals("")){
           s_Filename.setError("Enter File Name");
        }else{
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);//to fetch files
        startActivityForResult(intent, 86);}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //check whether user has selected a file or not
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            selectFile_btn.setVisibility(View.GONE);
            uploadFile_btn.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(view_notes.this, "File Not Selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadFiles(Uri pdfUri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading FIle");
        progressDialog.setProgress(0);
        progressDialog.show();

            StorageReference sRef = storage.child("books/" + System.currentTimeMillis() + ".pdf");
            sRef.putFile(pdfUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            uploads_fmodel upload = new uploads_fmodel(s_Filename.getText().toString(), taskSnapshot.getStorage().getDownloadUrl().toString());
                            database.child(database.push().getKey()).setValue(upload);

                            Toast.makeText(view_notes.this, "File Uploaded", Toast.LENGTH_SHORT).show();

                            s_Filename.setText("");
                            progressDialog.dismiss();
                            selectFile_btn.setVisibility(View.VISIBLE);
                            uploadFile_btn.setVisibility(View.INVISIBLE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setProgress(currentProgress);
                        }
                    });
        }
}