package com.ev.userservice.Security;

import com.ev.userservice.Entity.User;
import com.ev.userservice.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> isAvail = userRepository.findByMail(username);
        if(isAvail.isPresent()){
            User user = isAvail.get();
            return new CustomUserDetails(user);
        }
        throw new UsernameNotFoundException("user not found using this mail " + username);
    }
}
