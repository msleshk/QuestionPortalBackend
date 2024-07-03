package com.example.QuestionPortalBackend.services;

import com.example.QuestionPortalBackend.dto.UserDTO;
import com.example.QuestionPortalBackend.dto.UserToUpdateDTO;

import java.util.List;

public interface UsersService {
    public List<UserDTO> findAll();

    public UserDTO findOne(int id);

    public String updateUser(int id, UserToUpdateDTO userToUpdate);

    public void deleteUser(int id, String password);

    public UserDTO getUserByEmail(String email);

    void registerUser(UserDTO userDTO);
}
