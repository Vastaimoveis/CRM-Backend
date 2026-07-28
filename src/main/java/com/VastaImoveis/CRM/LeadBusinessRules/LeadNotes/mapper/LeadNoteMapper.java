package com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.mapper;

import com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.Entity.domain.LeadNote;
import com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.Entity.dto.LeadNoteRequestDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.Entity.dto.LeadNoteResponseDTO;

public class LeadNoteMapper {
    public static LeadNote toEntity(LeadNoteRequestDTO dto){
        LeadNote leadNote = new LeadNote();
        leadNote.setNote(dto.getNote());
        return leadNote;
    }

    public static LeadNoteResponseDTO toDTO(LeadNote leadNote){
        return new LeadNoteResponseDTO(
                leadNote.getId(),
                leadNote.getLead().getId(),
                leadNote.getNote(),
                leadNote.getCreatedAt()
        );
    }
}
