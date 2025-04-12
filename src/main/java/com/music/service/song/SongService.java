package com.music.service.song;

import com.music.exception.AccessDeniedException;
import com.music.exception.SongNotFoundException;
import com.music.exception.UserNotFoundException;
import com.music.model.Album;
import com.music.model.User;
import com.music.repository.UserRepository;
import com.music.utils.Constants;
import com.music.dto.TrackDto;
import com.music.model.Cover;
import com.music.model.Song;
import com.music.repository.SongRepository;
import com.music.service.S3Service;
import com.music.utils.Roles;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.music.utils.Utility.getDuration;

@Service
public class SongService implements ISongService {
    private final SongRepository songRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;

    public SongService(SongRepository songRepository, S3Service s3Service, UserRepository userRepository) {
        this.songRepository = songRepository;
        this.s3Service = s3Service;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void saveTrackWithCover(TrackDto trackDto, MultipartFile audioFile, MultipartFile coverFile, User auth) throws IOException {
        User user = userRepository.findByUsername(auth.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User with username:" + auth.getUsername() + " not found"));

        String audioKey = Constants.TRACKS_PREFIX + System.currentTimeMillis() + "_" + audioFile.getOriginalFilename();
        String audioUrl = s3Service.uploadFile(audioKey, audioFile.getBytes());

        String coverKey = Constants.COVERS_PREFIX + System.currentTimeMillis() + "_" + coverFile.getOriginalFilename();
        String coverUrl = s3Service.uploadFile(coverKey, coverFile.getBytes());

        Cover cover = new Cover();
        cover.setKey(coverKey);
        cover.setUrl(coverUrl);


        File tempFile = File.createTempFile("uploaded", ".mp3");
        audioFile.transferTo(tempFile);

        String duration = getDuration(tempFile);

        // Delete the temporary file
        tempFile.delete();

        Song song = TrackDto.toSongMapper(trackDto, audioKey, audioUrl);
        song.setCover(cover);
        song.setArtist(user);
        song.setDuration(duration);
        songRepository.save(song);
    }

    private void modifyUserFavorites(User auth, Collection<Song> songs){
        if(auth != null){
            User user = userRepository.findByUsername(auth.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Set<Song> favoriteSongs = user.getFavoriteSongs();

            for (Song song : songs) {
                song.setFavorite(favoriteSongs.contains(song));
            }

        }
    }

    @Override
    @Transactional
    public List<Song> findAllSongs(User auth) {
        List<Song> songs = songRepository.findAllOrderByLikeCountDesc();

        modifyUserFavorites(auth, songs);

        return songs;
    }

    @Override
    @Transactional
    public List<Song> findTopTen(User auth) {
        List<Song> songs = songRepository.findTopTen();

        modifyUserFavorites(auth, songs);

        return songs;
    }

    @Override
    @Transactional
    public List<Song> findSingleTracksByArtist(User artist) {
        List<Song> tracks = songRepository.findSingleTracksByArtist(artist);
        User user = userRepository.findByUsername(artist.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<Song> favoriteSongs = user.getFavoriteSongs();

        for (Song song : tracks) {
            song.setFavorite(favoriteSongs.contains(song));
        }

        return tracks;
    }

    @Override
    @Transactional
    public List<Song> findByAlbum(Album album, User auth) {
        List<Song> songs = songRepository.findByAlbumOrderByCreatedAt(album);

        modifyUserFavorites(auth, songs);

        return songs;
    }

    @Override
    @Transactional
    public void deleteSong(Long id, User user) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with id " + id + " not found"));

        // Check if user is admin or the song's artist
        if (!user.getRole().equals(Roles.ADMIN) &&
                !song.getArtist().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to delete this song");
        }


        s3Service.deleteFile(song.getAudioKey());
        if (song.getCover() != null) {
            s3Service.deleteFile(song.getCover().getKey());
        }


        songRepository.delete(song);
    }

    @Override
    @Transactional
    public void addFavorite(Long id, User auth) {
        User user = userRepository.findByUsername(auth.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User with username:" + auth.getUsername() + " not found"));

        Song song = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("song with id " + id + " not found"));


        if (user.getFavoriteSongs().contains(song)) {
            user.removeFavoriteSong(song);
            if(song.getLikeCount() > 0)
                song.setLikeCount(song.getLikeCount() - 1);
        } else {
            user.addFavoriteSong(song);
            song.setLikeCount(song.getLikeCount() + 1);
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public Set<Song> findUserFavorites(User auth) {
        User user = userRepository.findByUsername(auth.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User with username:" + auth.getUsername() + " not found"));

        Hibernate.initialize(user.getFavoriteSongs());

        Set<Song> favoriteSongs = user.getFavoriteSongs();

        modifyUserFavorites(auth, favoriteSongs);

        return favoriteSongs;
    }
}