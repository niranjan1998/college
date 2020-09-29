package project.msc.college;
//just for tp
// used to create adapter for recyclerview of sem1recyclerview.java to view in notes in sem1 swipe view of notes

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class notes_adapter extends RecyclerView.Adapter<notes_adapter.ViewHolder> {
    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<String> items = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();


    private DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("notebooks").child("BSC").child("sem6");


    void update(String name, String url) {
        items.add(name);
        urls.add(url);
        notifyDataSetChanged();//refreshes the recyclerview automatically
    }

    public notes_adapter(RecyclerView recyclerView, Context context, ArrayList<String> items, ArrayList<String> urls) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
        this.urls = urls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //to create views for recyclerview item
        /*using object of layout inflater for create an object item recycler.xml and
         storing the view in view */
        //creating view for item recycler.xml
        View view = LayoutInflater.from(context).inflate(R.layout.activity_notes_recycle_item, parent, false);
        return new ViewHolder(view);//returns object of view holder class
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //in this initialise the element of individual item
        holder.name.setText(items.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
                                               @SuppressLint("IntentReset")
                                               @Override
                                               public void onClick(View view) {//what happens when you click individual item view

                                                   // int position = recyclerView.getChildLayoutPosition(view);
                                                   Intent intent = new Intent();
                                                   intent.setAction(Intent.ACTION_VIEW);//denotes that we are going to view something
                                                   intent.setDataAndType(Uri.parse(urls.get(position)), "application/pdf");
                                                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                   context.startActivity(intent);
                                               }
                                           }
        );
/*
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to Delete this data ?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // removeItem(items.get(items.get()));
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
        });*/

    }

    @Override
    public int getItemCount() {// return the no of item
        return items.size();
    }

    private void removeItem(String url) {
        int i = items.indexOf(url);
        items.remove(url);

        //database removed
        Log.d("My", url);

        DatabaseReference demo = mReference.child(url);

        demo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        notifyItemRemoved(i);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CardView cardView;

        public ViewHolder(View itemView) {//represents individual list items
            super(itemView);
            //creating object of text_view
            name = itemView.findViewById(R.id.notes_name);
            cardView = itemView.findViewById(R.id.notes_card);

            itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {//what happens when you click individual item view
                                                Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();

                                                /*int position = recyclerView.getChildLayoutPosition(view);
                                                Intent intent = new Intent();
                                                intent.setType(Intent.ACTION_VIEW);//denotes that we are going to view something
                                                intent.setData(Uri.parse(urls.get(position)));
                                                context.startActivity(intent);*/
                                            }
                                        }
            );
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to Delete this data ?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                               //      removeItem(items.get(getAdapterPosition()));
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

    }
}
