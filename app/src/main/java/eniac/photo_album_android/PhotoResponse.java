package eniac.photo_album_android;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhotoResponse {
    private static final String API_BASE_URL = "https://photo-album-eniac.herokuapp.com/";
    private static List<PhotoFields> Photos;
    private void initiatePhotoApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PhotoREST service = retrofit.create(PhotoREST.class);
        Call<PhotoFields[]> call = service.getPhotos();
        call.enqueue(new Callback<PhotoFields[]>() {
                         @Override
                         public void onResponse(Call<PhotoFields[]> call, Response<PhotoFields[]> response) {
                             if(response.isSuccessful()) {
                                 Photos = Arrays.asList(response.body());
                             } else {
                                 System.out.println(response.errorBody());
                             }
                         }

                         @Override
                         public void onFailure(Call<PhotoFields[]> call, Throwable t) {

                         }
                     }

        );
    }
}
