package com.music.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class ArtistPic {
    @Column(name = "profile_key")
    private String key;

    @Column(name = "profile_url")
    private String url;
}
