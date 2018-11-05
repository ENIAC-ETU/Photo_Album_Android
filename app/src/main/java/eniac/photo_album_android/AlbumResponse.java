package eniac.photo_album_android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.support.v7.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlbumResponse  {
    private static final String API_BASE_URL = "https://photo-album-eniac.herokuapp.com/";
    private static List<AlbumFields> albums = new ArrayList<AlbumFields>();


    protected void initiateAlbumApi(final RecyclerView rView) {

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
                                 System.out.println("Success");
                                 albums = Arrays.asList(response.body());
                                 AlbumsAdapter adapter = new AlbumsAdapter(albums);
                                 rView.setAdapter(adapter);
                             } else {
                                 System.out.println("Fail");
                                 System.out.println(response.errorBody());
                             }
                         }

                         @Override
                         public void onFailure(Call<AlbumFields[]> call, Throwable t) {
                             t.printStackTrace();
                         }
                     }

        );
    }
}