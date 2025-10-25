package com.jeremiasAvero.app.auth.domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    UserEntity save(UserEntity user);
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
