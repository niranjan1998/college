package com.example.college;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class admin_user_adapter extends RecyclerView.Adapter<admin_user_adapter.UserViewHolder> {

    private Context context;
    private List<UserHelperClass> adminUserList;


    public admin_user_adapter(Context context, List<UserHelperClass> adminUserList) {
        this.context = context;
        this.adminUserList = adminUserList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_user_item, parent, false);
        return new UserViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
        Glide.with(context)
                .load(adminUserList.get(position).getPic()).into(holder.imageView);

        holder.u_name.setText(adminUserList.get(position).getName());
        holder.u_roll.setText(adminUserList.get(position).getRoll());
        holder.u_role.setText(adminUserList.get(position).getRole());
        holder.u_email.setText(adminUserList.get(position).getEmail());
        holder.u_phone.setText(adminUserList.get(position).getPhone());
        holder.u_password.setText(adminUserList.get(position).getPassword());


        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, admin_user_editor.class);
                intent.putExtra("image", adminUserList.get(holder.getAdapterPosition()).getPic());
                intent.putExtra("name", adminUserList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("roll", adminUserList.get(holder.getAdapterPosition()).getRoll());
                intent.putExtra("class", adminUserList.get(holder.getAdapterPosition()).getStream());
                intent.putExtra("email", adminUserList.get(holder.getAdapterPosition()).getEmail());
                intent.putExtra("phone", adminUserList.get(holder.getAdapterPosition()).getPhone());
                intent.putExtra("password", adminUserList.get(holder.getAdapterPosition()).getPassword());
                context.startActivity(intent);

                Toast.makeText(context, "edit clicked" + position, Toast.LENGTH_SHORT).show();
            }
        });


        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to get key of comment
                final Intent intent = new Intent(context, admin_show_std.class);
                intent.putExtra("roll", adminUserList.get(holder.getAdapterPosition()).getRoll());
                final String roll = intent.getStringExtra("roll");

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to Delete ?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adminUserList.remove(adminUserList.get(position));
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("UsersData");
                                databaseReference.child(roll).removeValue();
                                notifyDataSetChanged();
                                notifyItemRemoved(position);
                                Toast.makeText(context, "Student Deleted" + position, Toast.LENGTH_SHORT).show();
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
            }
        });

        boolean isExpanded = adminUserList.get(position).isExpanded();
        holder.relativeLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.relativeLayout_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHelperClass userHelperClass = adminUserList.get(holder.getAdapterPosition());
                userHelperClass.setExpanded(!userHelperClass.isExpanded());
                notifyItemChanged(holder.getAdapterPosition());

            }
        });
    }

    @Override
    public int getItemCount() {
        return adminUserList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView u_name, u_roll, u_role, u_email, u_phone, u_password, u_class;
        CardView cardView;
        ImageView imageView;
        Button btn_edit, btn_delete;
        RelativeLayout relativeLayout, relativeLayout_all;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);

            u_name = itemView.findViewById(R.id.user_name);
            u_roll = itemView.findViewById(R.id.user_roll);
            u_role = itemView.findViewById(R.id.user_role);
            u_email = itemView.findViewById(R.id.user_email);
            u_phone = itemView.findViewById(R.id.user_phone);
            u_password = itemView.findViewById(R.id.user_password);
            u_class = itemView.findViewById(R.id.user_class);
            imageView = itemView.findViewById(R.id.user_profile);
            cardView = itemView.findViewById(R.id.userCardView);
            btn_edit = itemView.findViewById(R.id.btn_user_edit);
            btn_delete = itemView.findViewById(R.id.btn_user_delete);
            relativeLayout = itemView.findViewById(R.id.rel_text);
            relativeLayout_all = itemView.findViewById(R.id.rel_all);


        }
    }

}
