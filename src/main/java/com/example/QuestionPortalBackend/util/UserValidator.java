package com.example.QuestionPortalBackend.util;

import com.example.QuestionPortalBackend.models.User;
import com.example.QuestionPortalBackend.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UsersService usersService;

    @Autowired
    public UserValidator(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        String email = user.getEmail();
        if (email == null || email.trim().isEmpty()){
            errors.rejectValue("email", "email.empty", "Email is required.");
            return;
        }
        User existingUser = usersService.getUserByEmail(email);
        if (existingUser != null){
            errors.rejectValue("email", "email.exist", "User with this email is already exists!");
        }
    }

}
