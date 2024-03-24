package org.example.springboot_labb2;

import org.springframework.data.repository.ListCrudRepository;
import java.util.List;
import java.util.UUID;

public interface UserRepository extends ListCrudRepository <User,UUID> {
    List<User> findByUsername(String username);
}
