package org.cuieney.videolife;

import android.os.Bundle;

import com.facebook.appevents.AppEventsLogger;

/**
 * Created by liyanju on 2018/1/5.
 */

public class FacebookReportUtils {

    public static void logSentPageShow(String page)  {
        AppEventsLogger logger = AppEventsLogger.newLogger(App.getInstance());
        Bundle bundle = new Bundle();
        bundle.putString("page", page);
        logger.logEvent("pageShow",bundle);
    }

    public static void logDownloadFinished(String title)  {
        AppEventsLogger logger = AppEventsLogger.newLogger(App.getInstance());
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        logger.logEvent("downloadFinished",bundle);
    }

    public static void logSongPlay(String title)  {
        AppEventsLogger logger = AppEventsLogger.newLogger(App.getInstance());
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        logger.logEvent("songPlay",bundle);
    }

    public static void logSongDownload(String title)  {
        AppEventsLogger logger = AppEventsLogger.newLogger(App.getInstance());
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        logger.logEvent("songDownload",bundle);
    }

    public static void logSentFBAdShow(String page)  {
        AppEventsLogger logger = AppEventsLogger.newLogger(App.getInstance());
        Bundle bundle = new Bundle();
        bundle.putString("page", page);
        logger.logEvent("FBAdShow",bundle);
    }
}
