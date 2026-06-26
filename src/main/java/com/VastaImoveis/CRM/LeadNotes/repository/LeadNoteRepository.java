package com.VastaImoveis.CRM.LeadNotes.repository;

import com.VastaImoveis.CRM.Lead.Entity.Domain.Lead;
import com.VastaImoveis.CRM.LeadNotes.Entity.domain.LeadNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface LeadNoteRepository extends JpaRepository<LeadNote, UUID> {
    Optional<LeadNote> findById(UUID uuid);

    Page<LeadNote> findByLead(Lead lead, Pageable pageable);

    Page<LeadNote> findByLeadIn(List<Lead> leads, Pageable pageable);

    List<LeadNote> findByLeadIdOrderByCreatedAtAsc(UUID leadId);

    Page<LeadNote> findByLeadIdOrderByCreatedAtDesc(UUID leadId, Pageable pageable);

    @Query("""
    SELECT ln.lead.id
    FROM LeadNote ln
    WHERE ln.lead.id IN :leadIds
""")
    List<UUID> findLeadIdsWithNotes(@Param("leadIds") List<UUID> leadIds);
}
