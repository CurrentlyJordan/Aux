package nyc.c4q.ashiquechowdhury.auxx.model.artistspecifics;

import java.util.ArrayList;

/**
 * Created by shawnspeaks on 3/16/17.
 */

public class ArtistInfo {

    private String name;
    private ArrayList<Images> images;

    public void setName(String name) {
        this.name = name;
    }

    public void setImages(ArrayList<Images> images) {
        this.images = images;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Images> getImages() {
        return this.images;
    }
}
