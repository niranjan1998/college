package com.example.college;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class dash_notes_adapter extends RecyclerView.Adapter<dash_notes_adapter.NotesViewHolder>{

    Context context;
    List<dash_notes_model> notesList;


    public dash_notes_adapter(Context context, List<dash_notes_model> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notes_recycle_item, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesViewHolder holder,final int i) {

        holder.notes_names.setText(notesList.get(i).getName());


       // holder.e_date.setText(dash_notes_list.get(position).getEvent_date());
        holder.cardView.setOnClickListener(new View.OnClickListener() {

            //dash_notes_list upload = dash_notes_model.get(i);
             dash_notes_model uploads = notesList.get(i);
                 Uri uri = Uri.parse(uploads.getUrl());
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(String.valueOf(uri)), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
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
