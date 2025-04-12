package com.music.service.user;

import com.music.dto.ArtistDto;
import com.music.dto.RegisterDto;
import com.music.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUserService {

    void saveUser(RegisterDto registerDto);

    void saveArtist(ArtistDto artistDto, MultipartFile profilePicture) throws IOException;

    boolean existsByUsernameOrEmailOrNickname(String username, String email, String nickname);

    User findById(Long id);

    List<User> findTenArtists();

    List<User> findAllArtists();

    void deleteArtist(Long id, User user);

}
