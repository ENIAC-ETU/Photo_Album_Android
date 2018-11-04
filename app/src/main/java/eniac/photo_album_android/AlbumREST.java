package eniac.photo_album_android;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AlbumREST {
        @GET("/albums")
        Call<AlbumFields[]> getAlbums();
}
