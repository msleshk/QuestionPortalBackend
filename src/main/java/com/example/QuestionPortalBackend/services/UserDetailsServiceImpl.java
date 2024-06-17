package com.example.QuestionPortalBackend.services;

import com.example.QuestionPortalBackend.models.User;
import com.example.QuestionPortalBackend.repositories.UsersRepository;
import com.example.QuestionPortalBackend.security.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsersRepository usersRepository;

    public UserDetailsServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user=usersRepository.findByEmail(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User not found!");
        }
        return new UserDetailsImpl(user.get());
    }
}
