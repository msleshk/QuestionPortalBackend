package com.example.QuestionPortalBackend.controllers;

import com.example.QuestionPortalBackend.dto.LoginRequest;
import com.example.QuestionPortalBackend.dto.UserDTO;
import com.example.QuestionPortalBackend.exceptions.ValidationException;
import com.example.QuestionPortalBackend.security.UserDetailsImpl;
import com.example.QuestionPortalBackend.services.AuthService;
import com.example.QuestionPortalBackend.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final UsersService usersService;
    private final AuthService authService;

    public AuthController(UsersService usersService, AuthService authService) {
        this.usersService = usersService;
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Perform login", description = "Performs login and responses with map of jwt-token and users firstName, id, email")
    public Map<String, String> performLogin(@RequestBody LoginRequest loginRequest) {
        String jwt = authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        UserDTO user = usersService.getUserByEmail(loginRequest.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("jwt-token", jwt);
        response.put("firstName", user.getFirstName());
        response.put("id", user.getId().toString());
        response.put("email", user.getEmail());
        return response;
    }

    @GetMapping("/userInfo")
    public String getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    @PostMapping("/registration")
    @Operation(summary = "User Registration", description = "Registers a new user with the provided details")
    public String performRegistration(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }

        usersService.registerUser(userDTO);
        return "User registered successfully!";
    }

}
