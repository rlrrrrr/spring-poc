package com.example.springsecuritypoc;

import com.example.springsecuritypoc.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Ici, vous pouvez récupérer le nom d'utilisateur et d'autres informations nécessaires
        // Par exemple, si vous avez besoin d'un token personnalisé, vous pouvez le concaténer avec le nom d'utilisateur
        // et le séparer ici pour récupérer les informations nécessaires
        // Pour cet exemple, nous supposons que vous avez besoin uniquement du nom d'utilisateur
        UserDetails userEntity = userRepository.findByLastName(username).get(0);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }
        
        // Construire les détails de l'utilisateur

        return User.withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .disabled(false)
                .authorities(userEntity.getAuthorities()).build();
    }

}
