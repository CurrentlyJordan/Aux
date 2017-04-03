package nyc.c4q.ashiquechowdhury.auxx.model;

import java.io.Serializable;

/**
 * Created by jordansmith on 3/9/17.
 */

public class PlaylistTrack implements Serializable {

    private String trackUri;
    private String artistName;
    private String albumName;
    private String trackName;
    private String user;
    private String message;
    private String albumArt;
    private String artistId;
    private String artistPictureUrl;
    private int vetos;
    private int likes;
    private String firebaseKey;



    private PlaylistTrack(Builder builder) {
        this.albumName = builder.albumName;
        this.artistName = builder.artistName;
        this.message = builder.message;
        this.trackName = builder.trackName;
        this.user = builder.user;
        this.trackUri = builder.trackUri;
        this.albumArt = builder.albumArt;
        this.artistId = builder.artistId;
        this.artistPictureUrl = builder.artistPictureUrl;

    }

    public PlaylistTrack() {
    }

    public String getTrackUri() {
        return trackUri;
    }

    public void setTrackUri(String trackUri) {
        this.trackUri = trackUri;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistPictureUrl() {
        return artistPictureUrl;
    }

    public void setArtistPictureUrl(String artistPictureUrl) {
        this.artistPictureUrl = artistPictureUrl;
    }
    public void setFirebaseKey(String firebaseKey){
        this.firebaseKey = firebaseKey;
    }

    public String getFirebaseKey(){
        return firebaseKey;
    }


    public int getVetos() {
        return vetos;
    }

    public void setVetos(int vetos) {
        this.vetos = vetos;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PlaylistTrack)) {
            return false;
        }
        PlaylistTrack playlistTrack = (PlaylistTrack) obj;
        return playlistTrack.getTrackUri().equals(this.getTrackUri());
    }

    public static class Builder {

        private final String trackName;
        private String trackUri;
        private String artistName;
        private String albumName;
        private String user;
        private String message;
        private String albumArt;
        private String artistId;
        private String artistPictureUrl;


        public Builder(String trackName) {
            this.trackName = trackName;
        }

        public Builder trackUri(String trackUri) {
            this.trackUri = trackUri;
            return this;
        }

        public Builder artistName(String artistName) {
            this.artistName = artistName;
            return this;
        }

        public Builder albumName(String albumName) {
            this.albumName = albumName;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder albumArt(String albumArt) {
            this.albumArt = albumArt;
            return this;
        }

        public Builder artistId(String artistId) {
            this.artistId = artistId;
            return this;
        }

        public Builder artistPictureUrl(String artistPictureUrl){
            this.artistPictureUrl = artistPictureUrl;
            return this;
        }

        public PlaylistTrack build() {
            return new PlaylistTrack(this);
        }


    }
}
