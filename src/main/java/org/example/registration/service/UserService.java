package org.example.registration.service;

import lombok.RequiredArgsConstructor;
import org.example.registration.model.dto.RegistrationUserDto;
import org.example.registration.model.entity.Role;
import org.example.registration.model.entity.User;
import org.example.registration.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Optional<User> findByUsername(String username) {
        return userRepository.findByName(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().getAuthority()))
        );
    }

    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User user = new User();
        System.out.println(registrationUserDto.getUsername());
        user.setName(registrationUserDto.getUsername());
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setRole(Role.Role_USER);
        return userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}