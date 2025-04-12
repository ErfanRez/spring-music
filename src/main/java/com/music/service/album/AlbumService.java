package com.music.service.album;

import com.music.exception.AccessDeniedException;
import com.music.exception.AlbumNotFoundException;
import com.music.exception.SongNotFoundException;
import com.music.model.User;
import com.music.repository.SongRepository;
import com.music.repository.UserRepository;
import com.music.utils.Constants;
import com.music.dto.AlbumDto;
import com.music.model.Album;
import com.music.model.Cover;
import com.music.model.Song;
import com.music.repository.AlbumRepository;
import com.music.service.S3Service;
import com.music.utils.Roles;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static com.music.utils.Utility.getDuration;

@Service
public class AlbumService implements IAlbumService {
    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final HttpSession session;
    private final SongRepository songRepository;

    public AlbumService(AlbumRepository albumRepository, UserRepository userRepository, S3Service s3Service, HttpSession session, SongRepository songRepository) {
        this.albumRepository = albumRepository;
        this.userRepository = userRepository;
        this.s3Service = s3Service;
        this.session = session;
        this.songRepository = songRepository;
    }

    @Override
    @Transactional
    public void saveAlbumWithSongsAndCover(AlbumDto albumDto, List<String> songTitles, List<MultipartFile> songFiles, User auth) throws IOException {
        User user = userRepository.findByUsername(auth.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User with username:" + auth.getUsername() + " not found"));

        String localCoverPath = session.getAttribute("coverPath").toString();

        Cover cover = new Cover();
        if (localCoverPath != null) {
            Path filePath = Paths.get(localCoverPath);
            byte[] coverBytes = Files.readAllBytes(filePath);

            String coverKey = Constants.COVERS_PREFIX + System.currentTimeMillis() + "_" + filePath.getFileName().toString();
            String coverUrl = s3Service.uploadFile(coverKey, coverBytes);

            cover.setKey(coverKey);
            cover.setUrl(coverUrl);

            // Delete the locally saved cover file
            Files.deleteIfExists(filePath);
        }

        Album album = AlbumDto.DtoToAlbumMapper(albumDto);
        album.setCover(cover);
        album.setArtist(user);

        for (int i = 0; i < songTitles.size(); i++) {
            String songTitle = songTitles.get(i);
            MultipartFile songFile = songFiles.get(i);

            String songKey = Constants.SONGS_PREFIX + System.currentTimeMillis() + "_" + songFile.getOriginalFilename();
            String songUrl = s3Service.uploadFile(songKey, songFile.getBytes());

            File tempFile = File.createTempFile("uploaded", ".mp3");
            songFile.transferTo(tempFile);

            String duration = getDuration(tempFile);

            // Delete the temporary file
            tempFile.delete();

            Song song = new Song();
            song.setTitle(songTitle);
            song.setAudioKey(songKey);
            song.setAudioUrl(songUrl);
            song.setGenre(album.getGenre());
            song.setCover(cover);
            song.setArtist(user);
            song.setDuration(duration);

            album.addSong(song);
        }

        albumRepository.save(album);
    }

    @Override
    @Transactional
    public Album findById(Long id, User auth) {
        Album album = albumRepository.findById(id).orElseThrow(() -> new AlbumNotFoundException("Album with ID: " + id + " Not Found!"));

        if(auth != null){
            User user = userRepository.findByUsername(auth.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Set<Album> favoriteAlbums = user.getFavoriteAlbums();

            album.setFavorite(favoriteAlbums.contains(album));
        }


        return album;
    }

    @Override
    @Transactional
    public List<Album> findByArtist(User artist) {
        List<Album> albums = albumRepository.findByArtist(artist);

        modifyUserFavorites(artist, albums);

        return albums;
    }

    private void modifyUserFavorites(User auth, Collection<Album> albums){
        if(auth != null){
            User user = userRepository.findByUsername(auth.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Set<Album> favoriteAlbums = user.getFavoriteAlbums();

            for (Album album : albums) {
                album.setFavorite(favoriteAlbums.contains(album));
            }

        }
    }

    @Override
    @Transactional
    public List<Album> findAllAlbums(User auth) {
        List<Album> albums = albumRepository.findAllByLikeCountDesc();

        modifyUserFavorites(auth, albums);

        return albums;
    }

    @Override
    @Transactional
    public List<Album> findTopTen(User auth){
        List<Album> albums = albumRepository.findTopTen();

        modifyUserFavorites(auth, albums);

        return albums;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteAlbum(Long id, User user) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id " + id + " not found"));


        if (!user.getRole().equals(Roles.ADMIN) &&
                !album.getArtist().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to delete this album");
        }

        List<Song> songs = songRepository.findByAlbumOrderByCreatedAt(album);

        songs.forEach(song -> {
            songRepository.delete(song);
            s3Service.deleteFile(song.getAudioKey());
        });

        albumRepository.delete(album);

        if (album.getCover() != null && album.getCover().getKey() != null) {
            s3Service.deleteFile(album.getCover().getKey());
        }
    }

    @Override
    @Transactional
    public void addFavorite(Long id, User auth) {
        User user = userRepository.findByUsername(auth.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User with username:" + auth.getUsername() + " not found"));

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("album with id " + id + " not found"));


        if (user.getFavoriteAlbums().contains(album)) {
            user.removeFavoriteAlbum(album);
            if(album.getLikeCount() > 0)
                album.setLikeCount(album.getLikeCount() - 1);
        } else {
            user.addFavoriteAlbum(album);
            album.setLikeCount(album.getLikeCount() + 1);
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public Set<Album> findUserFavorites(User auth) {
        User user = userRepository.findByUsername(auth.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User with username:" + auth.getUsername() + " not found"));

        Hibernate.initialize(user.getFavoriteAlbums());

        Set<Album> favoriteAlbums = user.getFavoriteAlbums();

        modifyUserFavorites(auth, favoriteAlbums);

        return favoriteAlbums;
    }
}