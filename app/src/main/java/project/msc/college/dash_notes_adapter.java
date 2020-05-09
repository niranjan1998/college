package project.msc.college;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class dash_notes_adapter extends RecyclerView.Adapter<dash_notes_adapter.NotesViewHolder> {

    private Context context;
    private List<dash_notes_model> notesList;
    private String name, key, stream, sem, extra, url;


    public dash_notes_adapter() {
    }

    dash_notes_adapter(Context context, List<dash_notes_model> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notes_recycle_item, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesViewHolder holder, final int i) {

        holder.notes_names.setText(notesList.get(i).getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {

            dash_notes_model uploads = notesList.get(i);

            @Override
            public void onClick(View v) {
                String name = notesList.get(i).getUrl();
                if (name.contains("pdf")) {
                    Intent intent = new Intent();
                    //    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(uploads.getUrl()), "application/pdf");
                    //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    //    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(uploads.getUrl()), "application/*");
                    //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    Toast.makeText(context, "Download files", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //to get values
        final Intent intent = new Intent(context, dash_notes_list.class);
        intent.putExtra("card_name", notesList.get(holder.getAdapterPosition()).getName());
        name = intent.getStringExtra("card_name");
        intent.putExtra("card_key", notesList.get(holder.getAdapterPosition()).getKey());
        key = intent.getStringExtra("card_key");
        intent.putExtra("card_stream", notesList.get(holder.getAdapterPosition()).getStream());
        stream = intent.getStringExtra("card_stream");
        intent.putExtra("card_sem", notesList.get(holder.getAdapterPosition()).getSem());
        sem = intent.getStringExtra("card_sem");
        intent.putExtra("card_url", notesList.get(holder.getAdapterPosition()).getUrl());
        url = intent.getStringExtra("card_url");
        intent.putExtra("card_extra", notesList.get(holder.getAdapterPosition()).getExtras());
        extra = intent.getStringExtra("card_extra");


        SharedPreferences result = context.getSharedPreferences("loginRef", Context.MODE_PRIVATE);
        String role = result.getString("role", "");

        if (role.equals("Teacher")) {
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to Delete ?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    notesList.remove(notesList.get(i));

                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference storageReference = storage.getReference("storeBooks").child(stream).child(sem);
                                    storageReference.child(name).delete();

                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("storeBooks").child(stream).child(sem).child(extra);
                                    databaseReference.child(key).removeValue();
                                    notifyItemRemoved(i);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Deleted" + i, Toast.LENGTH_SHORT).show();
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


        holder.more_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.select)
                        .setItems(R.array.Selected_notes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    download();
                                } else if (which == 1) {
                                    share();
                                }
                            }

                            String url = String.valueOf(notesList.get(holder.getLayoutPosition()).getUrl());

                            private void share() {
                                Intent intent = new Intent(Intent.ACTION_SEND);

                                intent.setType("text/plain");

                                intent.putExtra(Intent.EXTRA_TEXT, url);

                                context.startActivity(Intent.createChooser(intent, "Share Using"));

                            }

                            private void download() {
                                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                                Uri uri = Uri.parse(url);
                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                assert downloadManager != null;
                                long ref = downloadManager.enqueue(request);

                                Toast.makeText(context, "File Downloading", Toast.LENGTH_SHORT).show();

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }


    static class NotesViewHolder extends RecyclerView.ViewHolder {


        TextView notes_names;
        CardView cardView;
        AppCompatImageView more_icon;

        NotesViewHolder(@NonNull final View itemView) {
            super(itemView);

            notes_names = itemView.findViewById(R.id.notes_name);

            cardView = itemView.findViewById(R.id.notes_card);

            more_icon = itemView.findViewById(R.id.notes_more);
        }
    }
}
