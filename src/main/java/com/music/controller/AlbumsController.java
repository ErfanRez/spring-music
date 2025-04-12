package com.music.controller;

import com.music.model.Album;
import com.music.model.Song;
import com.music.model.User;
import com.music.service.album.AlbumService;
import com.music.service.song.SongService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/albums")
public class AlbumsController {
    private final AlbumService albumService;
    private final SongService songService;

    public AlbumsController(AlbumService albumService, SongService songService) {
        this.albumService = albumService;
        this.songService = songService;
    }

    @GetMapping
    public String displayAlbums(Model model, @AuthenticationPrincipal User user) {
        List<Album> albums = albumService.findAllAlbums(user);

        if (!albums.isEmpty())
            model.addAttribute("albums", albums);


        return "albums";
    }

    @GetMapping("/{id}")
    public String displaySingleAlbum(@PathVariable Long id, Model model, @AuthenticationPrincipal User user){
        Album album = albumService.findById(id, user);
        model.addAttribute("album", album);

        List<Song> songs = songService.findByAlbum(album, user);
        if (!songs.isEmpty())
            model.addAttribute("songs", songs);

        return "album-details";
    }

    @PostMapping("/delete/{id}")
    public String deleteAlbum(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes) {

        albumService.deleteAlbum(id, user);

        redirectAttributes.addFlashAttribute("deleteMessage", "Album deleted successfully");

        return "redirect:/";

    }

    @PostMapping("/add-fav/{id}")
    public String addFavorite(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            HttpServletRequest request) {

        albumService.addFavorite(id, user);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

}
