package com.trifler.chat_application_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trifler.chat_application_backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<User> findAllByIdNot(Long id);

    List<User> findByIdNotAndUsernameContainingIgnoreCase(
            Long id, String username);

    List<User> findByIdNotAndEmailContainingIgnoreCase(
            Long id, String email);

}
