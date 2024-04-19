package org.example.springboot_labb2.repository;

import org.example.springboot_labb2.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByStatusPrivateIsFalse();

    List<Message> findByUserId(Long userId);

    List<Message> findByUserUsername(String username);

}
