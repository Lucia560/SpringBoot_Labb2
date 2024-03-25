package Repositories;

import Entities.Message;
import org.springframework.data.repository.ListCrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface MessageRepository extends ListCrudRepository <Message, Long> {
    List<Message> listOfMessages= new ArrayList<>();
}
