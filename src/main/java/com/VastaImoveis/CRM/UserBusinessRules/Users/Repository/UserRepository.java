package com.VastaImoveis.CRM.UserBusinessRules.Users.Repository;

import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.RegiaoUsers;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findById(UUID id);

    Page<User> findByRegiao(RegiaoUsers regiaoUsers, Pageable pageable);

    Page<User> findAll(Pageable pageable);
}
