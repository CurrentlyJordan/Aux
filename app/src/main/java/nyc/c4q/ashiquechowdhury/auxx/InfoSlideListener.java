package nyc.c4q.ashiquechowdhury.auxx;

import nyc.c4q.ashiquechowdhury.auxx.model.PlaylistTrack;

/**
 * Created by jordansmith on 3/12/17.
 */

public interface InfoSlideListener {

    void slidePanelWithInfo(PlaylistTrack track);

    void slidePanelDownWithInfo(PlaylistTrack track);
}
