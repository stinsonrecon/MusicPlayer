package com.example.servicepratice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    boolean check = true;
    boolean checkStart = true;
    MusicService musicService;
    TextView songTitle;
    TextView author;
    ImageButton previous;
    ImageButton next;
    ImageButton play;
    CircleImageView songAlbumCover;
    SeekBar seekBar;

    ArrayList<Track> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songTitle = findViewById(R.id.song);
        author = findViewById(R.id.author);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        play = findViewById(R.id.play);
        songAlbumCover = findViewById(R.id.songAlbum);
        seekBar = findViewById(R.id.seekBar);

        songList = new ArrayList<>();

        Track emCoChacKhong = new Track("Em co chac khong", "Ngot", R.raw.em_co_chac_khong_bai_ca_rebound,R.drawable.ngot);
        Track mauDenTrang = new Track("Mau (Den trang)", "Ngot", R.raw.mau_den_trang, R.drawable.ngot);
        Track chuongBaoThuc = new Track("Chuong bao thuc", "Ngot", R.raw.chuong_bao_thuc_sang_roi, R.drawable.ngot);
        Track giaVo = new Track("Gia vo", "Ngot", R.raw.gia_vo, R.drawable.ngot);

        songList.add(emCoChacKhong);
        songList.add(mauDenTrang);
        songList.add(chuongBaoThuc);
        songList.add(giaVo);

        Intent i = new Intent(MainActivity.this,MusicService.class);
        i.putExtra("track",songList);
        startService(i);
        bindService(i,MainActivity.this,Context.BIND_AUTO_CREATE);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!musicService.isPlaying()){
                        musicService.playMusic();
                        play.setImageResource(R.drawable.ic_baseline_pause_24);
                        songTitle.setText(songList.get(musicService.getPosition()).getSongTitle());
                        author.setText(songList.get(musicService.getPosition()).getAuthor());
                        songAlbumCover.setImageResource(songList.get(musicService.getPosition()).getSongAlbumCover());
                }
                else {
                    musicService.pauseMusic();
                    play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.nextSong();
                songTitle.setText(songList.get(musicService.getPosition()).getSongTitle());
                author.setText(songList.get(musicService.getPosition()).getAuthor());
                songAlbumCover.setImageResource(songList.get(musicService.getPosition()).getSongAlbumCover());
                if(musicService.isPlaying()) {
                    play.setImageResource(R.drawable.ic_baseline_pause_24);
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.previousSong();
                songTitle.setText(songList.get(musicService.getPosition()).getSongTitle());
                author.setText(songList.get(musicService.getPosition()).getAuthor());
                songAlbumCover.setImageResource(songList.get(musicService.getPosition()).getSongAlbumCover());
                if(musicService.isPlaying()) {
                    play.setImageResource(R.drawable.ic_baseline_pause_24);
                }
            }
        });

        final TextView duration = findViewById(R.id.duration);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                duration.setVisibility(View.VISIBLE);
                int x = (int) Math.ceil(progress / 1000f);

                if (x == 0 && musicService.getMediaPlayer() != null && !musicService.isPlaying()) {
                    musicService.pauseMusic();
                    play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    MainActivity.this.seekBar.setProgress(0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                duration.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (musicService.getMediaPlayer() != null && musicService.getMediaPlayer().isPlaying()) {
                    musicService.getMediaPlayer().seekTo(seekBar.getProgress());
                }
            }
        });
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.LocalBinder localBinder = (MusicService.LocalBinder)service;
        musicService = localBinder.getInstance();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}