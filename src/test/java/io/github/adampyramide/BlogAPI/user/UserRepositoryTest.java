package io.github.adampyramide.BlogAPI.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_FindById_ReturnUser() {

        User user = GetTestUser("1");

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();

    }

    @Test
    public void UserRepository_GetAll_ReturnAllUsers() {
        userRepository.save(GetTestUser("1"));
        userRepository.save(GetTestUser("2"));

        List<User> fetchedUsers = userRepository.findAll();

        for (User user : fetchedUsers) {
            System.out.println(user.getUsername());
        }

        Assertions.assertThat(fetchedUsers).isNotNull();

    }

    private User GetTestUser(String name) {
        return User.builder()
                .username("testUser " + name)
                .password("password")
                .build();
    }


}
