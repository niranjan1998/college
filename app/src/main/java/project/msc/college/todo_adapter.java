package project.msc.college;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class todo_adapter extends RecyclerView.Adapter<todo_adapter.TodoViewHolder> implements Filterable {

    private Context context;
    private List<todo_model> todo_modelList;
    private List<todo_model> todo_modelListAll;


    todo_adapter(Context context, List<todo_model> todo_modelList) {
        this.context = context;
        this.todo_modelList = todo_modelList;
        this.todo_modelListAll = new ArrayList<>(todo_modelList);
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, final int position) {

        holder.txt_title.setText(todo_modelList.get(position).getTitle());
        holder.txt_desc.setText(todo_modelList.get(position).getDesc());
        holder.txt_do_date.setText(todo_modelList.get(position).getDate());
        holder.txt_po_date.setText(todo_modelList.get(position).getPo_date());
        holder.txt_tech.setText(todo_modelList.get(position).getTech_name());

        String date = todo_modelList.get(position).getDate();

        Date date1 = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d,yyyy", Locale.getDefault());
        String newDate = simpleDateFormat.format(date1);
        //Toast.makeText(context,newDate,Toast.LENGTH_SHORT).show();
        if (date.trim().equals(newDate)) {
            holder.txt_do_date.setTextColor(Color.RED);
        //    holder.card_item.setCardBackgroundColor(Color.parseColor("#FFEBEB"));
            // holder.txt_do_date.setOutlineAmbientShadowColor(Color.RED);
        } else {
            holder.txt_do_date.setTextColor(Color.GREEN);
        }


        //to get key of comment
        final Intent intent = new Intent(context, todo_main.class);
        intent.putExtra("group_name", todo_modelList.get(holder.getAdapterPosition()).getGrp_class());
        intent.putExtra("card_id", todo_modelList.get(holder.getAdapterPosition()).getId());
        final String i_group = intent.getStringExtra("group_name");
        final String i_id = intent.getStringExtra("card_id");


        SharedPreferences result = context.getSharedPreferences("loginRef", Context.MODE_PRIVATE);
        String role = result.getString("role", "");

        if (role.equals("Teacher")) {
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.select_todo)
                            .setItems(R.array.Selected, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        delete_To_Do();
                                    } else if (which == 1) {
                                        change_completed();
                                    } else if (which == 2) {
                                        change_not_completed();
                                    }

                                }

                                private void change_not_completed() {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("todoData");
                                    databaseReference.child(i_group).child(i_id).child("status").setValue("notCompleted");
                                    notifyDataSetChanged();
                                    notifyItemRemoved(position);
                                    Toast.makeText(context, "To-Do Completed", Toast.LENGTH_SHORT).show();
                                }

                                private void change_completed() {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("todoData");
                                    databaseReference.child(i_group).child(i_id).child("status").setValue("Completed");
                                    notifyDataSetChanged();
                                    notifyItemRemoved(position);
                                    Toast.makeText(context, "To-Do Completed", Toast.LENGTH_SHORT).show();
                                }

                                private void delete_To_Do() {
                                    todo_modelList.remove(todo_modelList.get(position));
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("todoData");
                                    databaseReference.child(i_group).child(i_id).removeValue();
                                    notifyDataSetChanged();
                                    notifyItemRemoved(position);
                                    Toast.makeText(context, "Deleted" + position, Toast.LENGTH_SHORT).show();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return todo_modelList.size();
    }

    @Override
    public Filter getFilter() {

        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<todo_model> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(todo_modelListAll);
            } else {
                for (todo_model data : todo_modelListAll) {
                    if (data.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(data);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            todo_modelList.clear();
            todo_modelListAll.addAll((Collection<? extends todo_model>) results.values);
            notifyDataSetChanged();
        }
    };


    static class TodoViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title, txt_desc, txt_do_date, txt_po_date, txt_tech;
        CardView cardView, card_item;

        TodoViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_title = itemView.findViewById(R.id.todo_title);
            txt_desc = itemView.findViewById(R.id.todo_desc);
            txt_do_date = itemView.findViewById(R.id.todo_do_date);
            txt_po_date = itemView.findViewById(R.id.todo_post_date);
            txt_tech = itemView.findViewById(R.id.todo_tech);
            cardView = itemView.findViewById(R.id.todo_card);
            card_item = itemView.findViewById(R.id.todo_card_item);

        }
    }
}
