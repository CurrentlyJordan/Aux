package nyc.c4q.ashiquechowdhury.auxx.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import static android.R.id.message;
import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;
import static nyc.c4q.ashiquechowdhury.auxx.util.SongListHelper.roomName;

/**
 * Created by jordansmith on 3/7/17.
 */

public class SpotifyUtil implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback, Player.OperationCallback {

    private static final String CLIENT_ID = "a47e94f21a9649c982f39e72920c1754";
    private static final String REDIRECT_URI = "aux://callback";
    private static SpotifyUtil instance;
    public Player spotifyPlayer;
    private AuthenticationResponse response;
    private TrackListener tracklistener;


    public static SpotifyUtil getInstance() {
        if (instance == null) {
            instance = new SpotifyUtil();

        }
        return instance;
    }

    public void authorizeSpotifyInFragment(Fragment fragment) {
        if (response == null || response.getExpiresIn() <= 0) {
            AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                    AuthenticationResponse.Type.TOKEN,
                    REDIRECT_URI);
            builder.setScopes(new String[]{"user-read-private", "streaming"});
            AuthenticationRequest request = builder.build();
            Intent intent = AuthenticationClient.createLoginActivityIntent(fragment.getActivity(), request);
            fragment.startActivityForResult(intent, REQUEST_CODE);
        }
    }

    public void authorizeSpotifyInActivity(AppCompatActivity activity) {
        if (response == null || response.getExpiresIn() <= 0) {
            AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                    AuthenticationResponse.Type.TOKEN,
                    REDIRECT_URI);
            builder.setScopes(new String[]{"user-read-private", "streaming"});
            AuthenticationRequest request = builder.build();
            AuthenticationClient.openLoginActivity(activity, REQUEST_CODE, request);
        }
    }

    //* CALL THIS IN OnActivityResult AFTER AUTHORIZING SPOTIFY

    public void setResponse(AuthenticationResponse response) {
        this.response = response;
    }

    public AuthenticationResponse getResponse() {
        return response;
    }

    public void createPlayer(Context activityContext) {
        Config playerConfig = new Config(activityContext, response.getAccessToken(), CLIENT_ID);
        Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(SpotifyPlayer spotifyPlayer) {
                instance.spotifyPlayer = spotifyPlayer;
                instance.spotifyPlayer.addConnectionStateCallback(SpotifyUtil.this);
                instance.spotifyPlayer.addNotificationCallback(SpotifyUtil.this);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("SearchAndChooseActivity", "Could not initialize player: " + throwable.getMessage());
            }
        });
    }


    @Override
    public void onLoggedIn() {
        Log.d(getClass().getName(), "User logged in");

    }

    @Override
    public void onLoggedOut() {
        Log.d(getClass().getName(), "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d(getClass().getName(), "Login failed");

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d(getClass().getName(), "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            case kSpPlaybackNotifyAudioDeliveryDone:

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference().child(roomName);

                Query removedMusicQuery = reference.orderByChild("trackUri").equalTo(SongListHelper.getCurrentlyPlayingSong().getTrackUri());
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

            case kSpPlaybackNotifyPlay:
                tracklistener.changeToPauseButton();
                break;
            case kSpPlaybackNotifyPause:
                tracklistener.changeToPlayButton();
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d(getClass().getName(), "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }

    }

    @Override
    public void onSuccess() {
        Log.d(getClass().getName(), "Received connection message: " + message);
    }

    @Override
    public void onError(Error error) {

    }

    public void setTracklistener(TrackListener tracklistener) {
        this.tracklistener = tracklistener;
    }

    public TrackListener getTracklistener() {
        return tracklistener;
    }
}
