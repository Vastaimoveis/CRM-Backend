
package com.VastaImoveis.CRM.ScriptBusinessRules.Script.Mapper;

import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Domain.Script;
import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Dto.ScriptCreateDTO;
import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Dto.ScriptResponseDTO;
import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Dto.ScriptUpdateDTO;

public class ScriptMapper {

    public static Script toEntity(ScriptCreateDTO dto) {
        Script script = new Script(
                dto.getTitle().trim(),
                dto.getMessage().trim()
                );
        script.setCategory(dto.getCategory());
        return script;
    }

    public static ScriptResponseDTO toResponse(Script entity) {
        return new ScriptResponseDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getMessage(),
                entity.getUser().getId(),
                entity.getCategory(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static Script toUpdate(
            Script entity,
            ScriptUpdateDTO update
    ) {

        if (update.getTitle() != null &&
                !update.getTitle().isBlank()) {

            entity.setTitle(update.getTitle().trim());
        }

        if (update.getMessage() != null &&
                !update.getMessage().isBlank()) {

            entity.setMessage(update.getMessage().trim());
        }

        return entity;
    }
}
