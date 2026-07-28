package com.VastaImoveis.CRM.Requisitos.Repository;

import com.VastaImoveis.CRM.Requisitos.Entity.Domain.Requisito;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequisitosRepository extends JpaRepository<Requisito, UUID> {
    Page<Requisito> findByCorretor(User corretor, Pageable pageable);

    Page<Requisito> findByGerente(User gerente, Pageable pageable);

}
