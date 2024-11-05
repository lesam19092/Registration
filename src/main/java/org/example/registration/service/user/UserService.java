package org.example.registration.service.user;

import org.example.registration.model.dto.RegistrationUserDto;
import org.example.registration.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {

    User findByUsername(String username);

    User createNewUser(RegistrationUserDto registrationUserDto);

    boolean existsByUsername(String username);

}
