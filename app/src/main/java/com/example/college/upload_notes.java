package com.example.college;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class upload_notes extends AppCompatActivity {

    Button uploadBtn, uploadFile_btn, selectFile_btn;
    EditText folder_name;
    TextView s_Filename;
    TextInputLayout fol_name, file_names;

    Uri pdfUri;
    // ProgressDialog progressDialog;

    RecyclerView recyclerView;

    FirebaseStorage storage;
    FirebaseDatabase rootNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notes);

        storage = FirebaseStorage.getInstance();
        rootNode = FirebaseDatabase.getInstance();

        uploadBtn = findViewById(R.id.upload_btn);
        folder_name = findViewById(R.id.folder_name);
        fol_name = findViewById(R.id.folder_names);
        recyclerView = findViewById(R.id.reFolder);

        selectFile_btn = findViewById(R.id.SelectFile_btn);
        uploadFile_btn = findViewById(R.id.uploadFile_btn);
        s_Filename = findViewById(R.id.file_name);
        file_names = findViewById(R.id.file_names);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // folder();
            }
        });

        selectFile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check permission of file system
                if (ContextCompat.checkSelfPermission(upload_notes.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPdf();
                } else {
                    ActivityCompat.requestPermissions(upload_notes.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);

                }
            }
        });

        uploadFile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pdfUri != null) {
                    uploadFiles(pdfUri);
                } else {
                    Toast.makeText(upload_notes.this, "Select file", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        } else {
            Toast.makeText(upload_notes.this, "please provide permissions..", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);//to fetch files
        startActivityForResult(intent, 86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //check whether user has selected a file or not
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            s_Filename.setText("File Name:"+ data.getData().getLastPathSegment());
        } else {
            Toast.makeText(upload_notes.this, "please select file", Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadFiles(final Uri pdfUri) {
/*
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading FIle");
        progressDialog.setProgress(0);
        progressDialog.show();
*/
        final String fileName = System.currentTimeMillis() + ".pdf";
        StorageReference storageReference = storage.getReference();

        storageReference.child("files").child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getStorage().getDownloadUrl().toString();

                       // Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();

                        DatabaseReference reference = rootNode.getReference("");

                        reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(upload_notes.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(upload_notes.this, "File not uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(upload_notes.this, "File not uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

          //      int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getBytesTransferred());
                //progressDialog.setProgress(currentProgress);
            }
        });

    }

    //empty folder
  /*  public void folder() {
        DatabaseReference references;
        String folderName = folder_name.getText().toString();
        if (folderName.length() != 0) {
            rootNode = FirebaseDatabase.getInstance();
            references = rootNode.getReference("notes");
            references.child(folderName).setValue(folderName);

            Toast.makeText(getApplicationContext(), "folder name" + folderName, Toast.LENGTH_LONG).show();
        } else {
            folder_name.setError("Enter Name");
        }
    }*/

}