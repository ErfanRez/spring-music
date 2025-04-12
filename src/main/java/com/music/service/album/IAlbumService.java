package com.music.service.album;

import com.music.dto.AlbumDto;
import com.music.model.Album;
import com.music.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IAlbumService {
    void saveAlbumWithSongsAndCover(AlbumDto albumDto, List<String> songTitles, List<MultipartFile> songFiles, User user) throws IOException;

    Album findById(Long id, User auth);

    List<Album> findByArtist(User artist);

    List<Album> findAllAlbums(User auth);

    List<Album> findTopTen(User auth);

    void deleteAlbum(Long id, User auth);

    void addFavorite(Long id, User user);

    Set<Album> findUserFavorites(User auth);

}