package com.seongyounmin.springsideproject.repository;

import com.seongyounmin.springsideproject.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

}
