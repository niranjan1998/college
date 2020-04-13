package com.example.college;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Random;

public class grp_adapter extends RecyclerView.Adapter {

    private List<grp_model> msgList;
    private Context context;

    grp_adapter(Context context, List<grp_model> msgList) {
        this.msgList = msgList;
        this.context = context;
    }


    @Override
    public int getItemViewType(int position) {
        SharedPreferences result = context.getSharedPreferences("loginRef", Context.MODE_PRIVATE);
        String name = result.getString("name", "");


        if (msgList.get(position).getUser_name().contains(name)) {
            return grp_model.send_text;
            }
            return grp_model.rec_text;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == grp_model.send_text) {

            view = layoutInflater.inflate(R.layout.grp_send_item, parent, false);
            return new MessageViewHolderSend(view);
        } else {
            view = layoutInflater.inflate(R.layout.grp_rec_item, parent, false);
            return new MessageViewHolderRec(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        SharedPreferences result = context.getSharedPreferences("loginRef", Context.MODE_PRIVATE);
        String name = result.getString("name", "");


        //to view group name from sp
        SharedPreferences sp_grp_name = context.getSharedPreferences("spGrpName", Context.MODE_PRIVATE);
        final String grp_names = sp_grp_name.getString("name", "");

        //to get key of comment
        final Intent intent = new Intent(context, grp_chat.class);
        intent.putExtra("date", msgList.get(holder.getAdapterPosition()).getUser_key());
        final String date = intent.getStringExtra("date");

        final String img = "image";

        if (msgList.get(position).getUser_name().contains(name)) {

            if(msgList.get(position).getUser_msg_type().equals(img)){
                final MessageViewHolderSend messageViewHolderSend = (MessageViewHolderSend) holder;
                messageViewHolderSend.s_name.setText(msgList.get(position).getUser_name());
                messageViewHolderSend.s_msg.setText(msgList.get(position).getUser_msg());

                Glide.with(context)
                        .load(msgList.get(position).getUser_msg()).into(((MessageViewHolderSend) holder).s_img);
                  messageViewHolderSend.s_msg.setVisibility(View.GONE);
                messageViewHolderSend.s_date.setText(msgList.get(position).getUser_key());
            }
            else {
                final MessageViewHolderSend messageViewHolderSend = (MessageViewHolderSend) holder;
                messageViewHolderSend.s_name.setText(msgList.get(position).getUser_name());
                messageViewHolderSend.s_msg.setText(msgList.get(position).getUser_msg());
                messageViewHolderSend.s_img.setVisibility(View.GONE);
                messageViewHolderSend.s_date.setText(msgList.get(position).getUser_key());
            }

                ((MessageViewHolderSend) holder).cardViewSend.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Do you want to Delete ?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        msgList.remove(msgList.get(position));

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups").child(grp_names).child("messages");
                                        databaseReference.child(date).removeValue();
                                        notifyDataSetChanged();
                                        notifyItemRemoved(position);
                                        Toast.makeText(context, "Message Deleted" + position, Toast.LENGTH_SHORT).show();
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
        else if((!msgList.get(position).getUser_name().contains(name)) &&( msgList.get(position).getUser_msg_type().contains(img) ))
        {
            Random random = new Random();
            int color = Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));

            MessageViewHolderRec messageViewHolderRec = (MessageViewHolderRec) holder;
            messageViewHolderRec.r_name.setText(msgList.get(position).getUser_name());
            messageViewHolderRec.r_name.setTextColor(color);

            messageViewHolderRec.r_msg.setText(msgList.get(position).getUser_msg());

            Glide.with(context).load(msgList.get(position).getUser_msg()).into(((MessageViewHolderRec) holder).r_img);
            messageViewHolderRec.r_msg.setVisibility(View.GONE);
            messageViewHolderRec.r_date.setText(msgList.get(position).getUser_key());
        }
        else
        {
                Random random = new Random();
                int color = Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));

                MessageViewHolderRec messageViewHolderRec = (MessageViewHolderRec) holder;
                messageViewHolderRec.r_name.setText(msgList.get(position).getUser_name());
                messageViewHolderRec.r_name.setTextColor(color);

                messageViewHolderRec.r_msg.setText(msgList.get(position).getUser_msg());

               // Glide.with(context).load(msgList.get(position).getUser_msg()).into(((MessageViewHolderRec) holder).r_img);
                messageViewHolderRec.r_msg.setVisibility(View.VISIBLE);
                messageViewHolderRec.r_img.setVisibility(View.GONE);
                messageViewHolderRec.r_date.setText(msgList.get(position).getUser_key());
            }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    static class MessageViewHolderSend extends RecyclerView.ViewHolder {


        TextView s_name, s_date, s_msg;
        ImageView s_img;
        CardView cardViewSend;

        MessageViewHolderSend(@NonNull final View itemView) {
            super(itemView);

            s_name = itemView.findViewById(R.id.s_name);
            s_msg = itemView.findViewById(R.id.s_msg);
            s_date = itemView.findViewById(R.id.s_date);
            s_img = itemView.findViewById(R.id.s_img);
            cardViewSend = itemView.findViewById(R.id.grp_send_card);
        }
    }

    static class MessageViewHolderRec extends RecyclerView.ViewHolder {


        TextView r_name, r_date, r_msg;
        ImageView r_img;
        CardView cardViewRec;

        MessageViewHolderRec(@NonNull final View itemView) {
            super(itemView);

            r_name = itemView.findViewById(R.id.r_name);
            r_msg = itemView.findViewById(R.id.r_msg);
            r_date = itemView.findViewById(R.id.r_date);
            r_img = itemView.findViewById(R.id.r_img);
            cardViewRec = itemView.findViewById(R.id.grp_rec_card);
        }
    }
}
