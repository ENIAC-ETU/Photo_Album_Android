package eniac.photo_album_android;

import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlbumResponse  {
    private static final String API_BASE_URL = "https://photo-album-eniac.herokuapp.com/";
    private static List<AlbumFields> albums;


    protected void initiateAlbumApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AlbumREST service = retrofit.create(AlbumREST.class);
        Call<AlbumFields[]> call = service.getAlbums();
        call.enqueue(new Callback<AlbumFields[]>() {
                         @Override
                         public void onResponse(Call<AlbumFields[]> call, Response<AlbumFields[]> response) {
                             if(response.isSuccessful()) {
                                 albums = Arrays.asList(response.body());
                             } else {
                                 System.out.println(response.errorBody());
                             }
                         }

                         @Override
                         public void onFailure(Call<AlbumFields[]> call, Throwable t) {

                         }
                     }

        );
    }

}
