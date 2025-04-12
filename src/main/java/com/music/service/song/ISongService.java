package com.music.service.song;


import com.music.dto.TrackDto;
import com.music.model.Album;
import com.music.model.Song;
import com.music.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ISongService {
    void saveTrackWithCover(TrackDto trackDto, MultipartFile audioFile, MultipartFile coverFile, User user) throws IOException;

    List<Song> findAllSongs(User user);

    List<Song> findTopTen(User user);

    List<Song> findSingleTracksByArtist(User artist);

    List<Song> findByAlbum(Album album, User user);

    void deleteSong(Long id, User user);

    void addFavorite(Long id, User user);

    Set<Song> findUserFavorites(User auth);
}