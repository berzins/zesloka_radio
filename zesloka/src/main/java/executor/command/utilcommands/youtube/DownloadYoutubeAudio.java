package executor.command.utilcommands.youtube;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.*;
import executor.command.Command;
import executor.command.parameters.Parameter;
import utilities.GlobalSettings;
import utilities.JSONUtils;
import utilities.Util;
import utilities.youtube.SongList;

import java.io.*;
import java.util.*;

public class DownloadYoutubeAudio extends Command {

    public static final String PARAM_PLAYLIST_ID = "playlist_id";

    static private String filePath = "";
    static private String scriptPath = "";
    static private String clientSecretPath = "";
    private GlobalSettings.Settings settings;


    // ----------------------------------------------------

    private static final String APPLICATION_NAME = "Ze Sloka";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/youtube");

    private static FileDataStoreFactory DATA_STORE_FACTORY;

    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(YouTubeScopes.YOUTUBE_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Create an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                Util.getResourceAsInputStream(clientSecretPath);
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    /**
     * Build and return an authorized API client service, such as a YouTube
     * Data API client service.
     * @return an authorized API client service
     * @throws IOException
     */
    public static YouTube getYouTubeService() throws IOException {
        Credential credential = authorize();
        return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    ////////////////////////////////////////////////////////


    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public DownloadYoutubeAudio(String name, String key) {
        super(name, key);
    }

    @Override
    protected void init() {
        super.init();
        initParamKeys(new Parameter[] {
                new Parameter(this.getKey(), PARAM_PLAYLIST_ID, Parameter.TYPE_STRING, Parameter.VALUE_UNDEFINED)
        });
        this.settings = GlobalSettings.getInstance().getSettings();
        filePath = settings.getValue("yt_download_file_path");
        scriptPath = settings.getValue("yt_download_script_path");
        clientSecretPath = settings.getValue("yt_client_secret_path");

    }

    @Override
    public void execute() {
        super.execute();

        // -----------------------------------
        // GET YOUTUBE IDS AND TITLES
        // -----------------------------------

        YouTube youtube = null;
        try {
            youtube = getYouTubeService();
            int totalResults = 0;
            List<SongData> songDataList = new ArrayList<>();
            String nextPage = null;
            YouTube.PlaylistItems.List songRequest = youtube.playlistItems().list("snippet, contentDetails");
            songRequest.setPlaylistId(params.getStringValue(this, PARAM_PLAYLIST_ID));
            songRequest.setMaxResults(50L);

            do {
                if(nextPage != null) {
                    songRequest.setPageToken(nextPage);
                }
                PlaylistItemListResponse response = songRequest.execute();
                SongList songs = JSONUtils.parsePlaylistResponse(response.toString());
                nextPage = songs.getNextPageToken();
                totalResults = songs.getPageInfo().getTotalResults();

                for(SongList.Song item : songs.getItems()) {
                    System.out.println(item.getSnippet().getTitle());
                    SongData sd = new SongData();
                    sd.setId(item.getContentDetails().getVideoId());
                    sd.setTitle(item.getSnippet().getTitle());
                    songDataList.add(sd);
                }
            } while (songDataList.size() < totalResults);



            IVideoDownloadedHandler vdh = new IVideoDownloadedHandler() {

                private List<String> items = new ArrayList<>();

                private void convertSongs() {

                    //Get all files from path
                    File dir = new File(filePath);
                    File[] files = dir.listFiles();
                    for(File f : files) {
                        // Check if file is mp4
                        try {
                            if(!f.getCanonicalPath().contains(".mp4")) {
                                continue;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Convert to mp3
                        Thread t = new Thread(() -> {
                            try {
                                String cmd = "ffmpeg -i \"" + f.getCanonicalPath() + "\" " +
                                        "-acodec libmp3lame \"" +
                                        f.getCanonicalPath().replace(".mp4", ".mp3") + "\"";
                                executeConsoleCommand(cmd, "ffmpeg", false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        t.start();
                    }
                }

                @Override
                public void onReady(String url) {
                    //TODO: add song to downloaded song list.
                    this.items.remove(url);
                    if(this.items.size() <= 0) {
                        this.convertSongs();
                    }
                }

                @Override
                public void onFail(String url) {
                    this.items.remove(url);
                    System.out.println("Sorry this shit failed to download : " + url);
                    if(this.items.size() <= 0) {
                        this.convertSongs();
                    }
                }

                @Override
                public void addItem(String songId) {
                    this.items.add(songId);
                }
            };

            for(SongData sd : songDataList) {
                // TODO: check if song has been downloaded before.
                synchronized (vdh) {
                    vdh.addItem(sd.getId());
                }
                Thread t = new Thread(new VideoDownloader(vdh, sd));
                t.start();
            }


        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            System.err.println("There was a service error: " +
                    e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    public void downloadSong(SongData song) {

    }

    public String getSongName(SongData song) {
        return song.getTitle();
    }

    public void executeConsoleCommand(String command, String commandName, boolean waitResponse) throws IOException {
        Process p = Runtime.getRuntime().exec(command);
        InputStream is = p.getErrorStream();
        BufferedReader retReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while (is.read() != -1) {

        }
        if(waitResponse) {
            String ret;
            while((ret = retReader.readLine()) != null && ret.length() != 0) {
                System.out.println(commandName + " return = " + ret);
            }
        }
    }



    private interface IVideoDownloadedHandler {
        void onReady(String url);
        void onFail(String url);
        void addItem(String songId);
    }

    private class VideoDownloader implements Runnable {

        public static final String YTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
        private SongData song;
        IVideoDownloadedHandler vdh;

        public VideoDownloader(IVideoDownloadedHandler vdh, SongData song) {
            this.song = song;
            this.vdh = vdh;
        }

        @Override
        public void run() {
            try {
                System.out.println("Donloading " + song.getTitle() + " ... ");
                String videoUrl = YTUBE_BASE_URL + song.getId();
                String command = "python " + scriptPath + " "  + videoUrl + " " + filePath;
                executeConsoleCommand(command, "python", true);
                synchronized (this.vdh) {
                    this.vdh.onReady(song.getId());
                }
            } catch (IOException e) {
                synchronized (this.vdh) {
                    this.vdh.onFail(song.getId());
                }
                e.printStackTrace();
            }
        }
    }

    private class SongData {

        private String id;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
