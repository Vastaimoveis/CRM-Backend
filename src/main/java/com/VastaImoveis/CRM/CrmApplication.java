package com.VastaImoveis.CRM;

import com.VastaImoveis.CRM.Users.Entity.Domain.RegiaoUsers;
import com.VastaImoveis.CRM.Users.Entity.Domain.RoleUsers;
import com.VastaImoveis.CRM.Users.Entity.Domain.User;
import com.VastaImoveis.CRM.Users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CrmApplication {

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }
    @Bean
    CommandLineRunner init(
            UserRepository repo,
            PasswordEncoder encoder
    ) {

        return args -> {

            if (!repo.existsByEmail(adminEmail)) {

                User user = new User();

                user.setNome("Administrador");
                user.setEmail(adminEmail);
                user.setPassword(
                        encoder.encode(adminPassword)
                );
                user.setRole(RoleUsers.GERENTE);
                user.setRegiao(RegiaoUsers.CURITIBA);

                repo.save(user);

                System.out.println(
                        "Usuário administrador criado."
                );
            }
        };
    }
}
