package org.cuieney.videolife.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import org.cuieney.videolife.entity.DownloadSong;

import java.util.ArrayList;

/**
 * Created by liyanju on 2017/12/24.
 */

public class DownloadDao {

    public static DownloadSong getDownloadTaskById(Context context, int id) {
        String selection = DownloadProvider.DownloadedTableInfo.ID
                + " = ? ";
        String selectionArgs[] = new String[]{String.valueOf(id)};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver()
                    .query(DownloadProvider.DownloadedTableInfo.URI,
                            null, selection, selectionArgs, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                return DownloadSong.cursorToDownloadSong(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static void removeDownloaded(Context context, int id) {
        String where = DownloadProvider.DownloadedTableInfo.ID + " = " + id;
        context.getContentResolver().delete(DownloadProvider.DownloadedTableInfo.URI, where, null);
    }

    public static int getNewDownloadCount(Context context) {
        Cursor cursor = null;
        try {
            String selecton = DownloadProvider.DownloadedTableInfo.NEWDOWLOAD + " = 1";
            cursor = context.getContentResolver().query(DownloadProvider.DownloadedTableInfo.URI,
                    null, selecton, null, null);
            if (cursor != null) {
                return cursor.getCount();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    public static void updateDownloadNew(Context context) {
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(DownloadProvider.DownloadedTableInfo.NEWDOWLOAD, 0);
        String selecton = DownloadProvider.DownloadedTableInfo.NEWDOWLOAD + " = 1";
        context.getContentResolver().update(DownloadProvider.DownloadedTableInfo.URI, contentValues,
                selecton, null);
    }

    public static ArrayList<DownloadSong> getAllDownloaded(Context context) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(DownloadProvider.DownloadedTableInfo.URI,
                    null, null, null, null);
            ArrayList<DownloadSong> list = new ArrayList<>();
            while (cursor != null && cursor.moveToNext()) {
                  DownloadSong downloadSong = DownloadSong.cursorToDownloadSong(cursor);
                  list.add(downloadSong);
            }
            return list;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void addDownloadTask(Context context, DownloadSong downloadSong) {
        context.getContentResolver().insert(DownloadProvider.DownloadedTableInfo.URI,
                DownloadSong.createContentValues(downloadSong));
    }
}
