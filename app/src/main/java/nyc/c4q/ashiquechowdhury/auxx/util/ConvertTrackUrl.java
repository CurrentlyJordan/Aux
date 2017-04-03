package nyc.c4q.ashiquechowdhury.auxx.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shawnspeaks on 3/19/17.
 */

public class ConvertTrackUrl {

    String albumUrl;
    Bitmap urlToBitmap;
    Palette palette;

    public ConvertTrackUrl(String albumUrl) {
        this.albumUrl = albumUrl;
    }


    private void setBitMapFromURL (final String albumUrl){
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(albumUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);

                }catch (IOException e){
                  Log.e("ERROR", "TRAGIC STUFF HAPPENED");
                }

            }
        };
        thread.run();
    }

}