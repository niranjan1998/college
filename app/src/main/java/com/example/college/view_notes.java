package com.example.college;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class view_notes extends AppCompatActivity {

    Button uploadFile_btn, selectFile_btn;
    TextInputLayout file_names;
    EditText s_Filename;
    TextView tv_main, tv_sem;

    Uri pdfUri;
    ProgressDialog progressDialog;

    StorageReference storage;
    DatabaseReference database;

    String Stream_path;
    String Sem_path;

    ListView listView;

    //list to store uploads data
    List<uploads_fmodel> uploads_fModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);

        storage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference("notebooks");

        selectFile_btn = findViewById(R.id.SelectFile_btn);
        uploadFile_btn = findViewById(R.id.uploadFile_btn);
        file_names = findViewById(R.id.file_names);
        s_Filename = findViewById(R.id.file_name);

        tv_sem = findViewById(R.id.sem_path);
        tv_main = findViewById(R.id.main_path);


        Intent intent = getIntent();
        String main = intent.getStringExtra("main");
        String sem = intent.getStringExtra("sem");

        tv_main.setText(main);
        tv_sem.setText(sem);


        Stream_path = tv_main.getText().toString();
        Sem_path = tv_sem.getText().toString();


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

        //for viewing files
        uploads_fModels = new ArrayList<>();
        listView = findViewById(R.id.listView);

        //viewAllFiles();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the upload
                uploads_fmodel upload = uploads_fModels.get(i);

                Uri uri = Uri.parse(upload.getUrl());

                //Opening the upload file in browser using the upload url

                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                String mimeType = myMime.getMimeTypeFromExtension(fileExt(uri.toString()));
                newIntent.setDataAndType(uri, mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Intent intent = Intent.createChooser(newIntent, "Open File");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "something happened!", Toast.LENGTH_LONG).show();
                }
            }
        });
       /* listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view_notes.this);
                builder.setMessage("Delete this file ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploads_fmodel upload = uploads_fModels.get(i);

                                Uri uri = Uri.parse(upload.getUrl());
                                removeItem(uri.toString());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.setTitle("Confirm");
                dialog.show();
                return true;
            }
        });*/

    }

    public void removeItem(String url) {
        int i = uploads_fModels.indexOf(url);
        uploads_fModels.remove(url);
        //getting storage reference
        final StorageReference sRef = storage.child("notebooks/").child(Stream_path).child(Sem_path);
        //getting the database reference
        DatabaseReference file_reference = FirebaseDatabase.getInstance().getReference("notebooks");
        //retrieving upload data from firebase database
        file_reference.child(Stream_path).child(Sem_path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String fileExt(String url) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.contains("%")) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.contains("/")) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    private void viewAllFiles() {

        //getting the database reference
        DatabaseReference file_reference = FirebaseDatabase.getInstance().getReference("notebooks");
        //retrieving upload data from firebase database
        file_reference.child(Stream_path).child(Sem_path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    uploads_fmodel upload = postSnapshot.getValue(uploads_fmodel.class);
                    uploads_fModels.add(upload);

                }

                String[] uploads = new String[uploads_fModels.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uploads_fModels.get(i).getName();
                }


                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                listView.setAdapter(adapter);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

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
        if (s_textFile.trim().equals("")) {
            s_Filename.setError("Enter File Name");
        } else {
            Intent intent = new Intent();
            intent.setType("application/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//to fetch files
            startActivityForResult(intent, 86);
        }
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

       // final String file_nameText = s_Filename.getText().toString() + ".pdf";

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading FIle");
        progressDialog.setProgress(0);
        progressDialog.show();

        StorageReference sRef = storage.child("notebooks/").child(Stream_path).child(Sem_path).child(s_Filename.getText().toString());
        sRef.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                database.child(Stream_path).child(Sem_path).child(s_Filename.getText().toString()).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(view_notes.this, "File successfully Uploaded !!!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(view_notes.this, notes_view.class));
                                            finish();
                                        } else {

                                            progressDialog.dismiss();
                                            Toast.makeText(view_notes.this, "File Upload Failed !!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
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


/*
listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the upload
                uploads_fmodel upload = uploads_fModels.get(i);

                Uri uri = Uri.parse(upload.getUrl());
                //Opening the upload file in browser using the upload url
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                Intent newIntent = Intent.createChooser(intent, "Open File");
                try {
                    startActivity(newIntent);
                } catch (ActivityNotFoundException e) {
                }
            }
        });
*/

/*
imeTypeMap myMime = MimeTypeMap.getSingleton();
Intent newIntent = new Intent(Intent.ACTION_VIEW);
String mimeType = myMime.getMimeTypeFromExtension(fileExt(getFile()).substring(1));
newIntent.setDataAndType(Uri.fromFile(getFile()),mimeType);
newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
try {
    context.startActivity(newIntent);
} catch (ActivityNotFoundException e) {
    Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
}
Using this function:

private String fileExt(String url) {
    if (url.indexOf("?") > -1) {
        url = url.substring(0, url.indexOf("?"));
    }
    if (url.lastIndexOf(".") == -1) {
        return null;
    } else {
        String ext = url.substring(url.lastIndexOf(".") + 1);
        if (ext.indexOf("%") > -1) {
            ext = ext.substring(0, ext.indexOf("%"));
        }
        if (ext.indexOf("/") > -1) {
            ext = ext.substring(0, ext.indexOf("/"));
        }
        return ext.toLowerCase();

    }
}
*/