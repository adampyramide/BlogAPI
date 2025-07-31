package io.github.adampyramide.BlogAPI.security;

import io.github.adampyramide.BlogAPI.error.ApiException;
import io.github.adampyramide.BlogAPI.user.User;
import io.github.adampyramide.BlogAPI.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "USER_NOT_FOUND",
                        "User not found: username='%s'".formatted(username)
                ));
        return new CustomUserDetails(user);
    }
}