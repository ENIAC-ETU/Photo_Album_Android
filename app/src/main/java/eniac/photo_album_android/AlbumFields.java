package eniac.photo_album_android;

public class AlbumFields {
    Integer id;
    String title;
    String created_at;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public String getCreated_at() {
        return created_at;
    }

    public AlbumFields(String title) {
        this.title = title;
    }
}
