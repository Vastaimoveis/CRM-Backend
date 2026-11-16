
package com.VastaImoveis.CRM.ScriptBusinessRules.Script.Repository;

import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Domain.Script;
import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Domain.ScriptCategories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ScriptRepository extends JpaRepository<Script, UUID> {

    // Lista todos os scripts de um usuário
    Page<Script> findAllByUser_Id(UUID userId, Pageable pageable);

    // Busca um script específico pertencente ao usuário
    Optional<Script> findByIdAndUser_Id(UUID id, UUID userId);

    // Pesquisa por título ou mensagem, limitada ao usuário
    Page<Script> findByUser_IdAndTitleContainingIgnoreCaseOrUser_IdAndMessageContainingIgnoreCase(
            UUID userId,
            String title,
            UUID userIdMessage,
            String message,
            Pageable pageable
    );

    // Verifica se o script pertence ao usuário
    boolean existsByIdAndUser_Id(UUID id, UUID userId);

    //Filtra por categoria
    Page<Script> findByUser_IdAndCategory(UUID userId, ScriptCategories category, Pageable pageable);

    // Exclui um script somente se pertencer ao usuário
    void deleteByIdAndUser_Id(UUID id, UUID userId);

    @Query("""
                SELECT s FROM Script s
                WHERE s.user.id = :userId
                AND (
                    LOWER(s.title) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(s.message) LIKE LOWER(CONCAT('%', :search, '%'))
                )
            """)
    Page<Script> searchByUser(
            @Param("userId") UUID userId,
            @Param("search") String search,
            Pageable pageable
    );
}
