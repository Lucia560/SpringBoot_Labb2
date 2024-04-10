package org.example.springboot_labb2.repository;

import org.example.springboot_labb2.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends ListPagingAndSortingRepository <User,Long>, ListCrudRepository <User,Long> {
    List<User> findByUsername(String username);

    @EntityGraph(attributePaths = {"messages"})
    List<User> findAllByRole(String role);

    @Query(value = """
            select * from user where user.userid > ?1 limit ?2
            """, nativeQuery = true)
    List<User> findUserBy(long cursor, int pageSize);

    Optional<User> findByGithubLogin(String githubLogin);
}
