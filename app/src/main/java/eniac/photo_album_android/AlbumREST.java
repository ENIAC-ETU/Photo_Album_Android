package eniac.photo_album_android;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AlbumREST {
        @GET("/albums")
        Call<AlbumFields[]> getAlbums();
}
