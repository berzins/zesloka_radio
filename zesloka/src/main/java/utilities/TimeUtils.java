package utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static String getCurrentTimeString() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()).toString();
    }
}
