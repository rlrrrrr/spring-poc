package com.example.springsecuritypoc;


import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
@RestController
public class LoginController {



    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    public LoginController(MyUserDetailService userDetailService, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository  = userRepository;
    }

    @GetMapping("/home")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("hello");
    }

    @GetMapping("/private")
    public ResponseEntity<String> personnal(){
        return ResponseEntity.ok("private");
    }


    @PostMapping("/signin")
    public ResponseEntity<Void> signIn(@RequestBody LoginRequest loginRequest) {
        List<String> authorities = new ArrayList<>();
        authorities.add("ROLE_USER");
        System.out.println(authorities.toString());
        UserEntity user  = new UserEntity(loginRequest.username, loginRequest.password, authorities);
        userRepository.save(user);
        LoginRequest userRequest = new LoginRequest(user.getUsername(), user.getPassword());
        return login(userRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);

        // Créer un cookie avec le nom d'utilisateur
        ResponseCookie cookie = ResponseCookie.from("username", authenticationResponse.getName())
                .httpOnly(true)
                .secure(false) 
                .path("/")
                .build();
                return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }


    public record LoginRequest(String username, String password) {
    }

}