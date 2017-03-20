package nyc.c4q.ashiquechowdhury.auxx.joinandcreate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import nyc.c4q.ashiquechowdhury.auxx.InfoSlideListener;
import nyc.c4q.ashiquechowdhury.auxx.R;
import nyc.c4q.ashiquechowdhury.auxx.chooseroomandlogin.ChooseRoomFragment;
import nyc.c4q.ashiquechowdhury.auxx.master.MasterMusicBottomFragment;
import nyc.c4q.ashiquechowdhury.auxx.master.MasterPlaylistFragment;
import nyc.c4q.ashiquechowdhury.auxx.model.Item;
import nyc.c4q.ashiquechowdhury.auxx.model.Listener;
import nyc.c4q.ashiquechowdhury.auxx.model.PlaylistTrack;
import nyc.c4q.ashiquechowdhury.auxx.util.SpotifyUtil;

public class PlaylistActivity extends AppCompatActivity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback, Listener, Player.OperationCallback, InfoSlideListener {

    //Todo: Write case to display placeholder view when song isn't playing/currently playing song == null and someone slides up on view
    //Todo: Set currently playing song = null when playlist finishes

    public static final String ROOMNAMEKEY = "nyc.c4q.PlaylistActivity.ROOMNAME";
    private SlidingUpPanelLayout slidingPanel;
    private final String CHOSEN_TRACK_KEY = "chosen track";
    public static boolean isSongClicked = false;
    private String roomName = "musicList";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_container);

        Intent intent = getIntent();
        roomName = intent.getStringExtra(ChooseRoomFragment.ROOMNAMEKEY);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment masterPlaylistFragment = new MasterPlaylistFragment();

        Bundle setArgumentsBundle = new Bundle();
        setArgumentsBundle.putString(ROOMNAMEKEY, roomName);
        masterPlaylistFragment.setArguments(setArgumentsBundle);
        fragmentTransaction
                .replace(R.id.playlist_maincontent_frame, masterPlaylistFragment)
                .replace(R.id.playlist_panelcontent_frame, new MasterMusicBottomFragment())
                .commit();

        slidingPanel = (SlidingUpPanelLayout) findViewById(R.id.activity_searchandchoose_container);

        slidingPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.playlist_panelcontent_frame, new MasterMusicBottomFragment()).commit();
                    isSongClicked = false;
                }
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED && !isSongClicked) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.playlist_panelcontent_frame, new CurrentSongInfoFragment()).commit();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        setBottomPanelHeight();
    }

    private void setBottomPanelHeight() {
        final LinearLayout layout = (LinearLayout) findViewById(R.id.bottom_panel_height);
        final ViewTreeObserver observer = layout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        slidingPanel.setPanelHeight(layout.getHeight());
                    }
                });
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(Error error) {

    }

    @Override
    public void onLoggedIn() {

    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(Error error) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {

    }

    @Override
    public void onPlaybackError(Error error) {

    }

    @Override
    public void playSelectedTrack(String uri) {

    }

    @Override
    public void queueSelectedTrack(Item item) {

    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(SpotifyUtil.getInstance().spotifyPlayer);
        super.onDestroy();
    }

    @Override
    public void slidePanelWithInfo(PlaylistTrack track) {
        isSongClicked = true;
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHOSEN_TRACK_KEY, track);
        bundle.putString(JoinRoomActivity.ROOMNAMEKEY, roomName);
        CurrentSongInfoFragment currentSongInfoFragment = new CurrentSongInfoFragment();
        currentSongInfoFragment.setArguments(bundle);
        slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        getSupportFragmentManager().beginTransaction().replace(R.id.playlist_panelcontent_frame, currentSongInfoFragment).commit();
    }

    @Override
    public void slidePanelDownWithInfo(){
        slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    public void onBackPressed() {
        if (slidingPanel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
}