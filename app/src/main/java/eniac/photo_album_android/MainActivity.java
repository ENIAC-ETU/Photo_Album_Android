package eniac.photo_album_android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity{
    AlbumResponse albumMain;
    PhotoResponse photoMain;
    AlbumPost postAlbum;
    PhotoPost postPhoto;
    RecyclerView rvAlbums, rvPhotos;
    private int PICK_IMAGE_REQUEST = 1;
    Integer currentAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAlbums();
    }

    public void getAlbums() {
        rvAlbums = (RecyclerView) findViewById(R.id.rvAlbums);
        albumMain = new AlbumResponse();
        albumMain.initiateAlbumApi(rvAlbums);
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
        EditText mEditOwmer = (EditText) findViewById(R.id.edit_owner);
        setContentView(R.layout.activity_main);
        postAlbum = new AlbumPost();
        postAlbum.createAlbum(this, mEditAlbumName.getText().toString(), mEditOwmer.getText().toString());
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
        photoMain.initiatePhotoApi(currentAlbum, rvPhotos);
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
            postPhoto.uploadPhoto(this, currentAlbum, uri);
        }
    }
}
