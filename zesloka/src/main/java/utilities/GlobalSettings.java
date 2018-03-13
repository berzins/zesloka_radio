package utilities;

import com.sun.scenario.Settings;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GlobalSettings implements Serializable {



    public static final String SETTINGS_FILE_NAME = "settings.json";

    private static GlobalSettings instance;
    private static Object lock;
    private Settings settings;

    private GlobalSettings() {
        try {
            BufferedReader file_reader = new BufferedReader(new FileReader(SETTINGS_FILE_NAME));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = file_reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            settings = JSONUtils.parseSettings(json);
            file_reader.close();
        } catch (FileNotFoundException e) {
            String json = JSONUtils.createJSON(new Settings());
            try {
                BufferedWriter file_writer = new BufferedWriter(new FileWriter(SETTINGS_FILE_NAME));
                file_writer.write(json);
                file_writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GlobalSettings getInstance() {
            if(instance == null) {
                instance = new GlobalSettings();
            }
            return  instance;
    }

    public GlobalSettings.Settings getSettings() {
        return this.settings;
    }



    /**
     * Container classes to hold setting values
     */
    public class Settings implements Serializable {

        private List<Setting> items;

        public Settings() {
            this.items = new ArrayList<>();
        }

        public List<Setting> getItems() {
            return items;
        }

        public String getValue(String key) {
            for(Setting s : items) {
                if(s.getKey().equals(key)) {
                    return s.getValue();
                }
            }
            return null;
        }

        /**
         * @return true if successful, if setting wasnt found return is false
         */
        public boolean setValue(String key, String value) {
            for(Setting s : items) {
                if(s.getKey().equals(key)) {
                    s.setValue(value);
                    return true;
                }
            }
            return  false;
        }
    }

    private class Setting implements Serializable{
        private String key;
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

}
