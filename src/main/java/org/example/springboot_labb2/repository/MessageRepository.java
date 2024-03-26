package org.example.springboot_labb2.repository;

import org.example.springboot_labb2.entity.Message;
import org.springframework.data.repository.ListCrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface MessageRepository extends ListCrudRepository <Message, Long> {
    List<Message> listOfMessages= new ArrayList<>();
}
