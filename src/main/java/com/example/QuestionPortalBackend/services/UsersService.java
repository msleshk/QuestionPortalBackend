package com.example.QuestionPortalBackend.services;

import com.example.QuestionPortalBackend.dto.UserDTO;
import com.example.QuestionPortalBackend.dto.UserToUpdateDTO;
import com.example.QuestionPortalBackend.exceptions.UserAlreadyExistsException;
import com.example.QuestionPortalBackend.exceptions.UserNotFoundException;
import com.example.QuestionPortalBackend.models.User;
import com.example.QuestionPortalBackend.repositories.UsersRepository;
import com.example.QuestionPortalBackend.util.UserMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final EmailNotificationSenderService emailNotificationSenderService;
    private final UserMapper userMapper;

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder, AuthService authService, EmailNotificationSenderService emailNotificationSenderService, UserMapper userMapper) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
        this.emailNotificationSenderService = emailNotificationSenderService;
        this.userMapper = userMapper;
    }

    public UserDTO findOne(int id) {
        return usersRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));
    }

    @Transactional
    public String updateUser(int id, UserToUpdateDTO userToUpdate) {
        User currentUser = usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found!"));

        currentUser.setFirstName(userToUpdate.getFirstName());
        currentUser.setLastName(userToUpdate.getLastName());
        currentUser.setEmail(userToUpdate.getEmail());
        currentUser.setPhoneNumber(userToUpdate.getPhoneNumber());

        if (userToUpdate.getCurrentPassword() != null && userToUpdate.getNewPassword() != null) {
            if (!passwordEncoder.matches(userToUpdate.getCurrentPassword(), currentUser.getPassword())) {
                throw new IllegalArgumentException("Incorrect password!");
            }
            currentUser.setPassword(passwordEncoder.encode(userToUpdate.getNewPassword()));
        }
        usersRepository.save(currentUser);
        return authService.authenticateUser(userToUpdate.getEmail(), userToUpdate.getNewPassword());
    }

    @Transactional
    public void deleteUser(int id, String password) {
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password!");
        }
        usersRepository.delete(user);
        emailNotificationSenderService.sendNotification(user.getEmail(), "Profile delete confirmation",
                "Dear, " + user.getFirstName() + ", your profile was successfully deleted!");
    }

    public UserDTO getUserByEmail(String email) {
        Optional<User> user = usersRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        return userMapper.toDTO(user.get());
    }

    @Transactional
    public void registerUser(UserDTO userDTO) throws UserAlreadyExistsException {
        User user= userMapper.toEntity(userDTO);
        if (usersRepository.existsUserByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Username with email " + user.getEmail() + " already exists!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
        emailNotificationSenderService.sendNotification(user.getEmail(), "Welcome to our service",
                "Thank you for registering with our service!");
    }

}
