package org.cuieney.videolife.provider;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.SparseArray;

import org.cuieney.videolife.provider.base.BaseContentProvider;
import org.cuieney.videolife.provider.base.TableInfo;

import java.util.Map;

/**
 * Created by liyanju on 2017/12/24.
 */

public class DownloadProvider extends BaseContentProvider {

    public static final int DOWNLOADEDKEY = 101;
    public static final String AUTHORITIES = "org.cuieney.videolife.provider";
    public static final String DATA_BASE = "download";

    @Override
    public void onAddTableInfo(SparseArray<TableInfo> tableInfoArray) {
        tableInfoArray.put(DOWNLOADEDKEY, new DownloadedTableInfo());
    }

    @Override
    public String onDataBaseName() {
        return DATA_BASE;
    }

    @Override
    public int onDataBaseVersion() {
        return 1;
    }

    @Override
    public void onDBUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static class DownloadedTableInfo extends TableInfo {

        public static final String TABLE_NAME = "Downloaded";

        public static final Uri URI = Uri.parse("content://" + DownloadProvider.AUTHORITIES + "/" + TABLE_NAME);

        public static final String ID = "id";
        public static final String PATH = "path";
        public static final String TITLE = "title";
        public static final String IMAGE = "image";
        public static final String DURATION = "duration";
        public static final String ARTISTNAME = "artistName";
        public static final String ALBUMNAME = "albumName";
        public static final String NEWDOWLOAD = "newdownload";

        @Override
        public String onTableName() {
            return TABLE_NAME;
        }

        @Override
        public Uri onContentUri() {
            return URI;
        }

        @Override
        public void onInitColumnsMap(Map<String, String> columnsMap) {
            columnsMap.put(ID, "int");
            columnsMap.put(PATH, "text");
            columnsMap.put(TITLE, "text");
            columnsMap.put(IMAGE, "text");
            columnsMap.put(DURATION, "text");
            columnsMap.put(ARTISTNAME, "text");
            columnsMap.put(ALBUMNAME, "text");
            columnsMap.put(NEWDOWLOAD, "int");

        }
    }
}
