package org.example.springboot_labb2;


import org.example.springboot_labb2.entity.User;
import org.example.springboot_labb2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:mysql:8.3.0:///mydatabase"
})

 class UserPersistenceTests {


    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;
    @Test
    void findUserByUsername() {
        var testuser  = new User();
        testuser.setUsername("Mirabella");
        testuser.setRole("USER");
        testuser.setNameSurname("Patrickson");
        testuser.setEmail("mirabella@example.com");
        testuser.setProfilePicture("https://example.com/image.jpg".getBytes());
        userRepository.save(testuser);
        List<User> resultList = userRepository.findByUsername(testuser.getUsername());

        assertThat(resultList).isNotNull().isNotEmpty();
        User foundUser = resultList.get(0);


        assertThat(foundUser.getUsername()).isEqualTo(testuser.getUsername());
        assertThat(foundUser.getRole()).isEqualTo(testuser.getRole());
        assertThat(foundUser.getNameSurname()).isEqualTo(testuser.getNameSurname());
        assertThat(foundUser.getEmail()).isEqualTo(testuser.getEmail());
        assertThat(foundUser.getProfilePicture()).isEqualTo(testuser.getProfilePicture());


    }

    @Test
     void testFindAllByRole() {
        var adminUser = new User();
        adminUser.setUsername("John");
        adminUser.setRole("ADMIN");
        adminUser.setNameSurname("John Nickel");
        adminUser.setEmail("john@example.com");
        adminUser.setProfilePicture("https://example.com/admin.jpg".getBytes());
        userRepository.save(adminUser);

        var regularUser1 = new User();
        regularUser1.setUsername("Normie");
        regularUser1.setRole("USER");
        regularUser1.setNameSurname("Venn");
        regularUser1.setEmail("normie@example.com");
        regularUser1.setProfilePicture("https://example.com/user1.jpg".getBytes());
        userRepository.save(regularUser1);

        User regularUser2 = new User();
        regularUser2.setUsername("Anna");
        regularUser2.setRole("USER");
        regularUser2.setNameSurname("Smith");
        regularUser2.setEmail("anna@example.com");
        regularUser2.setProfilePicture("https://example.com/user2.jpg".getBytes());
        userRepository.save(regularUser2);


        List<User> foundUsers = userRepository.findAllByRole("USER");


        assertThat(foundUsers).isNotNull().hasSize(2)
                .extracting(User::getUsername).containsExactlyInAnyOrder("Anna", "Normie");
        assertThat(foundUsers).extracting(User::getRole).containsOnly("USER");
    }

}
