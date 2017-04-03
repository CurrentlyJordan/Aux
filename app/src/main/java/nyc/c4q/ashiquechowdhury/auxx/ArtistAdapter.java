package nyc.c4q.ashiquechowdhury.auxx;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import nyc.c4q.ashiquechowdhury.auxx.model.artistModel.Track;

/**
 * Created by jordansmith on 3/14/17.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private SongTrackClickListener trackClickListener;
    private List <Track> tracksList;
//    private ArtistSongSelectedListener listener;

    public ArtistAdapter(List<Track> tracksList, SongTrackClickListener currentSongInfoFragment){
        this.tracksList = tracksList;
//        listener = ListenerHolder.getArtistSongSelectedListener();
        trackClickListener = currentSongInfoFragment;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View childView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_track_item, parent, false);
        int height = parent.getMeasuredHeight();
        int width = parent.getMeasuredWidth() / 2;
        childView.setLayoutParams(new RecyclerView.LayoutParams(width, height));


        return new ArtistViewHolder(childView);

    }

    @Override
    public void onBindViewHolder(final ArtistViewHolder holder, int position) {
        holder.bind(tracksList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listener.updatePlaylistUI();
                trackClickListener.songClicked(tracksList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return tracksList.size();
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder{
        TextView artistName;
        TextView songName;
        ImageView albumArt;


        public ArtistViewHolder(View itemView) {
            super(itemView);
            artistName = (TextView) itemView.findViewById(R.id.artist_name_text_view);
            songName = (TextView) itemView.findViewById(R.id.song_name_text_view);
            albumArt = (ImageView) itemView.findViewById(R.id.album_art_text_view);

        }

        void bind(Track track){
            artistName.setText(track.getArtists().get(0).getName());
            songName.setText(track.getName());
            if(!track.getAlbum().getImages().isEmpty()) {

                Glide.with(itemView.getContext()).load(track.getAlbum().getImages().get(0).getUrl()).fitCenter().into(albumArt);
            }
            else{
                Glide.with(itemView.getContext()).load("https://www.tunefind.com/i/new/album-art-empty.png").fitCenter().into(albumArt);
            }
        }
    }

}