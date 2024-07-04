package com.example.QuestionPortalBackend.services;

import com.example.QuestionPortalBackend.dto.UserDTO;
import com.example.QuestionPortalBackend.dto.UserToUpdateDTO;

import java.util.List;

public interface UsersService {
    List<UserDTO> findAll();

    UserDTO findOne(int id);

    String updateUser(int id, UserToUpdateDTO userToUpdate);

    void deleteUser(int id, String password);

    UserDTO getUserByEmail(String email);

    void registerUser(UserDTO userDTO);
}
