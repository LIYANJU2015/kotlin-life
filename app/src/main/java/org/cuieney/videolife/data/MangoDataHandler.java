package org.cuieney.videolife.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;

import org.cuieney.videolife.common.utils.Utils;
import org.cuieney.videolife.entity.MusicListBean;
import org.cuieney.videolife.entity.wyBean.TracksBean;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liyanju on 2018/1/25.
 */

public class MangoDataHandler {

    private static final String DB_NAME = "sqlite_magnatune.db";

    public static final int SONG_NAME = 0;
    public static final int SONG_MP3 = 1;
    public static final int SONG_DURATION = 2;
    public static final int ALBUM_NAME = 3;
    public static final int ALBUM_DESCRIPTION = 4;
    public static final int ALBUM_RELEASE_DATE = 5;
    public static final int ARTISTS_NAME = 6;
    public static final int ARTISTS_DESCRIPTION = 7;
    public static final int ARTISTS_PHOTO = 8;
    public static final int ARTISTS_ID = 9;
    public static final int ALBUM_ID = 10;

    public static final String HOST_URL = "http://he3.magnatune.com";
    public static final String ALBUM_IMAGE_URL = HOST_URL + "/music/";
    public static final String DOWNLOAD_MP3_URL = HOST_URL + "/all/";

    private static final String QUERY_ALBUM_SONG = "select songs.name,songs.mp3,songs.duration, albums.name,albums.description,albums.release_date, artists.name, artists.description, artists.photo, artists.artists_id, songs.album_id from albums,songs,artists " +
            "where artists.artists_id=albums.artist_id and albums.album_id=songs.album_id;";

    private static ArrayList<MusicListBean> sAlbumList = new ArrayList<>();
    private static ArrayList<MusicListBean> sSongList = new ArrayList<>();
    private static ArrayList<MusicListBean> sArtistList = new ArrayList<>();

    public static void init(Context context) {
        AssetsDatabaseManager.initManager(context);
        AssetsDatabaseManager.getManager().getDatabase(DB_NAME);
    }

    public static ArrayList<MusicListBean> searchData(ArrayList<MusicListBean> searchList,
                                                      String query) {
        ArrayList<MusicListBean> searchedList = new ArrayList<>();
        for (MusicListBean musicListBean : searchList) {
            if (musicListBean.getMname().toLowerCase().contains(query.toLowerCase())){
                searchedList.add(musicListBean);
            }
        }
        return searchedList;
    }

    private static void handleAlbumFromCursor(Cursor cursor) {
        cursor.moveToFirst();
        int initAlbumId = 0;
        MusicListBean musicListBean = null;
        while (cursor.moveToNext()) {
            String songName = cursor.getString(SONG_NAME);
            String mp3 = cursor.getString(SONG_MP3);
            int duration = cursor.getInt(SONG_DURATION);
            String albumName = cursor.getString(ALBUM_NAME);
            String albumDescription = cursor.getString(ALBUM_DESCRIPTION);
            long albumReleaseDate = cursor.getLong(ALBUM_RELEASE_DATE);
            String artistName = cursor.getString(ARTISTS_NAME);
            String aritistDescription = cursor.getString(ARTISTS_DESCRIPTION);
            String aritistPhonto = cursor.getString(ARTISTS_PHOTO);
            int aritistID = cursor.getInt(ARTISTS_ID);
            int albumID = cursor.getInt(ALBUM_ID);

            if (initAlbumId == 0) {
                initAlbumId = albumID;
                musicListBean = new MusicListBean();
                musicListBean.setTitle(albumName);
                musicListBean.setDescription(albumDescription);
                musicListBean.setArtwork_url(ALBUM_IMAGE_URL + artistName + "/"
                        + albumName + "/cover_300.jpg");
            } else if (initAlbumId != albumID) {
                initAlbumId = albumID;
                sAlbumList.add(musicListBean);
                musicListBean = new MusicListBean();
                musicListBean.setTitle(albumName);
                musicListBean.setDescription(albumDescription);
                musicListBean.setArtwork_url(ALBUM_IMAGE_URL + artistName + "/"
                        + albumName + "/cover_300.jpg");
            } else if (cursor.isLast()) {
                sAlbumList.add(musicListBean);
            }

            TracksBean tracksBean = new TracksBean();
            tracksBean.setFilename(DOWNLOAD_MP3_URL + encode(mp3));
            tracksBean.setSongname(songName);
            tracksBean.singer = artistName;
            tracksBean.setSongphoto(HOST_URL + aritistPhonto);
            musicListBean.getTracks().add(tracksBean);
        }

        if (sAlbumCallBack != null) {
            sAlbumCallBack.onCallBack(sAlbumList);
        }
    }

