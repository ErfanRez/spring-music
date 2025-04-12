package com.music.utils;

public enum Genres {
    POP("Pop"),
    ROCK("Rock"),
    HIP_HOP("Hip Hop"),
    R_AND_B("R&B"),
    CLASSICAL("Classical"),
    RAP("Rap"),
    COUNTRY("Country"),
    ALTERNATIVE("Alternative"),
    JAZZ("Jazz"),
    BLUES("Blues"),
    ELECTRONIC("Electronic"),
    METAL("Metal"),
    REGGAE("Reggae"),
    FOLK("Folk"),
    PUNK("Punk"),
    SOUL("Soul"),
    INDIE("Indie"),
    DISCO("Disco"),
    FUNK("Funk"),
    LATIN("Latin"),
    OPERA("Opera");

    private final String displayName;

    Genres(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

