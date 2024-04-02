package com.example.springsecuritypoc;


import jakarta.persistence.*;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;


  private  String lastName;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getLastName(){
        return lastName;
    }
}
