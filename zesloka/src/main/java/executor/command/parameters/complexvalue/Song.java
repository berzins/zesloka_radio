package executor.command.parameters.complexvalue;


/**
 * Describes song data container for data exchange
 */
public class Song {
    Integer id;
    String tile;
    String artist;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + ", " +
                "Artist: " + getArtist() + ", " +
                "Title: " + getTile();
    }
}
