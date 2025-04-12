package com.music.controller;

import com.music.model.Album;
import com.music.model.Song;
import com.music.model.User;
import com.music.service.album.AlbumService;
import com.music.service.song.SongService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("/fav")
public class FavoritesController {
    private final SongService songService;
    private final AlbumService albumService;

    public FavoritesController(SongService songService, AlbumService albumService) {
        this.songService = songService;
        this.albumService = albumService;
    }

    @GetMapping("/songs")
    public String displayFavSongs(Model model, @AuthenticationPrincipal User user) {
        Set<Song> songs = songService.findUserFavorites(user);

        model.addAttribute("songs", songs);

        return "fav-songs";
    }


    @GetMapping("/albums")
    public String displayFavAlbums(Model model, @AuthenticationPrincipal User user) {
        Set<Album> albums = albumService.findUserFavorites(user);

        model.addAttribute("albums", albums);

        return "fav-albums";
    }
}
