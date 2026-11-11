package com.example.employee_management;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service 
public class UserServiceImpl implements UserService{
    private final PasswordEncoder encoder;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public UserServiceImpl(CompanyRepository companyRepository, UserRepository userRepository, PasswordEncoder encoder){
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public User createUser(Long companyId, String userName, String password, String email){
        Company company = companyRepository.findById(companyId).orElseThrow(() -> 
            new CompanyNotFoundException("Company does not exist with the provided ID"));
        
        if (userRepository.findByUserName(userName).isPresent()){
            throw new UserAlreadyExistsException("That username is already associated with a user.");
        }
        if (userRepository.findByEmail(email).isPresent()){
            throw new UserAlreadyExistsException("That email is already associated with a user.");
        }

        String encodedPassword = encoder.encode(password);
        
        User user = new User(company, userName, encodedPassword, email);

        return userRepository.save(user);
    }
}
