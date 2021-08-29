package com.so.jpa.primary.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.so.jpa.primary.entity.User;


@Repository
@Transactional(rollbackFor = Exception.class)
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailIgnoreCase(String email);

    @Transactional
    @Modifying
    @Query(value = "update tb_users set password =?1 where user_id =?2", nativeQuery = true)
    void updateUserPassword(String password, Long userId);

    Optional<User> findByUsername(String username);
}