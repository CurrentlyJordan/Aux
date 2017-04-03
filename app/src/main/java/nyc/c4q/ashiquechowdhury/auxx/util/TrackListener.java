package nyc.c4q.ashiquechowdhury.auxx.util;

/**
 * Created by jordansmith on 3/11/17.
 */

public interface TrackListener {

    void updateCurrentlyPlayingText(String trackName);

    void changeToPlayButton();

    void changeToPauseButton();

    void pauseSong();
}
