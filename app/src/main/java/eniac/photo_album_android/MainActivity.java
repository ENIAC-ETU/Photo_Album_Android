package eniac.photo_album_android;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;


public class MainActivity extends AppCompatActivity{
    AlbumResponse albumMain;
    PhotoResponse photoMain;
    AlbumPost postAlbum;
    PhotoPost postPhoto;
    RecyclerView rvAlbums, rvPhotos;
    private int PICK_IMAGE_REQUEST = 1;
    Integer currentAlbum;
    String username = "test", password = "adminadmin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        if (data != null) {
            setContentView(R.layout.shared_photo);
            ImageView imageView = findViewById(R.id.album_photo);
            imageView.setVisibility(View.VISIBLE);
            String parseUrl = data.toString().split("file_name=")[1];
            String fileName = parseUrl.split("&type=")[0];
            String fileType = parseUrl.split("&type=")[1];
            String downloadUrl = data.toString().split("/shared")[0] + "/static/images/" + fileName + "." + fileType;
            new DownloadImageTask(imageView).execute(downloadUrl);
        }
        else {
            getAlbums();
        }
    }

    public void getAlbums() {
        rvAlbums = (RecyclerView) findViewById(R.id.rvAlbums);
        albumMain = new AlbumResponse();
        albumMain.initiateAlbumApi(rvAlbums, username, password);
        rvAlbums.setLayoutManager(new LinearLayoutManager(this));
    }

    public void backToAlbums(View view) {
        setContentView(R.layout.activity_main);
        getAlbums();
    }

    public void createAlbum(View view) {
        setContentView(R.layout.create_album);
    }

    public void postAlbum(View view) {
        EditText mEditAlbumName = (EditText) findViewById(R.id.edit_album_name);
        setContentView(R.layout.activity_main);
        postAlbum = new AlbumPost();
        postAlbum.createAlbum(this, mEditAlbumName.getText().toString(), username, password);
    }

    public void getPhotos(View view) {
        Button albumButton = (Button) view;
        Integer albumId = Integer.parseInt(albumButton.getContentDescription().toString());
        setContentView(R.layout.photos);

        currentAlbum = albumId;
        backToPhotos();
    }

    public void backToPhotos() {
        rvPhotos = (RecyclerView) findViewById(R.id.rvPhotos);
        photoMain = new PhotoResponse();
        photoMain.initiatePhotoApi(currentAlbum, rvPhotos, username, password);
        rvPhotos.setLayoutManager(new LinearLayoutManager(this));
    }

    public void postPhoto(View view) {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Granted","Permission is granted");
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            postPhoto = new PhotoPost();
            postPhoto.uploadPhoto(this, currentAlbum, uri, username, password);
        }
    }

    public void sharePhoto(View view) {
        Button shareButton = (Button) view;
        String path = shareButton.getContentDescription().toString();
        System.out.println(path);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("path", path);
        clipboard.setPrimaryClip(clip);

        CharSequence text = "Fotoğraf linki kopyalandı!";
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(getApplicationContext(), text, duration).show();


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
