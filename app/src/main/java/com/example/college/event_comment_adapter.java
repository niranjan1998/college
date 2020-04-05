package com.example.college;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class event_comment_adapter extends RecyclerView.Adapter<event_comment_adapter.CommentViewHolder> {

    private Context context;
    private List<comment_model> commentList;
    private String key;


    event_comment_adapter(Context context, List<comment_model> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_event_comment_item, parent, false);
        return new CommentViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, final int i) {

        holder.c_name.setText(commentList.get(i).getCmt_name());
        holder.c_class.setText(commentList.get(i).getCmt_class());
        holder.c_comment.setText(commentList.get(i).getCmt_comment());
        holder.c_date.setText(commentList.get(i).getKey());


        Bundle bundle = ((Activity) context).getIntent().getExtras();

        if (bundle != null) {
            key = bundle.getString("keyValue");
        }

        //to get key of comment
        final Intent intent = new Intent(context, event_expand.class);
        intent.putExtra("date", commentList.get(holder.getAdapterPosition()).getKey());
        final String date = intent.getStringExtra("date");

        SharedPreferences result = context.getSharedPreferences("loginRef", Context.MODE_PRIVATE);
        String name = result.getString("name", "");

        if (name.equals(commentList.get(holder.getAdapterPosition()).getCmt_name())){
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to Delete ?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    commentList.remove(commentList.get(i));
                                    //comment_model comment ;
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Events").child(key);/*.child("20-Mar-2020 12:36:54 PM");*/
                                    databaseReference.child("comments").child(date).removeValue();
                                    notifyDataSetChanged();
                                    notifyItemRemoved(i);
                                    Toast.makeText(context, "Comment Deleted" + i, Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    static class CommentViewHolder extends RecyclerView.ViewHolder {


        TextView c_name, c_class, c_date, c_comment;
        CardView cardView;

        CommentViewHolder(@NonNull final View itemView) {
            super(itemView);

            c_name = itemView.findViewById(R.id.c_name);
            c_class = itemView.findViewById(R.id.c_class);
            c_comment = itemView.findViewById(R.id.c_comment);
            c_date = itemView.findViewById(R.id.c_date);
            cardView = itemView.findViewById(R.id.event_comment_card);
        }
    }
}
