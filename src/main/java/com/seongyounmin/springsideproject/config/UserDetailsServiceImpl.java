package com.seongyounmin.springsideproject.config;

import com.seongyounmin.springsideproject.entity.Account;
import com.seongyounmin.springsideproject.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Account account = accountRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Not Found Account")
        );

        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setAccount(account);

        return userDetails;
    }
}
