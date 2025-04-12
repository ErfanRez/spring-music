package com.music.controller;


import com.music.dto.AlbumDto;
import com.music.dto.ArtistDto;
import com.music.dto.TrackDto;
import com.music.model.Album;
import com.music.model.Song;
import com.music.model.User;
import com.music.service.S3Service;
import com.music.service.album.AlbumService;
import com.music.service.song.SongService;
import com.music.service.user.UserService;
import com.music.utils.Roles;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.music.utils.Constants;

import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final AlbumService albumService;
    private final SongService songService;
    private final UserService userService;

    public DashboardController(AlbumService albumService, SongService songService, UserService userService) {
        this.albumService = albumService;
        this.songService = songService;
        this.userService = userService;
    }

    @GetMapping()
    public String displayDashboard(@AuthenticationPrincipal User user, Model model){
        if (user != null && user.getRole() == Roles.ARTIST) {
            List<Song> tracks = songService.findSingleTracksByArtist(user);
            List<Album> albums = albumService.findByArtist(user);

            model.addAttribute("tracks", tracks);
            model.addAttribute("albums", albums);
            model.addAttribute("currentUser", user);
        }


        return "dashboard";
    }

    @GetMapping("/new-track")
    public String showNewTrackPage(Model model) {
        model.addAttribute("track", new TrackDto());
        return "new-track";
    }

    @PostMapping("/new-track")
    public String handleNewTrackForm(
            @RequestParam("audioFile") MultipartFile audioFile,
            @RequestParam("cover") MultipartFile cover,
            @Valid @ModelAttribute("track") TrackDto trackDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal User user
    ) {
        if (bindingResult.hasErrors()) {
            return "new-track";
        }

        if (audioFile.isEmpty()) {
            model.addAttribute("audioError", "Please select track file to upload.");
            return "new-track";
        }

        if (cover.isEmpty()) {
            model.addAttribute("coverError", "Please select track cover to upload.");
            return "new-track";
        }

        try {
            songService.saveTrackWithCover(trackDto, audioFile, cover, user);
            redirectAttributes.addFlashAttribute("success", "Track saved successfully!");
            return "redirect:/dashboard";

        } catch (IOException e) {

            log.error("Failed to upload files for track: {}", trackDto.getTitle(), e);
            redirectAttributes.addFlashAttribute("error", "An error occurred while uploading the track.");
            return "redirect:/dashboard/new-track";
        }


    }

    @GetMapping("/new-album")
    public String displayNewAlbum(Model model) {
        model.addAttribute("album", new AlbumDto());
        return "new-album";
    }

    @PostMapping("/new-album")
    public String handleNewAlbumForm(
            @RequestParam("cover") MultipartFile cover,
            @Valid @ModelAttribute("album") AlbumDto albumDto,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ) {
        if (bindingResult.hasErrors()) {
            return "new-album";
        }

        if (cover.isEmpty()) {
            model.addAttribute("coverError", "Please select track cover to upload.");
            return "new-album";
        }

        Path filePath = null;

        try {
            // Ensure the directory exists
            Path directoryPath = Paths.get(Constants.ALBUM_LOCAL_DIR);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            // Save the image to the upload directory
            String fileName = cover.getOriginalFilename();
            filePath = Paths.get(Constants.ALBUM_LOCAL_DIR + fileName);
            Files.write(filePath, cover.getBytes());


            // Store the relative URL in the session
            String relativeUrl = "/assets/images/covers/" + fileName;
            albumDto.setCoverUrl(relativeUrl);
            log.info("Image saved at: {}", filePath.toAbsolutePath());

        } catch (IOException e) {
            log.error("Error saving file: {}", e.getMessage(), e);
        }

        session.setAttribute("album", albumDto);
        session.setAttribute("coverPath",  filePath);

        return "redirect:/dashboard/album/add-songs";
    }

    @GetMapping("/album/add-songs")
    public String displayAddSongForm() {

        return "add-songs";
    }

    @PostMapping("/album/add-songs")
    public synchronized String handleAddSongsForm(
            @RequestParam("songTitles") List<String> songTitles,
            @RequestParam("songFiles") List<MultipartFile> songFiles,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal User user
    ) {

        AlbumDto albumDto = (AlbumDto) session.getAttribute("album");
        if (albumDto == null) {
            redirectAttributes.addFlashAttribute("error", "No album found. Please create an album first.");
            return "redirect:/dashboard/new-album";
        }

        if (songTitles.size() != songFiles.size()) {
            redirectAttributes.addFlashAttribute("error", "Number of song titles and files do not match.");
            return "redirect:/dashboard/album/add-songs";
        }

        try {
            albumService.saveAlbumWithSongsAndCover(albumDto, songTitles, songFiles, user);
            session.removeAttribute("album");
            session.removeAttribute("coverPath");
            redirectAttributes.addFlashAttribute("success", "Album and songs saved successfully!");
            return "redirect:/dashboard";
        } catch (IOException e) {
            log.error("Failed to upload files for album: {}", albumDto.getTitle(), e);
            redirectAttributes.addFlashAttribute("error", "An error occurred while uploading the files.");
            return "redirect:/dashboard/new-album";
        }
    }

//    @PostMapping("/song/delete/{songId}")
//    public String deleteSong(
//            @PathVariable Long songId,
//            RedirectAttributes redirectAttributes
//    ) {
//        // Find the song in the database
//        Song song = songRepository.findById(songId)
//                .orElseThrow(() -> new RuntimeException("Song not found"));
//
//        try {
//            // Delete the song file from S3
//            s3Service.deleteFile(song.getS3Key());
//
//            // Delete the song record from the database
//            songRepository.delete(song);
//
//            redirectAttributes.addFlashAttribute("success", "Song deleted successfully!");
//        } catch (Exception e) {
//            log.error("Error deleting song: " + e.getMessage(), e);
//            redirectAttributes.addFlashAttribute("error", "An error occurred while deleting the song.");
//        }
//
//        return "redirect:/dashboard"; // Redirect to the dashboard or any other page
//    }

    @GetMapping("/new-artist")
    public String showArtistRegisterForm(Model model) {
        model.addAttribute("artist", new ArtistDto());
        return "new-artist";
    }

    @PostMapping("/new-artist")
    public String registerArtist(
            @Valid @ModelAttribute("artist") ArtistDto artistDto,
            BindingResult bindingResult,
            @RequestParam(value = "profilePicture") MultipartFile profilePicture,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes,
            Model model
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            return "new-artist";
        }

        if (!artistDto.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "new-artist";
        }

        if (profilePicture.isEmpty()) {
            model.addAttribute("error", "Please upload a profile picture");
            return "new-artist";
        }


        if (userService.existsByUsernameOrEmailOrNickname(artistDto.getUsername(), artistDto.getEmail(), artistDto.getNickname())) {
            model.addAttribute("error", "Artist already exists!");
            return "new-artist";
        }

        userService.saveArtist(artistDto, profilePicture);

        redirectAttributes.addFlashAttribute("success", "Artist registration successful");
        return "redirect:/dashboard";
    }

}