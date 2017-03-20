package nyc.c4q.ashiquechowdhury.auxx.nonmaster;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;

import es.dmoral.toasty.Toasty;
import nyc.c4q.ashiquechowdhury.auxx.ArtistSongSelectedListener;
import nyc.c4q.ashiquechowdhury.auxx.InfoSlideListener;
import nyc.c4q.ashiquechowdhury.auxx.R;
import nyc.c4q.ashiquechowdhury.auxx.joinandcreate.JoinRoomActivity;
import nyc.c4q.ashiquechowdhury.auxx.master.MasterSearchFragment;
import nyc.c4q.ashiquechowdhury.auxx.model.PlaylistTrack;
import nyc.c4q.ashiquechowdhury.auxx.util.ListenerHolder;
import nyc.c4q.ashiquechowdhury.auxx.util.SongListHelper;
import nyc.c4q.ashiquechowdhury.auxx.util.SpotifyUtil;

import static android.R.id.message;
import static nyc.c4q.ashiquechowdhury.auxx.joinandcreate.PlaylistActivity.ROOMNAMEKEY;

public class NonMasterPlaylistFragment extends Fragment implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback, Player.OperationCallback, ArtistSongSelectedListener {

    private String roomName = "musicList";
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ChildEventListener childListener;
    private RecyclerView recyclerView;
    private SpotifyUtil spotify;
    private static final String FRAGMENT_TAG = NonMasterPlaylistFragment.class.getSimpleName();
    private FloatingActionButton floatingSearchBtn;
    private NonMasterPlaylistAdapter myAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle argumentsBundle = getArguments();
        if(argumentsBundle != null) {
            roomName = argumentsBundle.getString(JoinRoomActivity.ROOMNAMEKEY);
        }

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child(roomName);
        childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!(dataSnapshot.getValue() instanceof String)) {
                    PlaylistTrack myTrack = dataSnapshot.getValue(PlaylistTrack.class);
                    myAdapter.add(myTrack);
                    String firebaseKey = dataSnapshot.getKey();
                    myTrack.setFirebaseKey(firebaseKey);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                PlaylistTrack myTrack = dataSnapshot.getValue(PlaylistTrack.class);
                if(myTrack.getVetos() >= 3) {
                    myAdapter.rowClicked(myTrack);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                PlaylistTrack myTrack = dataSnapshot.getValue(PlaylistTrack.class);
                myAdapter.removeTrackWithURI(myTrack.getTrackUri());
                InfoSlideListener info = (InfoSlideListener) getActivity();
                info.slidePanelDownWithInfo();
                SongListHelper.removeSongAfterVeto(myTrack);
                Toasty.error(getContext(), myTrack.getTrackName() + " was deleted", Toast.LENGTH_SHORT, true).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        myAdapter = new NonMasterPlaylistAdapter((InfoSlideListener) getActivity(), roomName);
        reference.addChildEventListener(childListener);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        spotify = SpotifyUtil.getInstance();
        spotify.createPlayer(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(myAdapter);

        TextView actionBar = (TextView) view.findViewById(R.id.toolbar_TV);
        actionBar.setText(roomName);

        ListenerHolder.setArtistSongSelectedListener(this);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        floatingSearchBtn = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment masterSearchFragment = new MasterSearchFragment();
                Bundle putArgumentsBundle = new Bundle();
                putArgumentsBundle.putString(ROOMNAMEKEY, roomName);
                masterSearchFragment.setArguments(putArgumentsBundle);
                fragmentTransaction
                        .replace(R.id.playlist_maincontent_frame, masterSearchFragment)
                        .addToBackStack(FRAGMENT_TAG)
                        .commit();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0 ||dy<0 && floatingSearchBtn.isShown())
                {
                    floatingSearchBtn.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    floatingSearchBtn.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onLoggedIn() {
        Log.d("SearchAndChooseActivity", "User logged in");

    }

    @Override
    public void onLoggedOut() {
        Log.d("SearchAndChooseActivity", "User logged out");

    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("SearchAndChooseActivity", "Login failed");

    }

    @Override
    public void onTemporaryError() {
        Log.d("SearchAndChooseActivity", "Temporary error occurred");

    }

    @Override
    public void onConnectionMessage(String s) {
        Log.d("SearchAndChooseActivity", "Received connection message: " + message);

    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {

    }


    @Override
    public void onPlaybackError(Error error) {
        Log.d("SearchAndChooseActivity", "Playback error received: " + error.name());
        switch (error) {
            default:
                break;
        }

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(Error error) {

    }

    @Override
    public void updatePlaylistUI() {

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        reference.removeEventListener(childListener);
    }
}