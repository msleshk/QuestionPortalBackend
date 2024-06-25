package com.example.QuestionPortalBackend.controllers;

import com.example.QuestionPortalBackend.dto.LoginRequest;
import com.example.QuestionPortalBackend.dto.UserDTO;
import com.example.QuestionPortalBackend.exceptions.UserAlreadyExistsException;
import com.example.QuestionPortalBackend.models.User;
import com.example.QuestionPortalBackend.security.JWTUtil;
import com.example.QuestionPortalBackend.security.UserDetailsImpl;
import com.example.QuestionPortalBackend.services.AuthService;
import com.example.QuestionPortalBackend.services.UsersService;
import com.example.QuestionPortalBackend.util.UserMapper;
import com.example.QuestionPortalBackend.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final JWTUtil jwtUtil;
    private final UserMapper userMapper;
    private final UsersService usersService;
    private final AuthService authService;
    private final UserValidator userValidator;

    public AuthController(JWTUtil jwtUtil, UserMapper userMapper, UsersService usersService, AuthService authService, UserValidator userValidator) {
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.usersService = usersService;
        this.authService = authService;
        this.userValidator = userValidator;
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody LoginRequest loginRequest){
        String jwt=authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        User user=usersService.getUserByEmail(loginRequest.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("jwt-token", jwt);
        response.put("firstName", user.getFirstName());
        response.put("id", user.getId().toString());
        return response;
    }
    @GetMapping("/userInfo")
    public String getUserInfo(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUsername();
    }
    @PostMapping("/registration")
    public ResponseEntity<?> performRegistration(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult){
        User user= userMapper.toEntity(userDTO);
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        try{
            usersService.registerUser(user);
            return ResponseEntity.ok("User registered successfully!");
        } catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
