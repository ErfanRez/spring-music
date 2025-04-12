package com.music.model;

import com.music.utils.Genres;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Song extends BaseEntity {

    private String title;

    @Column(unique = true)
    private String audioUrl;

    @Column(unique = true)
    private String audioKey;

    @Enumerated(EnumType.STRING)
    private Genres genre;

    @Column(nullable = false)
    private String duration;

    private long likeCount = 0;

    @Transient
    private boolean isFavorite = false;

    @Embedded
    @Column(nullable = false)
    private Cover cover;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "album_id", nullable = true)
    private Album album;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id", nullable = false)
    private User artist;
}

