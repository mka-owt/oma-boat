package com.omaden.boat.auth.domain.repository;

import com.omaden.boat.auth.domain.model.User;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByUsername(String username);
}
