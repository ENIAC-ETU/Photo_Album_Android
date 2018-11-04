package eniac.photo_album_android;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PhotoREST{
    @GET("/photos")
    Call<PhotoFields[]> getPhotos();
}
