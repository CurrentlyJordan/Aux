package nyc.c4q.ashiquechowdhury.auxx.util;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.ashiquechowdhury.auxx.model.Item;
import nyc.c4q.ashiquechowdhury.auxx.model.PlaylistTrack;
import nyc.c4q.ashiquechowdhury.auxx.model.artistModel.Track;

/**
 * Created by shawnspeaks on 3/7/17.
 */

public class SongListHelper {
    public static int trackCounter = 0;

    private static List<PlaylistTrack> songList = new ArrayList<>();

    private static PlaylistTrack currentlyPlayingSong;

    static SpotifyUtil spotify = SpotifyUtil.getInstance();

    public static List<PlaylistTrack> getSongList() {
        return songList;
    }

    public static PlaylistTrack getCurrentlyPlayingSong() {
        return currentlyPlayingSong;
    }

    public static void setCurrentlyPlayingSong(PlaylistTrack currentlyPlayingSong) {
        SongListHelper.currentlyPlayingSong = currentlyPlayingSong;
    }


    public static void playNextTrack(){
        if (trackCounter + 1 >= SongListHelper.getSongList().size()) {
        } else {
            trackCounter++;
            PlaylistTrack track = SongListHelper.getSongList().get(trackCounter);
            setCurrentlyPlayingSong(track);
            spotify.spotifyPlayer.playUri(null, track.getTrackUri(), 0, 0);
            spotify.getTracklistener().updateCurrentlyPlayingText(formatPlayerInfo(track));
        }
    }

    public static void playPreviousTrack(Context context) {
        PlaylistTrack track;
        if (trackCounter - 1 < 0) {
            Toast.makeText(context, "Start of playlist", Toast.LENGTH_SHORT).show();
            track = SongListHelper.getSongList().get(trackCounter);
            setCurrentlyPlayingSong(track);
            spotify.spotifyPlayer.playUri(null, track.getTrackUri(), 0, 0);
            spotify.getTracklistener().updateCurrentlyPlayingText(formatPlayerInfo(track));
        } else {
            trackCounter--;
            track = SongListHelper.getSongList().get(trackCounter);
            setCurrentlyPlayingSong(track);
            spotify.spotifyPlayer.playUri(null, track.getTrackUri(), 0, 0);
            spotify.getTracklistener().updateCurrentlyPlayingText(formatPlayerInfo(track));
        }
    }

    public static void transformAndAdd (Item item){

        PlaylistTrack track = new PlaylistTrack.Builder(item.getName())
                .trackUri(item.getUri())
                .albumName(item.getAlbum().getName())
                .artistName(item.getArtists().get(0).getName())
                .artistId(item.getArtists().get(0).getId())
                .build();

        if(item.getAlbum().getImages().isEmpty()){
            track.setAlbumArt("https://www.tunefind.com/i/new/album-art-empty.png");
        }
        else{
            track.setAlbumArt(item.getAlbum().getImages().get(0).getUrl());
        }
        SongListHelper.getSongList().add(track);

    }

    public static void transformAndAdd(Track track){
        PlaylistTrack playlistTrack = new PlaylistTrack.Builder(track.getName())
                .trackUri(track.getUri())
                .albumName(track.getAlbum().getName())
                .artistName(track.getArtists().get(0).getName())
                .artistId(track.getArtists().get(0).getId())
                .build();

        if(track.getAlbum().getImages().isEmpty()){
            playlistTrack.setAlbumArt("https://www.tunefind.com/i/new/album-art-empty.png");
        }
        else{
            playlistTrack.setAlbumArt(track.getAlbum().getImages().get(0).getUrl());
        }
        SongListHelper.getSongList().add(playlistTrack);


    }

    public static void  removeSongAfterVeto(PlaylistTrack track){
        if(SongListHelper.currentlyPlayingSong.equals(track)){
            playNextTrack();
            SongListHelper.getSongList().remove(track);
        }
    }

    public static String formatPlayerInfo(PlaylistTrack track){
        StringBuilder sb = new StringBuilder();
        sb.append(track.getArtistName());
        sb.append(" ");
        sb.append("-");
        sb.append(" ");
        sb.append(track.getTrackName());
        return sb.toString();
    }

}


