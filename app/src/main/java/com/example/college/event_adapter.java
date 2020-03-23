package com.example.college;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class event_adapter extends RecyclerView.Adapter<event_adapter.EventViewHolder> {

    Context context;
    List<event_model> eventList;
    String imgUrl = "";

    public event_adapter(Context context, List<event_model> eventList) {
        this.context = context;
        this.eventList = eventList;

    }

    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_event_recycle_item, parent, false);
        return new EventViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder holder, final int i) {

        // final int currentPosition = i;
        //  final Information infoData = eventList.get(i);
        Glide.with(context)
                .load(eventList.get(i).getEvent_img()).into(holder.imageView);

        //holder.imageView.setImageResource(eventList.get(i).getEvent_img());
        holder.e_title.setText(eventList.get(i).getEvent_name());
        holder.e_dep.setText(eventList.get(i).getEvent_dep());
        holder.e_date.setText(eventList.get(i).getEvent_date());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, event_expand.class);
                intent.putExtra("image", eventList.get(holder.getAdapterPosition()).getEvent_img());
                intent.putExtra("title", eventList.get(holder.getAdapterPosition()).getEvent_name());
                intent.putExtra("dep", eventList.get(holder.getAdapterPosition()).getEvent_dep());
                intent.putExtra("date", eventList.get(holder.getAdapterPosition()).getEvent_date());
                intent.putExtra("keyValue", eventList.get(holder.getAdapterPosition()).getKey());
                context.startActivity(intent);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Open Event then delete :) ").setCancelable(true)
                        .setPositiveButton("OKay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //  deleteEvent(imgUrl);
                            }
                        })
                        .setNegativeButton("bye", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.setTitle("Confirm");
                dialog.show();

                return false;
               /* Toast.makeText(context,"long click"+ i  ,Toast.LENGTH_SHORT).show();
                return true;*/
            }
        });


    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void deleteEvent(String url) {
        String key = "";


        Intent intent = ((Activity) context).getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            key = bundle.getString("keyValue");
            imgUrl = bundle.getString("image");
        }

        int i = eventList.indexOf(imgUrl);
        eventList.remove(imgUrl);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Events");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(imgUrl);
        final String finalKey = key;
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(finalKey).removeValue();
            }
        });
        notifyItemRemoved(i);
    }


    public class EventViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView e_title, e_dep, e_date;
        CardView cardView;

        public EventViewHolder(@NonNull final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.event_img);
            e_title = itemView.findViewById(R.id.tv_title);
            e_dep = itemView.findViewById(R.id.tv_dep);
            e_date = itemView.findViewById(R.id.tv_date);
            cardView = itemView.findViewById(R.id.event_card);


        }
    }
}
