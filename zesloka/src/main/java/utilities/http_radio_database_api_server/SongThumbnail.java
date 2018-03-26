package utilities.http_radio_database_api_server;

public class SongThumbnail {

    private Song song;
    private String img;

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
