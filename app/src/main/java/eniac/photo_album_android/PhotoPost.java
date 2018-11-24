package eniac.photo_album_android;

import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PhotoPost {
    private static final String API_BASE_URL = "https://photo-album-eniac.herokuapp.com/";

    protected void uploadPhoto(final MainActivity ma, Integer albumId, Uri filePath, String username, String password) {
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(new BasicAuthInterceptor(username, password))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PhotoPostRest service = retrofit.create(PhotoPostRest.class);
        File sdcard = Environment.getExternalStorageDirectory();
        InputStream is;

        try {
            is = ma.getContentResolver().openInputStream(filePath);
            System.out.println(is);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            File targetFile = new File(sdcard,"temp.jpg");
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), targetFile);
            MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", targetFile.getName(), reqFile);
            RequestBody album = RequestBody.create(MediaType.parse("multipart/form-data"), ""+albumId);

            Call<ResponseBody> call = service.uploadPhoto(photo, album);

            call.enqueue(new Callback<ResponseBody>() {
                             @Override
                             public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                 if(response.isSuccessful()) {
                                     System.out.println("Success");
                                     ma.backToPhotos();
                                 } else {
                                     System.out.println("Fail");
                                     System.out.println(response.errorBody());
                                 }
                             }

                             @Override
                             public void onFailure(Call<ResponseBody> call, Throwable t) {
                                 ma.backToPhotos();
                             }
                         }

            );
        }
        catch (Exception e) {
            //System.out.println(e);
            e.printStackTrace();
        }

    }
}
