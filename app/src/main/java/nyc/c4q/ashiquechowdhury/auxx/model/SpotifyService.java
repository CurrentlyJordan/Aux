package nyc.c4q.ashiquechowdhury.auxx.model;

import nyc.c4q.ashiquechowdhury.auxx.model.artistModel.ArtistResponse;
import nyc.c4q.ashiquechowdhury.auxx.model.artistspecifics.ArtistInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpotifyService {

    @GET("/v1/search")
    Call<Example> getResults(@Header("Authorization") String accessToken, @Query("q") String searchInput, @Query("type") String type, @Query("market") String market);

    @GET("/v1/search")
    Call<Example> getOtherResults(@Query("q") String searchInput, @Query("type") String type);

    @GET("/v1/artists/{id}/top-tracks")
    Call<ArtistResponse> getArtistTopTracks(@Path("id") String artistID, @Query("country") String countryCode);

    @GET("/v1/artists/{id}")
    Call<ArtistInfo> getArtistInfo(@Path("id") String artistID);

}
