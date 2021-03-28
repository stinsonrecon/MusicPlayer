package com.example.servicepratice;

import java.io.Serializable;

public class Track implements Serializable {
    private String songTitle;
    private String author;
    private int rawSong;
    private int songAlbumCover;

    public Track(String songTitle, String author, int rawSong, int songAlbumCover) {
        this.songTitle = songTitle;
        this.author = author;
        this.rawSong = rawSong;
        this.songAlbumCover = songAlbumCover;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getRawSong() {
        return rawSong;
    }

    public void setRawSong(int rawSong) {
        this.rawSong = rawSong;
    }

    public int getSongAlbumCover() {
        return songAlbumCover;
    }

    public void setSongAlbumCover(int songAlbumCover) {
        this.songAlbumCover = songAlbumCover;
    }
}
