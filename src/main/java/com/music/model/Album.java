package com.music.model;

import com.music.utils.Genres;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Album extends BaseEntity {

    private String title;

    private LocalDate releaseDate;

    private Double price;

    @Enumerated(EnumType.STRING)
    private Genres genre;

    private long likeCount = 0;

    @Transient
    private boolean isFavorite = false;

    @Embedded
    private Cover cover;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Song> songs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id", nullable = false)
    private User artist;

    public void addSong(Song song) {
        songs.add(song);
        song.setAlbum(this);
        song.setCover(this.cover);
    }

    public void removeSong(Song song) {
        songs.remove(song);
        song.setAlbum(null);
        song.setCover(null);
    }
}