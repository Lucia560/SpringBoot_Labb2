package org.example.springboot_labb2.repository;

import org.example.springboot_labb2.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;

public interface UserRepository extends ListCrudRepository <User,Long> {
    List<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"messages"})
    List<User> findAllByRole(String role);
}
