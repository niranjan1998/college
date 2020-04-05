package com.example.college;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class dash_notes_adapter extends RecyclerView.Adapter<dash_notes_adapter.NotesViewHolder> {

    private Context context;
    private List<dash_notes_model> notesList;


    public dash_notes_adapter() {
    }

    dash_notes_adapter(Context context, List<dash_notes_model> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notes_recycle_item, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesViewHolder holder, final int i) {

        holder.notes_names.setText(notesList.get(i).getName());

        // holder.e_date.setText(dash_notes_list.get(position).getEvent_date());
        holder.cardView.setOnClickListener(new View.OnClickListener() {

            //dash_notes_list upload = dash_notes_model.get(i);
            dash_notes_model uploads = notesList.get(i);
            Uri uri = Uri.parse(uploads.getUrl());

            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse(String.valueOf(uri)), "application/pdf");
                context.startActivity(intent);

            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to Delete ?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // removeItem(notesList.remove(notesList.get(i)));
                                //    String urls = notesList.get(i).getUrl();
                                removeItem(notesList.remove(notesList.get(i)));
/*
                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Events");
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageReference = storage.getReferenceFromUrl(imgUrl);
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        databaseReference.child(key).removeValue();
                                        Toast.makeText(context, "Deleted notes" + i, Toast.LENGTH_SHORT).show();
                                    }

                                 */
                             /*   notesList.remove(notesList.get(i));
                                notifyDataSetChanged();
                                notifyItemRemoved(i);
                                Toast.makeText(context,"Deleted notes" + i,Toast.LENGTH_SHORT).show();*/
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
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    private void removeItem(boolean url) {
        int i = notesList.indexOf(url);
        notesList.remove(url);

        DatabaseReference demo = FirebaseDatabase.getInstance().getReference("storebooks").child("BSC").child("SEMESTER-1");

        demo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        notifyItemRemoved(i);
    }


    public class NotesViewHolder extends RecyclerView.ViewHolder {


        TextView notes_names;
        CardView cardView;

        public NotesViewHolder(@NonNull final View itemView) {
            super(itemView);

            notes_names = itemView.findViewById(R.id.notes_name);

            cardView = itemView.findViewById(R.id.notes_card);
        }
    }
}
