package com.example.college;

import android.content.Context;
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

public class my_class_adapter extends RecyclerView.Adapter<my_class_adapter.ClassViewHolder> {

    private Context context;
    private List<UserHelperClass> usersList;

    my_class_adapter(Context context, List<UserHelperClass> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_class_item, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ClassViewHolder holder, int position) {

        Glide.with(context)
                .load(usersList.get(position).getPic()).into(holder.imageView);

        holder.u_name.setText(usersList.get(position).getName());
        holder.u_roll.setText(usersList.get(position).getRoll());
        holder.u_role.setText(usersList.get(position).getRole());
        holder.u_class.setText(usersList.get(position).getStream());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    static class ClassViewHolder extends RecyclerView.ViewHolder {

        TextView u_name, u_roll, u_role, u_class;
        CardView cardView;
        ImageView imageView;

        ClassViewHolder(@NonNull View itemView) {
            super(itemView);

            u_name = itemView.findViewById(R.id.person_name);
            u_roll = itemView.findViewById(R.id.person_roll);
            u_role = itemView.findViewById(R.id.person_role);
            u_class = itemView.findViewById(R.id.person_class);
            imageView = itemView.findViewById(R.id.person_img);
            cardView = itemView.findViewById(R.id.classCardView);

        }
    }
}
