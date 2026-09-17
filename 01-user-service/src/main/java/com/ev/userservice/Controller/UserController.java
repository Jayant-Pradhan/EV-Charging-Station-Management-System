package com.ev.userservice.Controller;

import com.ev.userservice.DTO.*;
import com.ev.userservice.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    /// Create User
    @PostMapping("/create")
    public ResponseEntity<String> userCreate(@RequestBody UserRequest userRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequest));
    }

    /// Get User
    @GetMapping("/get/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /// Get All Users
    @GetMapping("/getall")
    public ResponseEntity<List<UserResponse>> getAllUser(){
        return ResponseEntity.ok(userService.getAllUser());
    }

    /// Update User
    @PutMapping("/update/{id}")
    public UserResponse updateUserRequest(@PathVariable long id , @RequestBody UserUpdateRequest userUpdateRequest){
        return userService.updateUserRequestData(id,userUpdateRequest);
    }

    /// Delete User
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable long id){
        return userService.deleteUser(id);
    }

    /// user Log in
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> userLogin(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.login(loginRequest));
    }
}
