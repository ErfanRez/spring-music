package com.music.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Cover {
    @Column(name = "cover_key")
    private String key;

    @Column(name = "cover_url")
    private String url;
}
