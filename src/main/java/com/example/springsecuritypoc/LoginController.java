package com.example.springsecuritypoc;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
public class LoginController {



    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    public LoginController(MyUserDetailService userDetailService, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository  = userRepository;
    }

    @GetMapping("/home")
    public ResponseEntity<String> hello(Authentication authentication){
        System.out.println(authentication.isAuthenticated());
        System.out.println(authentication.getPrincipal());
        return ResponseEntity.ok("hello");
    }

    @GetMapping("/private")
    public ResponseEntity<String> personnal(Principal principal){
        return ResponseEntity.ok("private: "+principal.getName());
    }


    @PostMapping(value = "/signin", consumes = "application/json")
    public ResponseEntity<String> signIn(HttpServletRequest req,@RequestBody LoginRequest loginRequest) {
        List<String> authorities = new ArrayList<>();
        authorities.add("ROLE_USER");
        System.out.println(authorities.toString());
        UserEntity user  = new UserEntity(loginRequest.username, loginRequest.password, authorities);
        userRepository.save(user);
        LoginRequest userRequest = new LoginRequest(user.getUsername(), user.getPassword());
        return login(req,userRequest);
    }


    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<String> login(HttpServletRequest req, @RequestBody LoginRequest loginRequest) {
        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authenticationResponse);
        HttpSession session = req.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
        return ResponseEntity.ok("your are logged "+authenticationResponse.getName());

    }


    public record LoginRequest(String username, String password) {
    }

}