package com.music.controller;

import com.music.model.Album;
import com.music.model.Song;
import com.music.model.User;
import com.music.service.album.AlbumService;
import com.music.service.song.SongService;
import com.music.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class HomeController {
    private final SongService songService;
    private final AlbumService albumService;
    private final UserService userService;

    public HomeController(SongService songService, AlbumService albumService, UserService userService) {
        this.songService = songService;
        this.albumService = albumService;
        this.userService = userService;
    }

    @GetMapping
    public String displayHome(Model model, @AuthenticationPrincipal User user) {
        List<Song> songs = songService.findTopTen(user);
        List<Album> albums = albumService.findTopTen(user);
        List<User> artists = userService.findTenArtists();

        if (!songs.isEmpty())
            model.addAttribute("songs", songs);

        if(!albums.isEmpty())
            model.addAttribute("albums", albums);

        if(!artists.isEmpty())
            model.addAttribute("artists", artists);

        return "home";
    }
}
