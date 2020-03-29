package com.example.college;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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

        if (msgList.get(position).getUser_name().toString().contains(name)) {
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.grp_send_item, parent, false);
            return new MessageViewHolderSend(view);
        }

        view = layoutInflater.inflate(R.layout.grp_rec_item, parent, false);
        return new MessageViewHolderRec(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
         SharedPreferences result = context.getSharedPreferences("loginRef", Context.MODE_PRIVATE);
         String name = result.getString("name", "");

        if (msgList.get(position).getUser_name().toString().contains(name)) {
            final MessageViewHolderSend messageViewHolderSend = (MessageViewHolderSend) holder;
            messageViewHolderSend.s_name.setText(msgList.get(position).getUser_name());
            messageViewHolderSend.s_msg.setText(msgList.get(position).getUser_msg());
            messageViewHolderSend.s_date.setText(msgList.get(position).getUser_key());

            messageViewHolderSend.s_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  messageViewHolderSend.getAdapterPosition();
                    //comment_model comment ;
                    commentList.get(holder.getAdapterPosition()).getKey())
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups");
                    databaseReference.child("MSc IT 2").child(messageViewHolderSend.).removeValue();
                    notifyDataSetChanged();
                    notifyItemRemoved(i);
                    Toast.makeText(context, "Comment Deleted" + i, Toast.LENGTH_SHORT).show();*/
                }
            });

        } else {
            MessageViewHolderRec messageViewHolderRec = (MessageViewHolderRec) holder;
            messageViewHolderRec.r_name.setText(msgList.get(position).getUser_name());
            messageViewHolderRec.r_msg.setText(msgList.get(position).getUser_msg());
            messageViewHolderRec.r_date.setText(msgList.get(position).getUser_key());

        }

    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    static class MessageViewHolderSend extends RecyclerView.ViewHolder {


        TextView s_name, s_date, s_msg;
        CardView cardViewSend;

        MessageViewHolderSend(@NonNull final View itemView) {
            super(itemView);

            s_name = itemView.findViewById(R.id.s_name);
            s_msg = itemView.findViewById(R.id.s_msg);
            s_date = itemView.findViewById(R.id.s_date);
            cardViewSend = itemView.findViewById(R.id.grp_send_card);
        }
    }

    static class MessageViewHolderRec extends RecyclerView.ViewHolder {


        TextView r_name, r_date, r_msg;
        CardView cardViewRec;

        MessageViewHolderRec(@NonNull final View itemView) {
            super(itemView);

            r_name = itemView.findViewById(R.id.r_name);
            r_msg = itemView.findViewById(R.id.r_msg);
            r_date = itemView.findViewById(R.id.r_date);
            cardViewRec = itemView.findViewById(R.id.grp_rec_card);
        }
    }
}
