package com.example.QuestionPortalBackend.controllers;

import com.example.QuestionPortalBackend.dto.UserDTO;
import com.example.QuestionPortalBackend.dto.UserToUpdateDTO;
import com.example.QuestionPortalBackend.exceptions.ValidationException;
import com.example.QuestionPortalBackend.services.UsersService;
import com.example.QuestionPortalBackend.services.UsersServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Fetches a specific user by their ID")
    public Map<String, Object> getUser(@PathVariable Integer id) {
        UserDTO userDTO = usersService.findOne(id);
        return Map.of("user", userDTO);
    }

    @GetMapping()
    @Operation(summary = "Get all users", description = "Fetches all users")
    public List<UserDTO> getUsers() {
        return usersService.findAll();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user by their ID")
    public Map<String, String> updateUser(@PathVariable Integer id, @RequestBody @Valid UserToUpdateDTO userToUpdateDTO,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        userToUpdateDTO.setCurrentPassword(userToUpdateDTO.getPassword());

        String jwt = usersService.updateUser(id, userToUpdateDTO);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("jwt-token", jwt);
        responseMap.put("firstName", userToUpdateDTO.getFirstName());

        return responseMap;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes an existing user by their ID")
    public String deleteUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        usersService.deleteUser(id, userDTO.getPassword());
        return "User deleted successfully";
    }
}
