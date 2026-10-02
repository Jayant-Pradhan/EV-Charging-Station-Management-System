package com.ev.userservice.Controller;

import com.ev.userservice.DTO.*;
import com.ev.userservice.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;


    // =========================
    // CREATE USER
    // =========================

    @Test
    void userCreate_shouldReturnCreatedStatus() {

        UserRequest request = new UserRequest();

        when(userService.createUser(request))
                .thenReturn("user data inserted into DB successfully");

        ResponseEntity<String> response =
                userController.userCreate(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertEquals(
                "user data inserted into DB successfully",
                response.getBody()
        );

        verify(userService).createUser(request);
    }


    // =========================
    // GET USER BY ID
    // =========================

    @Test
    void getUserById_shouldReturnUser() {

        UserResponse userResponse = new UserResponse();

        userResponse.setId(1L);
        userResponse.setName("Jayant");
        userResponse.setMail("jayant@gmail.com");

        when(userService.getUserById(1L))
                .thenReturn(userResponse);

        ResponseEntity<UserResponse> response =
                userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());

        assertEquals(1L, response.getBody().getId());
        assertEquals("Jayant", response.getBody().getName());
        assertEquals("jayant@gmail.com", response.getBody().getMail());

        verify(userService).getUserById(1L);
    }


    // =========================
    // GET ALL USERS
    // =========================

    @Test
    void getAllUser_shouldReturnAllUsers() {

        UserResponse user1 = new UserResponse();
        user1.setId(1L);
        user1.setName("Jayant");

        UserResponse user2 = new UserResponse();
        user2.setId(2L);
        user2.setName("Rahul");

        List<UserResponse> users =
                List.of(user1, user2);

        when(userService.getAllUser())
                .thenReturn(users);

        ResponseEntity<List<UserResponse>> response =
                userController.getAllUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());

        assertEquals(2, response.getBody().size());

        assertEquals(
                "Jayant",
                response.getBody().get(0).getName()
        );

        assertEquals(
                "Rahul",
                response.getBody().get(1).getName()
        );

        verify(userService).getAllUser();
    }


    // =========================
    // UPDATE USER
    // =========================

    @Test
    void updateUserRequest_shouldReturnUpdatedUser() {

        UserUpdateRequest request =
                new UserUpdateRequest();

        request.setName("Updated Jayant");
        request.setMail("updated@gmail.com");
        request.setPhone("9999999999");
        request.setVehicleNumber("OD01NEW");

        UserResponse updatedUser =
                new UserResponse();

        updatedUser.setId(1L);
        updatedUser.setName("Updated Jayant");
        updatedUser.setMail("updated@gmail.com");
        updatedUser.setPhone("9999999999");
        updatedUser.setVehicleNumber("OD01NEW");

        when(userService.updateUserRequestData(1L, request))
                .thenReturn(updatedUser);

        UserResponse response =
                userController.updateUserRequest(1L, request);

        assertNotNull(response);

        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                "Updated Jayant",
                response.getName()
        );

        assertEquals(
                "updated@gmail.com",
                response.getMail()
        );

        verify(userService)
                .updateUserRequestData(1L, request);
    }


    // =========================
    // DELETE USER
    // =========================

    @Test
    void deleteUser_shouldReturnSuccessMessage() {

        when(userService.deleteUser(1L))
                .thenReturn("user deleted successfully");

        String response =
                userController.deleteUser(1L);

        assertEquals(
                "user deleted successfully",
                response
        );

        verify(userService)
                .deleteUser(1L);
    }


    // =========================
    // LOGIN
    // =========================

    @Test
    void userLogin_shouldReturnLoginResponse() {

        LoginRequest request =
                new LoginRequest();

        request.setMail("jayant@gmail.com");
        request.setPassword("123456");

        LoginResponse loginResponse =
                new LoginResponse(
                        "log in successfully ",
                        "dummy-jwt-token"
                );

        when(userService.login(request))
                .thenReturn(loginResponse);

        ResponseEntity<LoginResponse> response =
                userController.userLogin(request);

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                "log in successfully ",
                response.getBody().getMessage()
        );

        assertEquals(
                "dummy-jwt-token",
                response.getBody().getToken()
        );

        verify(userService)
                .login(request);
    }
}