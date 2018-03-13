package utilities.http_radio_database_api_server;

public class Song {

    private int id;
    private String artist;
    private String title;
    private String category;

    public Song() {
        id = -1;
        artist = "";
        title = "";
        category = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
