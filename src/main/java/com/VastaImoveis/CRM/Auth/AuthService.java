package com.VastaImoveis.CRM.Auth;

import com.VastaImoveis.CRM.Auth.Jwt.JwtService;
import com.VastaImoveis.CRM.Auth.dto.AuthResult;
import com.VastaImoveis.CRM.Exception.InvalidCredentialsException;
import com.VastaImoveis.CRM.Exception.InvalidEmailException;
import com.VastaImoveis.CRM.Exception.InvalidPasswordException;
import com.VastaImoveis.CRM.Users.Entity.Domain.User;
import com.VastaImoveis.CRM.Users.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository repository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       UserRepository userRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResult login(String email, String password) {

        User user = repository.findByEmail(email)
                .orElseThrow(InvalidEmailException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException();
        }
        System.out.println(user.getNome());
        System.out.println(user.getRole());
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.gerenateRefreshToken(user);
        return new AuthResult(token, refreshToken, user);
    }
    public String refreshToken(User user){

        return jwtService.generateToken(user);
    }



    public String extractEmailFromRefreshToken(
            String refreshToken
    ) {
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new InvalidCredentialsException();
        }
        return jwtService.extractEmail(refreshToken);
    }

    public String generateNewAccessToken(
            String email
    ) {

        User user = repository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        return jwtService.generateToken(user);
    }
}
