package eniac.photo_album_android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class AlbumsAdapter extends
        RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView, dateTextView;
        public Button albumButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.album_name);
            dateTextView = (TextView) itemView.findViewById(R.id.created_at);
            albumButton = (Button) itemView.findViewById(R.id.album_button);
        }
    }

    // Store a member variable for the contacts
    private List<AlbumFields> mAlbums;

    // Pass in the contact array into the constructor
    public AlbumsAdapter(List<AlbumFields> albums) {
        mAlbums = albums;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public AlbumsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View albumView = inflater.inflate(R.layout.item_album, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(albumView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(AlbumsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        AlbumFields album = mAlbums.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        Button button = viewHolder.albumButton;
        textView.setText(album.getTitle());
        textView = viewHolder.dateTextView;
        textView.setText(album.getCreated_at());
        button.setText("Albüme Git");
        button.setContentDescription(""+album.getId());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mAlbums.size();
    }
}
