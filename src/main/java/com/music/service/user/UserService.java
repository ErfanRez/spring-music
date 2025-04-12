package com.music.service.user;

import com.music.dto.ArtistDto;
import com.music.dto.RegisterDto;
import com.music.exception.AccessDeniedException;
import com.music.exception.ArtistNotFoundException;
import com.music.exception.DuplicateUsernameOrEmailException;
import com.music.exception.SongNotFoundException;
import com.music.model.ArtistPic;
import com.music.model.Cover;
import com.music.model.Song;
import com.music.model.User;
import com.music.repository.UserRepository;
import com.music.service.S3Service;
import com.music.utils.Constants;
import com.music.utils.Roles;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, S3Service s3Service) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Service = s3Service;
    }

    @Transactional
    public void saveUser(RegisterDto registerDto) {
        if(userRepository.existsByUsernameOrEmail(registerDto.getUsername(), registerDto.getEmail())) {
            throw new DuplicateUsernameOrEmailException("User already exists!");
        }
        User user = RegisterDto.dtoToUser(registerDto, passwordEncoder);
        userRepository.save(user);

    }

    @Transactional
    public void saveArtist(ArtistDto artistDto, MultipartFile profilePic) throws IOException {
        String picKey = Constants.PROFILES_PREFIX + System.currentTimeMillis() + "_" + profilePic.getOriginalFilename();
        String picUrl = s3Service.uploadFile(picKey, profilePic.getBytes());

        ArtistPic pic = new ArtistPic();
        pic.setKey(picKey);
        pic.setUrl(picUrl);
        User artist = ArtistDto.dtoToArtist(artistDto, passwordEncoder);
        artist.setProfilePic(pic);

        userRepository.save(artist);

    }

    @Override
    public boolean existsByUsernameOrEmailOrNickname(String username, String email, String nickname) {
        return userRepository.existsByUsernameOrEmailOrNickname(username, email, nickname);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ArtistNotFoundException("Artist with ID " + id + " not found!"));
    }

    @Override
    public List<User> findTenArtists() {
        return userRepository.findTop10ByRole(Roles.ARTIST);
    }

    @Override
    public List<User> findAllArtists() {
        return userRepository.findAllByRole(Roles.ARTIST);
    }

    @Override
    @Transactional
    public void deleteArtist(Long id, User user) {
        User artist = userRepository.findById(id)
                .orElseThrow(() -> new ArtistNotFoundException("Artist with id " + id + " not found"));

        if (!user.getRole().equals(Roles.ADMIN)) {
            throw new AccessDeniedException("You don't have permission to delete this song");
        }


        if (artist.getProfilePic() != null) {
            s3Service.deleteFile(artist.getProfilePic().getKey());
        }

        userRepository.delete(artist);
    }

}
