package com.example.college;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Random;

public class event_upload_event extends AppCompatActivity {

    String myCurrentDate = DateFormat.getDateTimeInstance()
            .format(Calendar.getInstance().getTime());

    EditText event_title, event_dep;
    ImageView event_image, e_img;
    Uri uri;
    String imageUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_upload_event);

        MaterialToolbar materialToolbar;
        materialToolbar = findViewById(R.id.toll_bar);
        materialToolbar.setTitle("Upload Events");
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        event_title = findViewById(R.id.ed_event_name);
        event_dep = findViewById(R.id.ed_event_dep);
        event_image = findViewById(R.id.event_up_img);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


    }

    public void btnSelectImage(View view) {

        Intent imgPicker = new Intent(Intent.ACTION_PICK);
        imgPicker.setType("image/*");
        startActivityForResult(imgPicker, 1);
    }

    public void btnTakeImage(View view) {
        Intent imgPicker = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Random random = new Random();
        int key = random.nextInt(1000);
        File photo = new File(Environment.getExternalStorageDirectory(), "picture" + key + ".jpg");
        uri = Uri.fromFile(photo);
        imgPicker.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(imgPicker, 22);
    }
/*
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000, true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            uri = data.getData();
            event_image.setImageURI(uri);
        } else if (requestCode == 22 && resultCode == RESULT_OK) {
            /*
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            event_image.setImageBitmap(bitmap);*/


            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(uri);
            this.sendBroadcast(mediaScanIntent);
            try {

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                event_image.setImageBitmap(bitmap);
              /*  Scanner scanner = new Scanner();
                final Bitmap bitmap = scanner.decodeBitmapUri(event_upload_event.this, uri);

                event_image.setImageBitmap(bitmap);
                uri = this.uri;

                //if (uri.getLastPathSegment().indexOf(".") > 0)
                //filename = uri.getLastPathSegment().substring(0, uri.getLastPathSegment().lastIndexOf("."));

                //  Toast.makeText(getApplicationContext(), filename,Toast.LENGTH_LONG).show();
*/

            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
        }

    }

    public void uploadImage() {
        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference().child("EventImages").child(uri.getLastPathSegment());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Event....");
        progressDialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                uploadEvent();
                progressDialog.dismiss();
            }
        });
    }

    public void uploadEvent() {

        event_model e_model = new event_model(
                event_title.getText().toString(),
                event_dep.getText().toString(),
                imageUrl,
                myCurrentDate
        );


        FirebaseDatabase.getInstance().getReference("Events")
                .child(myCurrentDate).setValue(e_model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(event_upload_event.this, "Event Uploaded", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    public void btn_uploadEvents(View view) {
        uploadImage();
    }

    public void btn_image_view(View view) {
        btnSelectImage( view);
    }
}
