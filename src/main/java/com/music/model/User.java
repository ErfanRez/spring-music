package com.music.model;

import com.music.utils.Roles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User extends BaseEntity implements UserDetails {
    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true, unique = true)
    private String nickname; // Optional

    @Embedded
    @Column(nullable = true)
    private ArtistPic profilePic;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role = Roles.USER; // Default role is USER, (ADMIN, USER, ARTIST)

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "user_favorite_songs",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private Set<Song> favoriteSongs = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "user_favorite_albums",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id")
    )
    private Set<Album> favoriteAlbums = new HashSet<>();

    @OneToMany(mappedBy = "artist",cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Song> songs = new ArrayList<>();

    @OneToMany(mappedBy = "artist", orphanRemoval = true, cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    private List<Album> albums = new ArrayList<>();

    public User(String username, String password, Roles role, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public String getFullName() {
        return (firstName != null ? firstName : "") +
                (lastName != null ? " " + lastName : "");
    }

    public void addFavoriteSong(Song song) {
        favoriteSongs.add(song);
    }

    public void removeFavoriteSong(Song song) {
        favoriteSongs.remove(song);
    }

    public void addFavoriteAlbum(Album album) {
        favoriteAlbums.add(album);
    }

    public void removeFavoriteAlbum(Album album) {
        favoriteAlbums.remove(album);
    }

    public void addSong(Song song) {
        songs.add(song);
        song.setArtist(this);
    }

    public void removeSong(Song song) {
        songs.remove(song);
        song.setArtist(null);
    }

    public void addAlbum(Album album) {
        albums.add(album);
        album.setArtist(this);
    }

    public void removeAlbum(Album album) {
        albums.remove(album);
        album.setArtist(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}