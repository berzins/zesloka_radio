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
import eventservice.IClientConnection;
import executor.command.Command;
import executor.command.parameters.Parameter;
import utilities.GlobalSettings;
import utilities.JSONUtils;
import utilities.Util;
import utilities.youtube.SongList;
import utilities.youtube.YoutubeService;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DownloadYoutubeAudio extends Command {

    public static final String PARAM_PLAYLIST_ID = "playlist_id";

    static private String filePath = "";
    static private String scriptPath = "";
    private GlobalSettings.Settings settings;

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
    }

    @Override
    public void execute() {
        super.execute();

        // -----------------------------------
        // GET YOUTUBE IDS AND TITLES
        // -----------------------------------

        YouTube youtube = null;
        try {
            youtube = YoutubeService.getYouTubeService();
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

                    // Create Handler what starts downloading manage threads when files has been downloaded
                    IAudioFileConvertedHandler fc = new IAudioFileConvertedHandler() {

                        private int threads = 3;
                        private Queue<File> fileQueue = new ConcurrentLinkedDeque(Arrays.asList(files));

                        @Override
                        public void run() {
                            for(int i = 0; i < threads; i++) {
                                //poll file from list
                                File f = fileQueue.poll();
                                if(f == null) {break;}
                                Thread t = new Thread(new AudioFileConverter(this, f));
                                t.start();
                            }
                        }

                        @Override
                        public void onReady(File f) {
                            getIO().write(fileQueue.size() + " files remaining");
                            File file = fileQueue.poll();
                            if(file == null) return;
                            Thread t = new Thread(new AudioFileConverter(this, file));
                            t.start();
                        }

                        @Override
                        public void onFail(File f) {
                            fileQueue.add(f);
                        }
                    };
                    fc.run();

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


    public String getSongName(SongData song) {
        return song.getTitle();
    }

    public boolean executeConsoleCommand(String command, boolean waitResponse) throws IOException {
        Process p = Runtime.getRuntime().exec(command);
        BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        BufferedReader retReader = new BufferedReader(new InputStreamReader(p.getInputStream()));

        if(waitResponse) {
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = retReader.readLine()) != null) {
                sb.append(line);
            }
            IClientConnection cc = this.getIO();
            cc.write(sb.toString());
            sb = new StringBuilder();
            while((line = error.readLine()) != null) {
                sb.append(line);
            }
            if(sb.toString().length() > 0) {
                cc.write(sb.toString());
            }
//            cc.close();
        }
        return false;
    }

    private interface IAudioFileConvertedHandler {
        void onReady(File f);
        void onFail(File f);
        void run();
    }

    private class AudioFileConverter implements Runnable {
        private IAudioFileConvertedHandler afch;
        private File f;

        public AudioFileConverter(IAudioFileConvertedHandler afch, File f){
            this.afch = afch;
            this.f = f;
        }

        @Override
        public void run() {
            IClientConnection cc = DownloadYoutubeAudio.this.getIO();
            Process p = null;
            try {
                // Check if file is mp4
                try {
                    if(!f.getCanonicalPath().contains(".mp4")) {
                        this.afch.onReady(f);
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    this.afch.onFail(f);
                    return;
                }
                String cmd = "ffmpeg -i \"" + f.getCanonicalPath() + "\" " +
                        "-acodec libmp3lame \"" +
                        f.getCanonicalPath().replace(".mp4", ".mp3") + "\"";
                p = Runtime.getRuntime().exec(cmd);
                BufferedReader retReader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                // Without this nonsense ffmpeg command stops and crushes in the middle of execution.
                InputStream is = p.getErrorStream();
                int c;
                while((c = is.read()) != -1) {
                    System.out.print((char) c);
                }
                cc.write(f.getCanonicalPath() + " converted");
                this.afch.onReady(f);
            } catch (IOException e) {
                e.printStackTrace();
                cc.write(f.getName() + "failed with error " + e.getMessage());
                this.afch.onFail(f);
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
                GlobalSettings.Settings settings = GlobalSettings.getInstance().getSettings();
                String py = settings.getValue("python_path");
                String command = py + " " + scriptPath + " "  + videoUrl + " " + filePath;
                executeConsoleCommand(command, true);
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
