package com.example.college;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.List;

public class event_adapter extends RecyclerView.Adapter<EventViewHolder> {

    private Context context;
    private List<event_model> eventList;


    public event_adapter(Context context, List<event_model> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_event_recycle_item,parent,false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder holder, int i) {

        Glide.with(context)
                .load(eventList.get(i).getEvent_img()).into(holder.imageView);
        //holder.imageView.setImageResource(eventList.get(i).getEvent_img());
        holder.e_title.setText(eventList.get(i).getEvent_name());
        holder.e_dep.setText(eventList.get(i).getEvent_dep());
        holder.e_date.setText(eventList.get(i).getEvent_date());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,event_expand.class);
                intent.putExtra("image",eventList.get(holder.getAdapterPosition()).getEvent_img());
                intent.putExtra("title",eventList.get(holder.getAdapterPosition()).getEvent_name());
                intent.putExtra("dep",eventList.get(holder.getAdapterPosition()).getEvent_dep());
                intent.putExtra("date",eventList.get(holder.getAdapterPosition()).getEvent_date());
                intent.putExtra("keyValue",eventList.get(holder.getAdapterPosition()).getKey());
                context.startActivity(intent);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to Delete this event ?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //removeItem(items.get(getAdapterPosition()));
                                deleteEvent();
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

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void deleteEvent(){
   /*
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Events");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(imgUrl);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(key).removeValue();

            }
        });*/
    }
}

class EventViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView e_title,e_dep,e_date;
    CardView cardView;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.event_img);
        e_title = itemView.findViewById(R.id.tv_title);
        e_dep = itemView.findViewById(R.id.tv_dep);
        e_date = itemView.findViewById(R.id.tv_date);
        cardView = itemView.findViewById(R.id.event_card);
    }
}
