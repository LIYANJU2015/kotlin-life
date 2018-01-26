package org.cuieney.videolife;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.notification.BaseNotificationItem;
import com.liulishuo.filedownloader.notification.FileDownloadNotificationHelper;
import com.liulishuo.filedownloader.notification.FileDownloadNotificationListener;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.rating.RatingActivity;

import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.common.utils.ToastUtil;
import org.cuieney.videolife.common.utils.Utils;
import org.cuieney.videolife.entity.DownloadSong;
import org.cuieney.videolife.entity.wyBean.TracksBean;
import org.cuieney.videolife.provider.DownloadDao;
import org.cuieney.videolife.ui.act.MainActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by liyanju on 2017/12/24.
 */

public class FileDownloaderHelper {

    public static final String TAG = "FileDownloader";
    private static File defaultfile = new File(Environment.getExternalStorageDirectory(),
            App.getInstance().getString(R.string.app_name));
    private static Context sContext = App.getInstance();

    public static void addDownloadTask(TracksBean song) {
        if (song == null) {
            return;
        }

        if (!defaultfile.exists()) {
            defaultfile.mkdirs();
        }

        if (!defaultfile.exists() || !defaultfile.canWrite() || !defaultfile.canRead()) {
            defaultfile = new File(Environment.getExternalStorageDirectory(), "FileDownloader");
        }

        if (!defaultfile.exists()) {
            defaultfile.mkdirs();
        }

        String path = defaultfile + File.separator + String.valueOf(song.getSongname()) + ".mp3";

        int createDownloadId = FileDownloadUtils.generateId(song.getFilename(), path);
        if (DownloadDao.getDownloadTaskById(sContext, createDownloadId) != null) {
            ToastUtil.showLongToastSafe(sContext.getString(R.string.download_added));
            return;
        }

        FileDownloader.getImpl().create(song.getFilename())
                .setPath(path)
                .setAutoRetryTimes(1)
                .setTag(song)
                .setListener(new SelfNotificationListener(new FileDownloadNotificationHelper()))
                .start();
        LogUtil.d(" stream_url::::" + song.getFilename());

        ToastUtil.showLongToastSafe(song.getSongname() + " " + sContext.getString(R.string.download_add_success));
    }


    public static class SelfNotificationListener extends FileDownloadNotificationListener {

        private NotificationManager manager;

        public SelfNotificationListener(FileDownloadNotificationHelper helper) {
            super(helper);
            manager = (NotificationManager) FileDownloadHelper.getAppContext().
                    getSystemService(Context.NOTIFICATION_SERVICE);
        }

        @Override
        protected BaseNotificationItem create(BaseDownloadTask task) {
            return new NotificationItem(task.getId(), ((TracksBean) task.getTag()).getSongname(),
                    "");
        }

        @Override
        public void destroyNotification(final BaseDownloadTask task) {
            super.destroyNotification(task);
            Utils.runSingleThread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.d("destroyNotification getStatus " + task.getStatus());
                    if (task.getStatus() == FileDownloadStatus.completed) {
                        TracksBean song = (TracksBean) task.getTag();
                        String path = task.getPath();
                        int id = task.getId();
                        LogUtil.d("destroyNotification completed id :" + id + " getTitle:: "
                                + song.getSongname() + " path " + path);

                        ToastUtil.showLongToastSafe(song.getSongname() + " " + sContext.getString(R.string.download_video_success));

                        DownloadSong downloadSong = new DownloadSong(song.getSongname(),
                                "", song.getSonger(), path, 0,
                                song.getSongphoto(), id, true);
                        DownloadDao.addDownloadTask(sContext, downloadSong);

                        notifiyDownloadFinished();

                        showCompletedNotification(task.getId(), song.getSongname());

                        RatingActivity.setPopTotalCount(App.getInstance(), 2);
                        RatingActivity.launch(App.getInstance(), null,
                                App.getInstance().getString(R.string.star_five_text));
                    } else {
                        File file = new File(task.getPath());
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
            });
        }

        private void showCompletedNotification(int id, String title) {
            NotificationCompat.Builder builder = new NotificationCompat.
                    Builder(FileDownloadHelper.getAppContext());
            Intent intent = new Intent(sContext, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(sContext, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setDefaults(Notification.DEFAULT_LIGHTS)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(App.getInstance().getResources().getString(R.string.finish_download))
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_download);
            manager.notify(id, builder.build());
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            super.error(task, e);
            e.printStackTrace();
            ToastUtil.showLongToastSafe(sContext.getString(R.string.error_download));
        }
    }

    private static ArrayList<Runnable> sRunnableList = new ArrayList<>();

    public static void registerDownloadFinishListener(Runnable runnable) {
        if (sRunnableList.contains(runnable)) {
            sRunnableList.remove(runnable);
        }
        sRunnableList.add(runnable);
    }

    private static void notifiyDownloadFinished() {
        for (Runnable runnable : sRunnableList) {
            runnable.run();
        }
    }

    public static void removeDownloadFinishListener(Runnable runnable) {
        sRunnableList.remove(runnable);
    }

    public static class NotificationItem extends BaseNotificationItem {

        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        private NotificationItem(int id, String title, String desc) {
            super(id, title, desc);
            Intent intent = new Intent(sContext, MainActivity.class);

            this.pendingIntent = PendingIntent.getActivity(sContext, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            builder = new NotificationCompat.
                    Builder(FileDownloadHelper.getAppContext());

            builder.setDefaults(Notification.DEFAULT_LIGHTS)
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .setContentTitle(getTitle())
                    .setContentText(desc)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(android.R.drawable.stat_sys_download);

        }

        @Override
        public void show(boolean statusChanged, int status, boolean isShowProgress) {

            String desc = getDesc();
            switch (status) {
                case FileDownloadStatus.pending:
                    desc += " prepare";
                    break;
                case FileDownloadStatus.started:
                    desc += " started";
                    break;
                case FileDownloadStatus.progress:
                    desc += " downloading... " + (int)(getSofar() * 1f / getTotal() * 1f * 100) + "%";
                    break;
                case FileDownloadStatus.retry:
                    desc += " retry";
                    break;
                case FileDownloadStatus.error:
                    desc += " error";
                    break;
                case FileDownloadStatus.paused:
                    desc += " paused";
                    break;
                case FileDownloadStatus.completed:
                    desc += " completed";
                    break;
                case FileDownloadStatus.warn:
                    desc += " warn";
                    break;
            }

            builder.setContentTitle(getTitle())
                    .setContentText(desc);

            if (statusChanged) {
                builder.setTicker(desc);
            }

            builder.setProgress(getTotal(), getSofar(), !isShowProgress);
            getManager().notify(getId(), builder.build());
        }

    }
}
