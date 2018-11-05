package eniac.photo_album_android;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PhotoPostRest {
    @Multipart
    @POST("/photos")
    Call<ResponseBody> uploadPhoto(@Part MultipartBody.Part photo, @Part("album") RequestBody album);
}
