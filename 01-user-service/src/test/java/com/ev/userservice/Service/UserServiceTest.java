package com.ev.userservice.Service;

import com.ev.userservice.DTO.*;
import com.ev.userservice.Entity.User;
import com.ev.userservice.Exception.UserNotFoundException;
import com.ev.userservice.Kafka.UserEventProducer;
import com.ev.userservice.Repository.UserRepository;
import com.ev.userservice.Security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserEventProducer userEventProducer;

    @InjectMocks
    private UserService userService;


    // =========================
    // CREATE USER
    // =========================

    @Test
    void createUser_shouldCreateUserSuccessfully() {

        UserRequest request = new UserRequest();
        request.setName("Jayant");
        request.setMail("jayant@gmail.com");
        request.setPassword("123456");
        request.setPhone("9876543210");
        request.setVehicleNumber("OD01AB1234");

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Jayant");
        savedUser.setMail("jayant@gmail.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setPhone("9876543210");
        savedUser.setRole("USER");
        savedUser.setVehicleNumber("OD01AB1234");
        savedUser.setCreatedAt(LocalDateTime.now());

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        String result = userService.createUser(request);

        assertEquals(
                "user data inserted into DB successfully",
                result
        );

        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(any(User.class));
        verify(userEventProducer).sendingToKafkaEvent(any());
    }


    // =========================
    // GET USER BY ID - SUCCESS
    // =========================

    @Test
    void getUserById_shouldReturnUser_whenUserExists() {

        User user = new User();

        user.setId(1L);
        user.setName("Jayant");
        user.setMail("jayant@gmail.com");
        user.setPhone("9876543210");
        user.setRole("USER");
        user.setVehicleNumber("OD01AB1234");
        user.setCreatedAt(LocalDateTime.now());

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(1L);

        assertNotNull(response);

        assertEquals(1L, response.getId());
        assertEquals("Jayant", response.getName());
        assertEquals("jayant@gmail.com", response.getMail());
        assertEquals("9876543210", response.getPhone());
        assertEquals("USER", response.getRole());
        assertEquals("OD01AB1234", response.getVehicleNumber());

        verify(userRepository).findById(1L);
    }


    // =========================
    // GET USER BY ID - NOT FOUND
    // =========================

    @Test
    void getUserById_shouldThrowException_whenUserDoesNotExist() {

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(99L)
        );

        verify(userRepository).findById(99L);
    }


    // =========================
    // GET ALL USERS
    // =========================

    @Test
    void getAllUser_shouldReturnAllUsers() {

        User user1 = new User();
        user1.setId(1L);
        user1.setName("Jayant");
        user1.setMail("jayant@gmail.com");
        user1.setPhone("9876543210");
        user1.setRole("USER");
        user1.setVehicleNumber("OD01AB1234");
        user1.setCreatedAt(LocalDateTime.now());

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Rahul");
        user2.setMail("rahul@gmail.com");
        user2.setPhone("9999999999");
        user2.setRole("USER");
        user2.setVehicleNumber("OD02CD5678");
        user2.setCreatedAt(LocalDateTime.now());

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        List<UserResponse> responses =
                userService.getAllUser();

        assertNotNull(responses);
        assertEquals(2, responses.size());

        assertEquals("Jayant", responses.get(0).getName());
        assertEquals("Rahul", responses.get(1).getName());

        verify(userRepository).findAll();
    }


    // =========================
    // GET ALL USERS - EMPTY
    // =========================

    @Test
    void getAllUser_shouldReturnEmptyList_whenNoUsersExist() {

        when(userRepository.findAll())
                .thenReturn(List.of());

        List<UserResponse> responses =
                userService.getAllUser();

        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(userRepository).findAll();
    }


    // =========================
    // UPDATE USER - SUCCESS
    // =========================

    @Test
    void updateUserRequestData_shouldUpdateUserSuccessfully() {

        User existingUser = new User();

        existingUser.setId(1L);
        existingUser.setName("Old Name");
        existingUser.setMail("old@gmail.com");
        existingUser.setPhone("1111111111");
        existingUser.setRole("USER");
        existingUser.setVehicleNumber("OLD123");
        existingUser.setCreatedAt(LocalDateTime.now());

        UserUpdateRequest request = new UserUpdateRequest();

        request.setName("New Name");
        request.setMail("new@gmail.com");
        request.setPhone("9999999999");
        request.setVehicleNumber("NEW123");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.save(any(User.class)))
                .thenReturn(existingUser);

        UserResponse response =
                userService.updateUserRequestData(1L, request);

        assertNotNull(response);

        assertEquals("New Name", response.getName());
        assertEquals("new@gmail.com", response.getMail());
        assertEquals("9999999999", response.getPhone());
        assertEquals("NEW123", response.getVehicleNumber());

        verify(userRepository).findById(1L);
        verify(userRepository).save(existingUser);
    }


    // =========================
    // UPDATE USER - NOT FOUND
    // =========================

    @Test
    void updateUserRequestData_shouldThrowException_whenUserDoesNotExist() {

        UserUpdateRequest request = new UserUpdateRequest();

        request.setName("New Name");
        request.setMail("new@gmail.com");
        request.setPhone("9999999999");
        request.setVehicleNumber("NEW123");

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUserRequestData(99L, request)
        );

        verify(userRepository).findById(99L);
        verify(userRepository, never()).save(any(User.class));
    }


    // =========================
    // DELETE USER - SUCCESS
    // =========================

    @Test
    void deleteUser_shouldDeleteUserSuccessfully() {

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        String result = userService.deleteUser(1L);

        assertEquals(
                "user deleted successfully",
                result
        );

        verify(userRepository).findById(1L);
        verify(userRepository).deleteById(1L);
    }


    // =========================
    // DELETE USER - NOT FOUND
    // =========================

    @Test
    void deleteUser_shouldThrowException_whenUserDoesNotExist() {

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(99L)
        );

        verify(userRepository).findById(99L);
        verify(userRepository, never()).deleteById(anyLong());
    }


    // =========================
    // LOGIN - SUCCESS
    // =========================

    @Test
    void login_shouldReturnTokenSuccessfully() {

        LoginRequest request = new LoginRequest();

        request.setMail("jayant@gmail.com");
        request.setPassword("123456");

        when(jwtService.generateToken("jayant@gmail.com"))
                .thenReturn("dummy-jwt-token");

        UserService service = userService;

        LoginResponse response =
                service.login(request);

        assertNotNull(response);

        assertEquals(
                "log in successfully ",
                response.getMessage()
        );

        assertEquals(
                "dummy-jwt-token",
                response.getToken()
        );

        verify(authenticationManager)
                .authenticate(any());

        verify(jwtService)
                .generateToken("jayant@gmail.com");
    }
}