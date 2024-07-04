package com.example.QuestionPortalBackend.services;

import com.example.QuestionPortalBackend.dto.UserDTO;
import com.example.QuestionPortalBackend.dto.UserToUpdateDTO;
import com.example.QuestionPortalBackend.exceptions.UserAlreadyExistsException;
import com.example.QuestionPortalBackend.exceptions.UserNotFoundException;
import com.example.QuestionPortalBackend.models.User;
import com.example.QuestionPortalBackend.repositories.UsersRepository;
import com.example.QuestionPortalBackend.util.UserMapperImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final EmailNotificationSenderService emailNotificationSenderService;
    private final UserMapperImpl userMapper;

    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder, AuthService authService, EmailNotificationSenderService emailNotificationSenderService, UserMapperImpl userMapper) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
        this.emailNotificationSenderService = emailNotificationSenderService;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> findAll() {
        return usersRepository.findAll()
                .stream().map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findOne(int id) {
        return usersRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));
    }

    @Override
    @Transactional
    public String updateUser(int id, UserToUpdateDTO userToUpdate) {
        User currentUser = usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found!"));

        currentUser.setFirstName(userToUpdate.getFirstName());
        currentUser.setLastName(userToUpdate.getLastName());
        if (!Objects.equals(currentUser.getEmail(), userToUpdate.getEmail())) {
            if (usersRepository.findByEmail(userToUpdate.getEmail()).isPresent()) {
                throw new UserAlreadyExistsException("User with this username already exists");
            }
        }
        currentUser.setEmail(userToUpdate.getEmail());
        currentUser.setPhoneNumber(userToUpdate.getPhoneNumber());

        if (userToUpdate.getCurrentPassword() != null && !userToUpdate.getNewPassword().isBlank()) {
            if (!passwordEncoder.matches(userToUpdate.getCurrentPassword(), currentUser.getPassword())) {
                throw new IllegalArgumentException("Incorrect password!");
            }
            currentUser.setPassword(passwordEncoder.encode(userToUpdate.getNewPassword()));
            usersRepository.save(currentUser);
            return authService.authenticateUser(userToUpdate.getEmail(), userToUpdate.getNewPassword());
        }
        usersRepository.save(currentUser);
        userToUpdate.setNewPassword(userToUpdate.getPassword());
        return authService.authenticateUser(userToUpdate.getEmail(), userToUpdate.getPassword());
    }

    @Override
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

    @Override
    public UserDTO getUserByEmail(String email) {
        Optional<User> user = usersRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        return userMapper.toDTO(user.get());
    }

    @Override
    @Transactional
    public void registerUser(UserDTO userDTO) throws UserAlreadyExistsException {
        User user = userMapper.toEntity(userDTO);
        if (usersRepository.existsUserByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Username with email " + user.getEmail() + " already exists!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
        emailNotificationSenderService.sendNotification(user.getEmail(), "Welcome to our service",
                "Thank you for registering with our service!");
    }

}
