package com.example.springsecuritypoc;


import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    public LoginController(AuthenticationManager authenticationManager,UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository  = userRepository;
    }


    @PostMapping("/signIn")
    public ResponseEntity<Void> signIn(@RequestBody LoginRequest loginRequest) {
        UserDetails user  = User.withUsername(loginRequest.username).password(loginRequest.password).roles("USER").build();
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
                .httpOnly(true) // Pour améliorer la sécurité
                .secure(false) // Pour envoyer le cookie uniquement sur des connexions HTTPS sécurisées
                .path("/")
                .build();

        // Ajouter le cookie à la réponse
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }


    public record LoginRequest(String username, String password) {
    }

}