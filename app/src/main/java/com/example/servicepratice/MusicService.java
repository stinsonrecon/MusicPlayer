package com.example.servicepratice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.Serializable;
import java.util.ArrayList;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private LocalBinder localBinder = new LocalBinder();
    private ArrayList<Track> songList;
    private int position = 0;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this,"id");
        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.earth_wind_fire);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                createNotificationChannel("music channel", "play music", "11"))
                .setSmallIcon(R.drawable.ic_baseline_play_arrow_24)
                .setLargeIcon(icon)
                .setContentTitle("MusicService")
                .setContentText("MusicPlaying")
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSessionCompat.getSessionToken()))
                .setPriority(NotificationCompat.
                        PRIORITY_MAX);

        Notification notification = builder.build();


        songList = (ArrayList<Track>) intent.getSerializableExtra("track");
        mediaPlayer = MediaPlayer.create(this,songList.get(position).getRawSong());
        startForeground(11,notification);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.release();
        super.onDestroy();
    }

    private String createNotificationChannel(String channel_name, String channel_description, String CHANNEL_ID) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = channel_name;
            String description = channel_description;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            return CHANNEL_ID;
        }
        return "";
    }

    public void pauseMusic(){
        mediaPlayer.pause();
    }

    public  void playMusic(){

        mediaPlayer.start();
    }

    public void nextSong(){
        mediaPlayer.release();
        position++;
        if (position >= songList.size()) {
            position = 0;
        }
        mediaPlayer = MediaPlayer.create(this,songList.get(position).getRawSong());
        mediaPlayer.start();
    }

    public void previousSong(){
        mediaPlayer.release();
        position--;
        if(position <= songList.size()){
            position = songList.size() - 1;
        }
        mediaPlayer = MediaPlayer.create(this,songList.get(position).getRawSong());
        mediaPlayer.start();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public int getPosition(){
        return position;
    }

    public class LocalBinder extends Binder {
        public MusicService getInstance(){
            return MusicService.this;
        }
    }

    public int getTotalTime(){
        return mediaPlayer.getDuration();
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    public int getDuration(){
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                Log.v("playing", (mediaPlayer.getCurrentPosition() / 1000) + "");
                return mediaPlayer.getCurrentPosition() / 1000;
            } else {
                //Log.v("not playing", (mediaPlayer.getCurrentPosition() / 1000) + "");
                return 0;
            }
        }
        catch (IllegalStateException i) {
            i.printStackTrace();
            return 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
