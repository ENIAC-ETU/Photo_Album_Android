package eniac.photo_album_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class PhotosAdapter extends
        RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView photoImageView;
        public Button uploadButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            photoImageView = (ImageView) itemView.findViewById(R.id.album_photo);
            uploadButton = (Button) itemView.findViewById(R.id.upload_photo);
        }
    }

    // Store a member variable for the contacts
    private List<PhotoFields> mPhotos;

    // Pass in the contact array into the constructor
    public PhotosAdapter(List<PhotoFields> photos) {
        mPhotos = photos;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public PhotosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View photoView = inflater.inflate(R.layout.item_photo, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(photoView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(PhotosAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        PhotoFields photo = mPhotos.get(position);

        ImageView imageView = viewHolder.photoImageView;
        imageView.setVisibility(View.VISIBLE);
        String path = photo.getPhoto().replace("/photos", "");
        path = path.replace("/app", "");
        path = path.replace("/albums", "");
        new DownloadImageTask(imageView).execute(path);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mPhotos.size();
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
