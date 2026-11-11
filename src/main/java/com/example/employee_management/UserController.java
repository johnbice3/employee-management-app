package com.example.employee_management;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUserByRequest(@RequestBody @Valid CreateUserRequest request){
        User user = userService.createUser(request.getCompanyId(), request.getUserName(), request.getPassword(), request.getEmail());
        return new UserResponse(user.getId(), user.getUserName(), user.getEmail());
    }
}
