package Repositories;

import Entities.User;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;
import java.util.UUID;

public interface UserRepository extends ListCrudRepository <User,UUID> {
    List<User> findByUsername(String username);
}
