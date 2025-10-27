package com.jeremiasAvero.app.auth.infraestructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.jeremiasAvero.app.auth.domain.UserEntity;
import com.jeremiasAvero.app.auth.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SpringUserRepository implements UserRepository{
	private final JpaUserRepository repo;
	
	@Override
	public UserEntity save(UserEntity user) {
		return repo.save(user);
	}

	@Override
	public Optional<UserEntity> findById(Long id) {
		return repo.findById(id);
	}

	@Override
	public Optional<UserEntity> findByEmail(String email) {
		return repo.findByEmail(email);
	}

	@Override
	public boolean existsByEmail(String email) {
		return repo.existsByEmail(email);
	}

}
