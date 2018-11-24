package eniac.photo_album_android;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhotoResponse {
    private static final String API_BASE_URL = "https://photo-album-eniac.herokuapp.com/";
    private static List<PhotoFields> photos = new ArrayList<PhotoFields>();

    protected void initiatePhotoApi(Integer albumId, final RecyclerView rView, String username, String password) {
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(new BasicAuthInterceptor(username, password))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PhotoREST service = retrofit.create(PhotoREST.class);
        Call<PhotoFields[]> call = service.getPhotos(albumId);

        call.enqueue(new Callback<PhotoFields[]>() {
                         @Override
                         public void onResponse(Call<PhotoFields[]> call, Response<PhotoFields[]> response) {
                             if(response.isSuccessful()) {
                                 System.out.println("Success");
                                 photos = Arrays.asList(response.body());
                                 PhotosAdapter adapter = new PhotosAdapter(photos);
                                 rView.setAdapter(adapter);
                             } else {
                                 System.out.println(response.errorBody());
                             }
                         }

                         @Override
                         public void onFailure(Call<PhotoFields[]> call, Throwable t) {
                             t.printStackTrace();
                         }
                     }

        );
    }
}
