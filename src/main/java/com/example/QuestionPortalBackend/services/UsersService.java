package com.example.QuestionPortalBackend.services;

import com.example.QuestionPortalBackend.exception.UserAlreadyExistsException;
import com.example.QuestionPortalBackend.models.User;
import com.example.QuestionPortalBackend.repositories.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void registerUser(User user) throws UserAlreadyExistsException {
        if (usersRepository.existsUserByEmail(user.getEmail())){
            throw new UserAlreadyExistsException("username with email "+user.getEmail()+" already exists!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }

}
