package project.msc.college;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class event_adapter extends RecyclerView.Adapter<event_adapter.EventViewHolder> {

    private Context context;
    private List<event_model> eventList;
    private String imgUrl = "";

    event_adapter(Context context, List<event_model> eventList) {
        this.context = context;
        this.eventList = eventList;

    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_event_recycle_item, parent, false);
        return new EventViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder holder, final int i) {

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

        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);

                intent.putExtra("image", eventList.get(holder.getAdapterPosition()).getEvent_img());
                intent.putExtra("title", eventList.get(holder.getAdapterPosition()).getEvent_name());
                intent.putExtra("dep", eventList.get(holder.getAdapterPosition()).getEvent_dep());
                intent.setType("text/plain");

                String send_title = intent.getStringExtra("title");
                String send_dep = intent.getStringExtra("dep");

                intent.setType("image/*");
                String send_img = intent.getStringExtra("image");


                String message = send_title + " " + send_dep;

                intent.putExtra(Intent.EXTRA_TEXT, message);
                // intent.putExtra(Intent.EXTRA_TEXT, send_dep);
                // intent.putExtra(Intent.EXTRA_STREAM, send_img);

                context.startActivity(Intent.createChooser(intent, "Share Using"));

            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView, img_share;
        TextView e_title, e_dep, e_date;
        CardView cardView;

        EventViewHolder(@NonNull final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.event_img);
            img_share = itemView.findViewById(R.id.event_share);
            e_title = itemView.findViewById(R.id.tv_title);
            e_dep = itemView.findViewById(R.id.tv_dep);
            e_date = itemView.findViewById(R.id.tv_date);
            cardView = itemView.findViewById(R.id.event_card);

        }
    }
}
