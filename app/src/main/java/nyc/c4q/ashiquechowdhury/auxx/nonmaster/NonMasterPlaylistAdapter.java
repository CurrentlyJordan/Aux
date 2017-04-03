package nyc.c4q.ashiquechowdhury.auxx.nonmaster;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.ashiquechowdhury.auxx.InfoSlideListener;
import nyc.c4q.ashiquechowdhury.auxx.R;
import nyc.c4q.ashiquechowdhury.auxx.model.PlaylistTrack;

public class NonMasterPlaylistAdapter extends RecyclerView.Adapter<NonMasterPlaylistViewHolder> {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private List<PlaylistTrack> trackList;
    private InfoSlideListener infoSlideListener;


    public NonMasterPlaylistAdapter(InfoSlideListener infoSlideListener, String roomName){
        trackList = new ArrayList<>();
        this.infoSlideListener = infoSlideListener;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child(roomName);
    }

    @Override
    public NonMasterPlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View childView = inflater.inflate(R.layout.playlist_track_item_view, parent, false);
        return new NonMasterPlaylistViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(final NonMasterPlaylistViewHolder holder, int position) {
        holder.bind(trackList.get(position));
        final PlaylistTrack newtrack = trackList.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                rowClicked(newtrack);
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getAdapterPosition() >= 0)
                    infoSlideListener.slidePanelWithInfo(trackList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public void add(PlaylistTrack myTrack) {
        trackList.add(myTrack);
        notifyItemInserted(trackList.size() - 1);
    }

    public void removeTrackWithURI(String trackURI) {
        int albumposition = 0;
        for(int i=0; i < trackList.size(); i++){
            if(trackList.get(i).getTrackUri().equals(trackURI)){
                albumposition = i;
            }
        }
        if(trackList.size() == 0){
            throw new IllegalArgumentException("TrackList Size Cannot Be Zero");
        }
        trackList.remove(albumposition);
        notifyItemRemoved(albumposition);
    }


    public void rowClicked(PlaylistTrack track) {
        database = FirebaseDatabase.getInstance();

        Query removedMusicQuery = reference.orderByChild("trackUri").equalTo(track.getTrackUri());
        removedMusicQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
