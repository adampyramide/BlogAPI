package io.github.adampyramide.BlogAPI.user;

import io.github.adampyramide.BlogAPI.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepo;
    UserMapper mapper;

    public UserService(UserRepository userRepo, UserMapper mapper) {
        this.userRepo = userRepo;
        this.mapper = mapper;
    }

    public PublicUserDTO getUserById(int id) {
        return mapper.toPublicDTO(
                userRepo.findById(id)
                        .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND)
                )
        );
    }

}
