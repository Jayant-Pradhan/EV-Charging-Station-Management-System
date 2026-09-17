package com.ev.userservice.Service;

import com.ev.userservice.DTO.*;
import com.ev.userservice.Entity.User;
import com.ev.userservice.Exception.UserNotFoundException;
import com.ev.userservice.Kafka.UserEventProducer;
import com.ev.userservice.KafkaDTO.EventData;
import com.ev.userservice.Repository.UserRepository;
import com.ev.userservice.Security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserEventProducer userEventProducer;

    public String createUser(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setMail(userRequest.getMail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setPhone(userRequest.getPhone());
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setVehicleNumber(userRequest.getVehicleNumber());

        userRepository.save(user);

        userEventProducer.sendingToKafkaEvent(
                new EventData(user.getId(),user.getName(),user.getMail(),user.getPhone(),user.getRole(),
                        user.getVehicleNumber(),user.getCreatedAt())
        );

        log.info("user is inserted into DB successfully ");

        return "user data inserted into DB successfully";

    }

    @Cacheable(value = "userdata", key = "#id")
    public UserResponse getUserById(long id) {
        Optional<User> isAvail = userRepository.findById(id);

        if(isAvail.isPresent()){
            User user = isAvail.get();
            UserResponse response = new UserResponse();
            response.setId(user.getId());
            response.setName(user.getName());
            response.setMail(user.getMail());
            response.setPhone(user.getPhone());
            response.setRole(user.getRole());
            response.setCreatedAt(user.getCreatedAt());
            response.setVehicleNumber(user.getVehicleNumber());
            return response;

        }
        throw new UserNotFoundException("user not found with :" + id);
    }

    public List<UserResponse> getAllUser() {

        List<User> users = userRepository.findAll();

        List<UserResponse> responsesList = new ArrayList<>();
        for(User user : users){
            UserResponse response = new UserResponse();
            response.setId(user.getId());
            response.setName(user.getName());
            response.setMail(user.getMail());
            response.setPhone(user.getPhone());
            response.setRole(user.getRole());
            response.setVehicleNumber(user.getVehicleNumber());
            response.setCreatedAt(user.getCreatedAt());
            responsesList.add(response);

            log.info("response is added to list of response : {} " , user.getId() );
        }
        return responsesList;

    }

    @CachePut(value = "userdata" , key = "#id")
    public UserResponse updateUserRequestData(long id, UserUpdateRequest userUpdateRequest) {

        Optional<User> isAvail = userRepository.findById(id);

        if(isAvail.isPresent()){
            User user = isAvail.get();
            user.setName(userUpdateRequest.getName());
            user.setMail(userUpdateRequest.getMail());
            user.setPhone(userUpdateRequest.getPhone());
            user.setVehicleNumber(userUpdateRequest.getVehicleNumber());
            userRepository.save(user);
            UserResponse response = new UserResponse();
            response.setName(userUpdateRequest.getName());
            response.setMail(userUpdateRequest.getMail());
            response.setPhone(userUpdateRequest.getPhone());
            response.setVehicleNumber(userUpdateRequest.getVehicleNumber());
            response.setRole(user.getRole());
            response.setId(user.getId());
            response.setCreatedAt(user.getCreatedAt());

            return response;


        }
        else{
            throw new UserNotFoundException("user not found with this id : " + id);
        }

    }

    @CacheEvict(value = "userdata", key = "#id")
    public String deleteUser(long id) {
        Optional<User> isAvail = userRepository.findById(id);

        if(isAvail.isPresent()){
            userRepository.deleteById(id);
            return "user deleted successfully";
        }
        else{
            throw new UserNotFoundException("user not found with this id  : " + id);
        }
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getMail() , loginRequest.getPassword())
        );
        String token = jwtService.generateToken(loginRequest.getMail());

        return new LoginResponse("log in successfully " , token);

    }
}
