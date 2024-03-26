package org.example.springboot_labb2.repository;

import org.example.springboot_labb2.entity.Message;
import org.springframework.data.repository.ListCrudRepository;

public interface MessageRepository extends ListCrudRepository <Message, Long> {
}
