package com.example.QuestionPortalBackend.controllers;

import com.example.QuestionPortalBackend.dto.UserDTO;
import com.example.QuestionPortalBackend.dto.UserToUpdateDTO;
import com.example.QuestionPortalBackend.services.UsersService;
import com.example.QuestionPortalBackend.util.UserMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public Map<String, Object> getUser(@PathVariable Integer id) {
        UserDTO userDTO = usersService.findOne(id);
        return Map.of("user", userDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody @Valid UserToUpdateDTO userToUpdateDTO,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        try {
            String jwt = usersService.updateUser(id, userToUpdateDTO);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("jwt-token", jwt);
            responseMap.put("firstName", userToUpdateDTO.getFirstName());

            return ResponseEntity.ok(responseMap);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        try {
            usersService.deleteUser(id, userDTO.getPassword());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
        }
    }
}
