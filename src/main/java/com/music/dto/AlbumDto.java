package com.music.dto;

import com.music.utils.Genres;
import com.music.model.Album;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AlbumDto {
    @NotBlank(message = "Album name is required")
    private String title;

    @NotNull(message = "Release date is required")
    @FutureOrPresent(message = "Release date can not be in the past")
    private LocalDate releaseDate;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number")
    private Double price;

    @NotNull(message = "Genre is required")
    private Genres genre;

    private String coverUrl;

    public static Album DtoToAlbumMapper(AlbumDto albumDto){
        Album album = new Album();
        album.setTitle(albumDto.getTitle());
        album.setReleaseDate(albumDto.getReleaseDate());
        album.setPrice(albumDto.getPrice());
        album.setGenre(albumDto.getGenre());
        return album;
    }
}
