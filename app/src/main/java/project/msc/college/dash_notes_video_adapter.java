package project.msc.college;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class dash_notes_video_adapter extends RecyclerView.Adapter<dash_notes_video_adapter.NotesViewHolder> {

    private Context context;
    private List<dash_notes_model> notesList;

    public dash_notes_video_adapter() {
    }

    dash_notes_video_adapter(Context context, List<dash_notes_model> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_notes_item, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesViewHolder holder, final int i) {
        holder.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);

                String kk = notesList.get(i).getUrl();

                youTubePlayer.cueVideo(kk, 0);

            }
        });

        holder.youtube_title.setText(notesList.get(i).getName());

        holder.more_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.select_video)
                        .setItems(R.array.Selected_video, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    download();
                                } else if (which == 1) {
                                    share();
                                } else if (which == 2) {
                                    delete();
                                }
                            }

                            String name = String.valueOf(notesList.get(holder.getLayoutPosition()).getName());
                            String key = String.valueOf(notesList.get(holder.getLayoutPosition()).getKey());
                            String stream = String.valueOf(notesList.get(holder.getLayoutPosition()).getStream());
                            String sem = String.valueOf(notesList.get(holder.getLayoutPosition()).getSem());
                            String extra = String.valueOf(notesList.get(holder.getLayoutPosition()).getExtras());
                            String url = String.valueOf(notesList.get(holder.getLayoutPosition()).getUrl());


                            private void delete() {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("storeBooks").child(stream).child(sem).child(extra);
                                databaseReference.child(key).removeValue();
                                notifyItemRemoved(i);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Deleted" + i, Toast.LENGTH_SHORT).show();
                            }

                            private void share() {

                                Intent intent = new Intent(Intent.ACTION_SEND);

                                intent.setType("text/plain");

                                String message = "https://youtu.be/" + url.trim();

                                intent.putExtra(Intent.EXTRA_TEXT, message);

                                context.startActivity(Intent.createChooser(intent, "Share Using"));

                            }

                            private void download() {
                                /*DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                                Uri uri = Uri.parse(url);
                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                assert downloadManager != null;
                                long ref = downloadManager.enqueue(request);*/

                                Toast.makeText(context, "YouTube Error", Toast.LENGTH_SHORT).show();

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

        YouTubePlayerView youTubePlayerView;
        MaterialTextView youtube_title;
        AppCompatImageView more_icon;

        NotesViewHolder(@NonNull final View itemView) {
            super(itemView);

            youTubePlayerView = itemView.findViewById(R.id.youtube_view);

            youtube_title = itemView.findViewById(R.id.youtube_title);

            more_icon = itemView.findViewById(R.id.youtube_more);
        }
    }
}
