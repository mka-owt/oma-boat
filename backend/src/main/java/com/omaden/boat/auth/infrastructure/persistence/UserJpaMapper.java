package com.omaden.boat.auth.infrastructure.persistence;

import com.omaden.boat.auth.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserJpaMapper {

    public User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return User.reconstitute(
                entity.getId(), entity.getUsername(), entity.getPassword(), entity.getRole());
    }

    public UserJpaEntity toJpa(User user) {
        if (user == null) {
            return null;
        }
        return new UserJpaEntity(
                user.getId(), user.getUsername(), user.getPasswordHash(), user.getRole());
    }
}
