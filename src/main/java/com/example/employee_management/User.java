package com.example.employee_management;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Table(name = "app_user")
@Entity 
public class User {
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(unique = true)
    private String userName;
    @NotBlank 
    private String password;
    @ManyToOne 
    @JoinColumn(name="company_id", nullable = false)
    @NotNull
    private Company employer;
    @NotBlank 
    @Email 
    @Column(unique = true)
    private String email;
    private boolean enabled;

    public User(Company employer, String userName, String password, String email){
        this.employer = employer;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.enabled = true;
    }
    protected User(){

    }
    public String getUserName(){
        return this.userName;
    }
    public String getEmail(){
        return this.email;
    }
}
