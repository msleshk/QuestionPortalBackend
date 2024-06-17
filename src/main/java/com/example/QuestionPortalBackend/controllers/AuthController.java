package com.example.QuestionPortalBackend.controllers;

import com.example.QuestionPortalBackend.DTO.LoginRequest;
import com.example.QuestionPortalBackend.DTO.UserDTO;
import com.example.QuestionPortalBackend.exception.UserAlreadyExistsException;
import com.example.QuestionPortalBackend.models.User;
import com.example.QuestionPortalBackend.security.JWTUtil;
import com.example.QuestionPortalBackend.security.UserDetailsImpl;
import com.example.QuestionPortalBackend.services.AuthService;
import com.example.QuestionPortalBackend.services.UsersService;
import com.example.QuestionPortalBackend.util.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JWTUtil jwtUtil;
    private final UserMapper userMapper;
    private final UsersService usersService;
    private final AuthService authService;

    public AuthController(JWTUtil jwtUtil, UserMapper userMapper, UsersService usersService, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.usersService = usersService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody LoginRequest loginRequest){
        String jwt=authService.authenticateUser(loginRequest);
        return Map.of("jwt-token", jwt);
    }
    @GetMapping("/userInfo")
    public String getUserInfo(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUsername();
    }
    @PostMapping("/registration")
    public ResponseEntity<String> performRegistration(@RequestBody UserDTO userDTO){
        User user= userMapper.toEntity(userDTO);
        try{
            usersService.registerUser(user);
            return ResponseEntity.ok("User registered successfully!");
        } catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        //TODO валидация
    }


}
