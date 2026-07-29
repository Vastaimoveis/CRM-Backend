package com.VastaImoveis.CRM.LeadBusinessRules.Lead.Repository;

import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.Lead;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.StatusLead;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.dto.StatusCount;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeadRepository extends JpaRepository<Lead, UUID> {

    Optional<Lead> findByEmail(String email);

    Page<Lead> findByUser(User user, Pageable pageable);

    Page<Lead> findByUserId(UUID userId, Pageable pageable);

    List<Lead> findByUserId(UUID userId);

    Page<Lead> findByUserIn(List<User> users, Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByTelefone(String telefone);

    Page<Lead> findByStatus(StatusLead statusLead, Pageable pageable);

    Page<Lead> findByStatusNot(StatusLead statusLead, Pageable pageable);

    List<Lead> findByStatusIn(
            List<StatusLead> status
    );

    List<Lead> findByStatusInAndUserId(
            List<StatusLead> status,
            UUID userId
    );

    Page<Lead> findByStatusNotAndUser_Id(StatusLead status, UUID userId, Pageable pageable);
    @Query("""

                 SELECT new com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.dto.StatusCount(l.status, COUNT(l))
                FROM Lead l
                WHERE (:user IS NULL OR l.user = :user)
                GROUP BY l.status
            """)
    List<StatusCount> countByStatus(User user);

    @Query("""
    SELECT new com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.dto.StatusCount(
        l.status,
        COUNT(l)
    )
    FROM Lead l
    WHERE (:userId IS NULL OR l.user.id = :userId)
    GROUP BY l.status
""")
    List<StatusCount> countByStatusByUser(@Param("userId") UUID userId);

    @Query("""
    SELECT l
    FROM Lead l
    WHERE
        LOWER(l.nome) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(l.email) LIKE LOWER(CONCAT('%', :search, '%'))
        OR l.telefone LIKE CONCAT('%', :search, '%')
""")
    Page<Lead> search(
            @Param("search") String search,
            Pageable pageable
    );

    @Query("""
SELECT l
FROM Lead l
WHERE
(
    COALESCE(:search, '') = ''
    OR LOWER(l.nome) LIKE CONCAT('%', LOWER(:search), '%')
    OR LOWER(l.email) LIKE CONCAT('%', LOWER(:search), '%')
    OR l.telefone LIKE CONCAT('%', :search, '%')
)
AND
(
    (:status IS NULL AND l.status <> com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.StatusLead.ENCERRADO)
    OR l.status = :status
)
AND
(
    :userId IS NULL
    OR l.user.id = :userId
)
AND (
    l.createdAt >= :startDate
)
AND (
     l.createdAt < :endDate
)
ORDER BY l.createdAt DESC
""")
    Page<Lead> filter(
            @Param("search") String search,
            @Param("status") StatusLead status,
            @Param("userId") UUID userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    /*
    Leads por periodo:
        WHERE (:user IS NULL OR l.user = :user)
        AND l.createdAt BETWEEN :start AND :end

    Leads por mês
        SELECT FUNCTION('MONTH', l.createdAt), COUNT(l)

    Ranking de corretores
        SELECT l.user.nome, COUNT(l)
            FROM Lead l
            GROUP BY l.user.nome

    Conversão (taxa de fechamento)
            long fechados = porStatus.getOrDefault("FECHADO", 0L);
            double taxa = total > 0 ? (double) fechados / total : 0;
        */

    Optional<Lead> findByTelefone(String telefone);
}