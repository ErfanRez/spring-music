package com.music.controller;

import com.music.model.Album;
import com.music.model.Song;
import com.music.model.User;
import com.music.service.album.AlbumService;
import com.music.service.song.SongService;
import com.music.service.user.UserService;
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
@RequestMapping("/artists")
public class ArtistsController {
    private final UserService userService;
    private final SongService songService;

    public ArtistsController(UserService userService, SongService songService) {
        this.userService = userService;
        this.songService = songService;
    }

    @GetMapping
    public String displayArtists(Model model){
        List<User> artists = userService.findAllArtists();

        if(!artists.isEmpty())
            model.addAttribute("artists", artists);

        return "artists";
    }

    @GetMapping("/{id}")
    public String disPlaySingleArtist(Model model, @PathVariable Long id){
        User artist = userService.findById(id);
        model.addAttribute("artist", artist);

        List<Song> tracks = songService.findSingleTracksByArtist(artist);
        if(!tracks.isEmpty())
            model.addAttribute("tracks", tracks);

        return "artist-details";
    }

    @PostMapping("/delete/{id}")
    public String deleteArtist(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        userService.deleteArtist(id, user);

        redirectAttributes.addFlashAttribute("deleteMessage", "Artist deleted successfully");

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");

    }
}
