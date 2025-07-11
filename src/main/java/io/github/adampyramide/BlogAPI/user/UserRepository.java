package io.github.adampyramide.BlogAPI.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(long id);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

}
