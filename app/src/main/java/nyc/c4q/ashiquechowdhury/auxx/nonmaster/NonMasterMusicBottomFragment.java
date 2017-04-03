package nyc.c4q.ashiquechowdhury.auxx.nonmaster;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import nyc.c4q.ashiquechowdhury.auxx.R;
import nyc.c4q.ashiquechowdhury.auxx.util.SongListHelper;
import nyc.c4q.ashiquechowdhury.auxx.util.SpotifyUtil;
import nyc.c4q.ashiquechowdhury.auxx.util.TrackListener;

public class NonMasterMusicBottomFragment extends Fragment implements View.OnClickListener, TrackListener {
    private FrameLayout upVoteButton;
    private FrameLayout playButton;
    private FrameLayout downVoteButton;
    private TextView currentTrackInfoTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master_musicplayer, container, false);

        upVoteButton = (FrameLayout) view.findViewById(R.id.upvotebutton);
        playButton = (FrameLayout) view.findViewById(R.id.playbutton);
        playButton.setVisibility(View.GONE);
        downVoteButton = (FrameLayout) view.findViewById(R.id.downvotebutton);
        currentTrackInfoTextView = (TextView) view.findViewById(R.id.current_playing_song_textview);
        currentTrackInfoTextView.setSelected(true);
        currentTrackInfoTextView.setSingleLine(true);
        SpotifyUtil.getInstance().setTracklistener(this);

        if(SongListHelper.getCurrentlyPlayingSong() != null){
            currentTrackInfoTextView.setText(SongListHelper.formatPlayerInfo(SongListHelper.getCurrentlyPlayingSong()));
        }
        else{
            currentTrackInfoTextView.setText("Click to Like or Veto Songs");
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        upVoteButton.setOnClickListener(this);
        downVoteButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upvotebutton:
                Toasty.success(getContext(), "You liked this song.", Toast.LENGTH_SHORT, true).show();
                break;

            case R.id.downvotebutton:
                SongListHelper.removeSongAfterVeto(SongListHelper.getCurrentlyPlayingSong());
                Toasty.error(getContext(), "You vetoed this song.", Toast.LENGTH_SHORT, true).show();
                break;
        }
    }

    @Override
    public void updateCurrentlyPlayingText(String trackName) {
        currentTrackInfoTextView.setText(trackName);
    }

    @Override
    public void changeToPlayButton() {

    }

    @Override
    public void changeToPauseButton() {

    }

    @Override
    public void pauseSong() {
    }
}
