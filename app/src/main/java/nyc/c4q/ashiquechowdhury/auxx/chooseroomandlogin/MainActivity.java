package nyc.c4q.ashiquechowdhury.auxx.chooseroomandlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;

import nyc.c4q.ashiquechowdhury.auxx.R;
import nyc.c4q.ashiquechowdhury.auxx.util.AlterToolBar;
import nyc.c4q.ashiquechowdhury.auxx.util.SpotifyUtil;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class MainActivity extends AppCompatActivity implements ChooseRoomFragment.ToolBarListener, SpotifyPlayer.NotificationCallback, ConnectionStateCallback, Player.OperationCallback {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpotifyUtil.getInstance().authorizeSpotifyInActivity(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new LoginFragment()).commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            SpotifyUtil.getInstance().setResponse(response);
        }
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
    public void onSuccess() {

    }

    @Override
    public void onError(Error error) {

    }

    @Override
    public void changeToolBarName(String name) {
        TextView tv = (TextView) findViewById(R.id.toolbar_TV);
        AlterToolBar alter = new AlterToolBar(name, tv);
        alter.changeToolBarName();
    }
}