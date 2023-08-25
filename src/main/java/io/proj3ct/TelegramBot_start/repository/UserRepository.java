package io.proj3ct.TelegramBot_start.repository;

import io.proj3ct.TelegramBot_start.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends ListCrudRepository<User, Long> {
}
