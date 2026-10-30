package com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Repository;

import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.Domain.FollowUp;
import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.Domain.FollowUpStatus;
import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.dto.FollowUpResponseDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.dto.FollowUpStatusCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowUpRepository extends JpaRepository<FollowUp, UUID> {
    Page<FollowUp> findByLead_IdOrderByCreatedAtDesc(
            UUID leadId,
            Pageable pageable
    );

    Optional<FollowUp> findByLead_IdAndAtualTrue(UUID leadId);

    Page<FollowUp> findByAtualTrueAndLead_User_Id(UUID userId, Pageable pageable);

    boolean existsByLead_Id(UUID leadId);

    boolean existsByLead_IdAndAtualTrue(UUID leadId);

    boolean existsByLead_IdAndAtualTrueAndStatus(
            UUID leadId,
            FollowUpStatus status
    );

    @Query("""
                SELECT new com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.dto.FollowUpStatusCountDTO(
                    f.status,
                    COUNT(f)
                )
                FROM FollowUp f
                WHERE f.atual = true
                GROUP BY f.status
                ORDER BY f.status
            """)
    List<FollowUpStatusCountDTO> countCurrentByStatus();

    Page<FollowUp> findByAtualTrue(Pageable pageable);

    @Query("""
                SELECT new com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.dto.FollowUpStatusCountDTO(
                    f.status,
                    COUNT(f)
                )
                FROM FollowUp f
                WHERE f.atual = true
                  AND f.lead.user.id = :userId
                GROUP BY f.status
                ORDER BY f.status
            """)
    List<FollowUpStatusCountDTO> countCurrentByStatusAndUser(
            @Param("userId") UUID userId
    );

    @Query("""
    SELECT COUNT(f)
    FROM FollowUp f
    WHERE f.atual = true
      AND f.lead.user.id = :userId
      AND f.status = :status
      AND f.proximoContato < :inicioDeHoje
""")
    Long countAtrasadosByUser(
            @Param("userId") UUID userId,
            @Param("status") FollowUpStatus status,
            @Param("inicioDeHoje") LocalDateTime inicioDeHoje
    );

    @Query("""
                SELECT f
                FROM FollowUp f
                JOIN f.lead l
                WHERE f.atual = true
            
                AND (
                    COALESCE(:search, '') = ''
                    OR LOWER(l.nome) LIKE CONCAT('%', LOWER(:search), '%')
                    OR LOWER(l.email) LIKE CONCAT('%', LOWER(:search), '%')
                    OR l.telefone LIKE CONCAT('%', :search, '%')
                )
            
                AND (
                    :userId IS NULL
                    OR l.user.id = :userId
                )
            
                AND (
                    :status IS NULL
            
                    OR (
                        :status = :atrasado
                        AND f.status IN (:pendente, :reagendado)
                        AND f.proximoContato < :inicioDeHoje
                    )
            
                    OR (
                        :status = :pendente
                        AND f.status = :pendente
                        AND (
                            f.proximoContato >= :inicioDeHoje
                            OR f.proximoContato IS NULL
                        )
                    )
            
                    OR (
                        :status = :reagendado
                        AND f.status = :reagendado
                        AND (
                            f.proximoContato >= :inicioDeHoje
                            OR f.proximoContato IS NULL
                        )
                    )
                    
                    OR (
                        :status <> :pendente
                        AND :status <> :atrasado
                        AND :status <> :reagendado
                        AND f.status = :status
                    )
                )
            
                AND (
                    f.proximoContato >= :startDate
                )
            
                AND (
                    f.proximoContato < :endDate
                )
            
                ORDER BY f.proximoContato ASC, f.id ASC
            """)
    Page<FollowUp> filter(
            @Param("search") String search,
            @Param("status") FollowUpStatus status,
            @Param("userId") UUID userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("pendente") FollowUpStatus pendente,
            @Param("reagendado") FollowUpStatus reagendado,
            @Param("atrasado") FollowUpStatus atrasado,
            @Param("inicioDeHoje") LocalDateTime inicioDeHoje,
            Pageable pageable
    );

    @Query("""
                SELECT COUNT(f)
                FROM FollowUp f
                WHERE f.atual = true
                  AND f.lead.user.id = :userId
                  AND f.status = :status
                  AND f.proximoContato >= :inicioDeHoje
                  AND f.proximoContato < :inicioDeAmanha
            """)
    Long countTodayByUser(
            @Param("userId") UUID userId,
            @Param("status") FollowUpStatus status,
            @Param("inicioDeHoje") LocalDateTime inicioDeHoje,
            @Param("inicioDeAmanha") LocalDateTime inicioDeAmanha
    );

    @Query("""
    SELECT COUNT(f)
    FROM FollowUp f
    WHERE f.atual = true
      AND f.status = :status
      AND f.proximoContato < :inicioDeHoje
""")
    Long countAllAtrasados(
            @Param("status") FollowUpStatus status,
            @Param("inicioDeHoje") LocalDateTime inicioDeHoje
    );

    @Query("""
                SELECT COUNT(f)
                FROM FollowUp f
                WHERE f.atual = true
                  AND f.status = :status
                  AND f.proximoContato >= :inicioDeHoje
                  AND f.proximoContato < :inicioDeAmanha
            """)
    Long countAllToday(
            @Param("status") FollowUpStatus status,
            @Param("inicioDeHoje") LocalDateTime inicioDeHoje,
            @Param("inicioDeAmanha") LocalDateTime inicioDeAmanha
    );
}
