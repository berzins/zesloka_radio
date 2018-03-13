package utilities.youtube;

import java.util.List;

public class SongList {

        private String etag;
        private List<Song> items;
        private String kind;
        String nextPageToken;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    PageInfo pageInfo;


    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public List<Song> getItems() {
        return items;
    }

    public void setItems(List<Song> items) {
        this.items = items;
    }

    public class PageInfo {
        public int getResultsPerPpage() {
            return resultsPerPpage;
        }

        public void setResultsPerPpage(int resultsPerPpage) {
            this.resultsPerPpage = resultsPerPpage;
        }

        public int getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(int totalResults) {
            this.totalResults = totalResults;
        }

        int resultsPerPpage;
        int totalResults;
    }

    public class Song {
        ContentDetails contentDetails;
        private String etag;
        private String id;
        private String kind;
        Snippet snippet;

        public ContentDetails getContentDetails() {
            return contentDetails;
        }

        public void setContentDetails(ContentDetails contentDetails) {
            this.contentDetails = contentDetails;
        }

        public String getEtag() {
            return etag;
        }

        public void setEtag(String etag) {
            this.etag = etag;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public Snippet getSnippet() {
            return snippet;
        }

        public void setSnippet(Snippet snippet) {
            this.snippet = snippet;
        }
    }

    public class ContentDetails {
        private String videoId;
        private String videoPublishedAt;

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getVideoPublishedAt() {
            return videoPublishedAt;
        }

        public void setVideoPublishedAt(String videoPublishedAt) {
            this.videoPublishedAt = videoPublishedAt;
        }
    }

    public class Snippet {
        String channelId;
        String channelTitle;
        String desciption;
        String playlistId;
        String position;
        String publishedAt;
        ResourceId resourceId;
        Thumbnails thumbnails;
        String title;

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getChannelTitle() {
            return channelTitle;
        }

        public void setChannelTitle(String channelTitle) {
            this.channelTitle = channelTitle;
        }

        public String getDesciption() {
            return desciption;
        }

        public void setDesciption(String desciption) {
            this.desciption = desciption;
        }

        public String getPlaylistId() {
            return playlistId;
        }

        public void setPlaylistId(String playlistId) {
            this.playlistId = playlistId;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public ResourceId getResourceId() {
            return resourceId;
        }

        public void setResourceId(ResourceId resourceId) {
            this.resourceId = resourceId;
        }

        public Thumbnails getThumbnails() {
            return thumbnails;
        }

        public void setThumbnails(Thumbnails thumbnails) {
            this.thumbnails = thumbnails;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    private class ResourceId {
        String kind;
        String videoId;
    }

    private class Thumbnails {
        WTF def;
        WTF high;
        WTF maxres;
        WTF medium;
        WTF standard;

        public WTF getDef() {
            return def;
        }

        public void setDef(WTF def) {
            this.def = def;
        }

        public WTF getHigh() {
            return high;
        }

        public void setHigh(WTF high) {
            this.high = high;
        }

        public WTF getMaxres() {
            return maxres;
        }

        public void setMaxres(WTF maxres) {
            this.maxres = maxres;
        }

        public WTF getMedium() {
            return medium;
        }

        public void setMedium(WTF medium) {
            this.medium = medium;
        }

        public WTF getStandard() {
            return standard;
        }

        public void setStandard(WTF standard) {
            this.standard = standard;
        }
    }

    private class WTF {
        String height;
        String url;
        String width;

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }
    }
}
