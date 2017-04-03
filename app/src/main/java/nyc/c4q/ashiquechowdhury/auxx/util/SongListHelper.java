package nyc.c4q.ashiquechowdhury.auxx.util;

import java.util.ArrayList;
import java.util.List;

import nyc.c4q.ashiquechowdhury.auxx.model.Item;
import nyc.c4q.ashiquechowdhury.auxx.model.PlaylistTrack;
import nyc.c4q.ashiquechowdhury.auxx.model.artistModel.Track;

public class SongListHelper {

    public static boolean isSongPlaying = false;
    public static boolean isPlaylistPlaying = false;
    public static List<PlaylistTrack> songList = new ArrayList<>();

    private static PlaylistTrack currentlyPlayingSong;

    public static String roomName;

    static SpotifyUtil spotify = SpotifyUtil.getInstance();

    public static List<PlaylistTrack> getSongList() {
        return songList;
    }

    public static void setSongList(List<PlaylistTrack> songList) {
        SongListHelper.songList = songList;
    }

    public static PlaylistTrack getCurrentlyPlayingSong() {
        return currentlyPlayingSong;
    }

    public static void setCurrentlyPlayingSong(PlaylistTrack currentlyPlayingSong) {
        SongListHelper.currentlyPlayingSong = currentlyPlayingSong;
    }

    public static void playNextTrack() {
        if (0 == SongListHelper.getSongList().size()) {
            spotify.getTracklistener().updateCurrentlyPlayingText("No Songs Currently Playing");
            spotify.getTracklistener().pauseSong();
            isPlaylistPlaying = false;
        } else {
            PlaylistTrack track = SongListHelper.getSongList().get(0);
            setCurrentlyPlayingSong(track);
            spotify.spotifyPlayer.playUri(null, track.getTrackUri(), 0, 0);
            spotify.getTracklistener().updateCurrentlyPlayingText(formatPlayerInfo(track));
        }
    }

    public static PlaylistTrack transformAndAdd(Item item) {
        PlaylistTrack track = new PlaylistTrack.Builder(item.getName())
                .trackUri(item.getUri())
                .albumName(item.getAlbum().getName())
                .artistName(item.getArtists().get(0).getName())
                .artistId(item.getArtists().get(0).getId())
                .build();

        if (item.getAlbum().getImages().isEmpty()) {
            track.setAlbumArt("https://www.tunefind.com/i/new/album-art-empty.png");
        } else {
            track.setAlbumArt(item.getAlbum().getImages().get(0).getUrl());
        }

        return track;
    }

    public static PlaylistTrack transformAndAdd(Track track) {
        PlaylistTrack playlistTrack = new PlaylistTrack.Builder(track.getName())
                .trackUri(track.getUri())
                .albumName(track.getAlbum().getName())
                .artistName(track.getArtists().get(0).getName())
                .artistId(track.getArtists().get(0).getId())
                .build();

        if (track.getAlbum().getImages().isEmpty()) {
            playlistTrack.setAlbumArt("https://www.tunefind.com/i/new/album-art-empty.png");
        } else {
            playlistTrack.setAlbumArt(track.getAlbum().getImages().get(0).getUrl());
        }
        return playlistTrack;
    }

    public static void removeSongAfterVeto(PlaylistTrack track) {
        if (currentlyPlayingSong != null && SongListHelper.currentlyPlayingSong.equals(track)) {
            SongListHelper.getSongList().remove(track);
            playNextTrack();
        } else {
            SongListHelper.getSongList().remove(track);
        }
    }

    public static String formatPlayerInfo(PlaylistTrack track) {
        StringBuilder sb = new StringBuilder();
        sb.append(track.getArtistName());
        sb.append(" ");
        sb.append("-");
        sb.append(" ");
        sb.append(track.getTrackName());
        return sb.toString();
    }
}