package eniac.photo_album_android;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlbumPost {
    private static final String API_BASE_URL = "https://photo-album-eniac.herokuapp.com/";

    protected void createAlbum(final MainActivity ma, String albumName, String username, String password) {
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(new BasicAuthInterceptor(username, password))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AlbumPostRest service = retrofit.create(AlbumPostRest.class);
        Call<AlbumFields> call = service.createAlbum(new AlbumFields(albumName));

        call.enqueue(new Callback<AlbumFields>() {
                         @Override
                         public void onResponse(Call<AlbumFields> call, Response<AlbumFields> response) {
                             if(response.isSuccessful()) {
                                 System.out.println("Success");
                                 ma.getAlbums();
                             } else {
                                 System.out.println("Fail");
                                 System.out.println(response.errorBody());
                             }
                         }

                         @Override
                         public void onFailure(Call<AlbumFields> call, Throwable t) {
                             ma.getAlbums();
                         }
                     }

        );
    }
}
