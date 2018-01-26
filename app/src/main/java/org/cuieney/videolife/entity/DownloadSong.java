package org.cuieney.videolife.entity;

import android.content.ContentValues;
import android.database.Cursor;

import org.cuieney.videolife.provider.DownloadProvider;


/**
 * Created by liyanju on 2017/12/24.
 */

public class DownloadSong {

    public String downloadUrl;

    public String title;

    public String albumName;

    public String artistName;

    public String data;

    public long duration;

    public String image;

    public int id;

    public boolean isNewDowload;

    public static DownloadSong cursorToDownloadSong(Cursor cursor) {
        DownloadSong downloadSong = new DownloadSong();
        downloadSong.title = cursor.getString(cursor.getColumnIndexOrThrow(DownloadProvider
                .DownloadedTableInfo.TITLE));
        downloadSong.albumName = cursor.getString(cursor.getColumnIndexOrThrow(DownloadProvider
                .DownloadedTableInfo.ALBUMNAME));
        downloadSong.artistName = cursor.getString(cursor.getColumnIndexOrThrow(DownloadProvider
                .DownloadedTableInfo.ARTISTNAME));
        downloadSong.data = cursor.getString(cursor.getColumnIndexOrThrow(DownloadProvider
                .DownloadedTableInfo.PATH));
        downloadSong.duration = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadProvider
                .DownloadedTableInfo.DURATION));
        downloadSong.image = cursor.getString(cursor.getColumnIndexOrThrow(DownloadProvider.DownloadedTableInfo.IMAGE));
        downloadSong.id = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadProvider.DownloadedTableInfo.ID));
        downloadSong.isNewDowload = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadProvider.DownloadedTableInfo.NEWDOWLOAD)) == 1;
        return downloadSong;
    }

    public static ContentValues createContentValues(DownloadSong downloadSong) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DownloadProvider
                .DownloadedTableInfo.TITLE, downloadSong.title);
        contentValues.put(DownloadProvider
                .DownloadedTableInfo.ALBUMNAME, downloadSong.albumName);
        contentValues.put(DownloadProvider
                .DownloadedTableInfo.ARTISTNAME, downloadSong.artistName);
        contentValues.put(DownloadProvider
                .DownloadedTableInfo.PATH, downloadSong.data);
        contentValues.put(DownloadProvider
                .DownloadedTableInfo.DURATION, downloadSong.duration);
        contentValues.put(DownloadProvider
                .DownloadedTableInfo.IMAGE, downloadSong.image);
        contentValues.put(DownloadProvider
                .DownloadedTableInfo.ID, downloadSong.id);
        contentValues.put(DownloadProvider
                .DownloadedTableInfo.NEWDOWLOAD, downloadSong.isNewDowload ? 1 : 0);
        return contentValues;
    }

    public DownloadSong(){}

    public DownloadSong(String title, String albumName, String artistName,
                        String path, long duration, String image, int id, boolean isNewDowload) {
        this.title = title;
        this.albumName = albumName;
        this.artistName = artistName;
        this.data = path;
        this.duration = duration;
        this.image = image;
        this.id = id;
        this.isNewDowload = isNewDowload;
    }
}
