package com.omaden.boat.auth.infrastructure.adapter;

import com.omaden.boat.auth.domain.model.User;
import com.omaden.boat.auth.domain.repository.UserRepository;
import com.omaden.boat.auth.infrastructure.persistence.JpaUserRepository;
import com.omaden.boat.auth.infrastructure.persistence.UserJpaEntity;
import com.omaden.boat.auth.infrastructure.persistence.UserJpaMapper;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaRepository;
    private final UserJpaMapper mapper;

    public UserRepositoryAdapter(JpaUserRepository jpaRepository, UserJpaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toJpa(user);
        UserJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(mapper::toDomain);
    }
}