    private static void handleArtistFromCursor(Cursor cursor) {
        cursor.moveToFirst();
        int initArtistId = 0;
        MusicListBean musicListBean = null;
        while (cursor.moveToNext()) {
            String songName = cursor.getString(SONG_NAME);
            String mp3 = cursor.getString(SONG_MP3);
            int duration = cursor.getInt(SONG_DURATION);
            String albumName = cursor.getString(ALBUM_NAME);
            String albumDescription = cursor.getString(ALBUM_DESCRIPTION);
            long albumReleaseDate = cursor.getLong(ALBUM_RELEASE_DATE);
            String artistName = cursor.getString(ARTISTS_NAME);
            String aritistDescription = cursor.getString(ARTISTS_DESCRIPTION);
            String aritistPhonto = cursor.getString(ARTISTS_PHOTO);
            int aritistID = cursor.getInt(ARTISTS_ID);
            int albumID = cursor.getInt(ALBUM_ID);

            if (initArtistId == 0) {
                initArtistId = aritistID;
                musicListBean = new MusicListBean();
                musicListBean.setTitle(artistName);
                musicListBean.setDescription(aritistDescription);
                musicListBean.setArtwork_url(HOST_URL + aritistPhonto);
            } else if (initArtistId != aritistID) {
                initArtistId = aritistID;
                sArtistList.add(musicListBean);

                musicListBean = new MusicListBean();
                musicListBean.setTitle(artistName);
                musicListBean.setDescription(aritistDescription);
                musicListBean.setArtwork_url(HOST_URL + aritistPhonto);
            } else if (cursor.isLast()) {
                sArtistList.add(musicListBean);
            }

            TracksBean tracksBean = new TracksBean();
            tracksBean.setFilename(DOWNLOAD_MP3_URL + encode(mp3));
            tracksBean.setSongname(songName);
            tracksBean.singer = artistName;
            tracksBean.setSongphoto(ALBUM_IMAGE_URL + artistName + "/"
                    + albumName + "/cover_300.jpg");
            musicListBean.getTracks().add(tracksBean);
        }

        if (sArtistsCallBack != null) {
            sArtistsCallBack.onCallBack(sArtistList);
        }
    }

    private static String encode(String str) {
        return str.replace(" ", "%20");
    }

    private static void handleSongFromCursor(Cursor cursor) {
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String songName = cursor.getString(SONG_NAME);
            String mp3 = cursor.getString(SONG_MP3);
            int duration = cursor.getInt(SONG_DURATION);
            String albumName = cursor.getString(ALBUM_NAME);
            String albumDescription = cursor.getString(ALBUM_DESCRIPTION);
            long albumReleaseDate = cursor.getLong(ALBUM_RELEASE_DATE);
            String artistName = cursor.getString(ARTISTS_NAME);
            String aritistDescription = cursor.getString(ARTISTS_DESCRIPTION);
            String aritistPhonto = cursor.getString(ARTISTS_PHOTO);
            int aritistID = cursor.getInt(ARTISTS_ID);
            int albumID = cursor.getInt(ALBUM_ID);

            MusicListBean musicListBean = new MusicListBean();
            musicListBean.setArtwork_url(HOST_URL + aritistPhonto);
            musicListBean.setDescription(albumDescription);
            musicListBean.setTitle(songName);

            TracksBean tracksBean = new TracksBean();
            tracksBean.setFilename(DOWNLOAD_MP3_URL + encode(mp3));
            tracksBean.singer = artistName;
            tracksBean.setSongname(songName);
            tracksBean.setSongphoto(HOST_URL + aritistPhonto);
            musicListBean.getTracks().add(tracksBean);

            sSongList.add(musicListBean);
        }

        if (sSongCallBack != null) {
            sSongCallBack.onCallBack(sSongList);
        }
    }


    public static void initMangoData() {
        SQLiteDatabase database = AssetsDatabaseManager.getManager().getDatabase(DB_NAME);
        if (database == null) {
            return;
        }

        Cursor cursor = null;
        try {
            cursor = database.rawQuery(QUERY_ALBUM_SONG, null);
            if (cursor == null) {
                return;
            }

            handleAlbumFromCursor(cursor);
            handleArtistFromCursor(cursor);
            handleSongFromCursor(cursor);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private static CallBack sAlbumCallBack;

    public static void registerAlbumDataListener(CallBack callBack) {
        sAlbumCallBack = callBack;
        if (sAlbumList.size() > 0) {
            Utils.runSingleThread(new Runnable() {
                @Override
                public void run() {
                    sAlbumCallBack.onCallBack(sAlbumList);
                }
            });
        }
    }

    public static void unRegisterAlbumDataListener() {
        sAlbumCallBack = null;
    }

    private static CallBack sArtistsCallBack;

    public static void registerArtistsDataListener(CallBack callBack) {
        sArtistsCallBack = callBack;
        if (sArtistList.size() > 0) {
            Utils.runSingleThread(new Runnable() {
                @Override
                public void run() {
                    sArtistsCallBack.onCallBack(sArtistList);
                }
            });
        }
    }

    public static void unRegisterArtistsDataListener() {
        sArtistsCallBack = null;
    }

    private static CallBack sSongCallBack;

    public static void registerSongDataListener(CallBack callBack) {
        sSongCallBack = callBack;
        if (sSongList.size() > 0) {
            Utils.runSingleThread(new Runnable() {
                @Override
                public void run() {
                    sSongCallBack.onCallBack(sSongList);
                }
            });
        }
    }

    public static void unRegisterSongDataListener() {
        sSongCallBack = null;
    }

    public interface CallBack {

        void onCallBack(ArrayList<MusicListBean> list);
    }
}
