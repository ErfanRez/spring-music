package com.music.controller;

import com.music.model.Song;
import com.music.model.User;
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
@RequestMapping("/songs")
public class SongsController {
    private final SongService songService;

    public SongsController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    public String displaySongs(Model model, @AuthenticationPrincipal User user){
        List<Song> songs = songService.findAllSongs(user);

        if(!songs.isEmpty())
            model.addAttribute("songs", songs);

        return "songs";
    }

    @PostMapping("/delete/{id}")
    public String deleteSong(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        songService.deleteSong(id, user);

        redirectAttributes.addFlashAttribute("deleteMessage", "Song deleted successfully");

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");

    }

    @PostMapping("/add-fav/{id}")
    public String addFavorite(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            HttpServletRequest request) {

        songService.addFavorite(id, user);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");

    }
}
