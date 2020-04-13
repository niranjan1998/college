package com.example.college;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notes_recycle_item, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesViewHolder holder, final int i) {

        holder.notes_names.setText(notesList.get(i).getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {

            //dash_notes_list upload = dash_notes_model.get(i);

            dash_notes_model uploads = notesList.get(i);
            Uri uri = Uri.parse(uploads.getUrl());

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                //    intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(uploads.getUrl()), "application/pdf");
                //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                                String ref = notesList.get(i).getName();
                                notesList.remove(notesList.get(i));
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("storeBooks");
                                databaseReference.child("name").child(ref).removeValue();
                                notifyDataSetChanged();
                                notifyItemRemoved(i);
                                Toast.makeText(context, "Deleted" + i, Toast.LENGTH_SHORT).show();
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


    static class NotesViewHolder extends RecyclerView.ViewHolder {


        TextView notes_names;
        CardView cardView;

        NotesViewHolder(@NonNull final View itemView) {
            super(itemView);

            notes_names = itemView.findViewById(R.id.notes_name);

            cardView = itemView.findViewById(R.id.notes_card);
        }
    }
}
