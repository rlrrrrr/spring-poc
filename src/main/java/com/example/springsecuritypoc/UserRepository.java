package com.example.springsecuritypoc;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    List<UserEntity> findByLastName(String lastName);

}
