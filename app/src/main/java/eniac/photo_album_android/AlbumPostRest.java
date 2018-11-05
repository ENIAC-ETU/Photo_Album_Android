package eniac.photo_album_android;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface AlbumPostRest {
    @POST("/albums")
    Call<AlbumFields> createAlbum(@Body AlbumFields album);

}