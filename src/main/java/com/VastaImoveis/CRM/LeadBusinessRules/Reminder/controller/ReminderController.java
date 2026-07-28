package com.VastaImoveis.CRM.LeadBusinessRules.Reminder.controller;

import com.VastaImoveis.CRM.LeadBusinessRules.Reminder.Service.ReminderService;
import com.VastaImoveis.CRM.LeadBusinessRules.Reminder.entity.dto.ReminderRequestDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.Reminder.entity.dto.ReminderResponseDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.Reminder.entity.dto.RequestReadDTO;
import com.VastaImoveis.CRM.shared.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reminders")
public class ReminderController {
        private final ReminderService service;

        public ReminderController(ReminderService service) {
            this.service = service;
        }

        @GetMapping("/{userId}")
        @PreAuthorize("hasAnyRole('GERENTE','CORRETOR')")
        public ResponseEntity<ApiResponse<List<ReminderResponseDTO>>> findByUser(
                @PathVariable UUID userId
        ){
                List<ReminderResponseDTO> list = service.findByUserId(userId);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ApiResponse<>(true, list, "Lembretes buscados com sucesso")
                );
        }

        @PostMapping
        @PreAuthorize("hasAnyRole('GERENTE','CORRETOR')")
        public ResponseEntity<ApiResponse<ReminderResponseDTO>> createReminder(@RequestBody ReminderRequestDTO dto){
                System.out.println(dto.getAlarmAt());
                ReminderResponseDTO created = service.create(dto);

                return ResponseEntity.status(HttpStatus.CREATED).body(
                        new ApiResponse<>(true, created, "Lembrete criado com sucesso")
                );
        }

        @PatchMapping("/{reminderId}")
        @PreAuthorize("hasAnyRole('GERENTE','CORRETOR')")
        public ResponseEntity<ApiResponse<ReminderResponseDTO>> patchRead(@RequestBody RequestReadDTO dto, @PathVariable UUID reminderId){
            ReminderResponseDTO reminder = service.patchRead(reminderId, dto.isRead());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse<>(true, reminder, "Lembrete alterado com sucesso")
            );
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasAnyRole('GERENTE','CORRETOR')")
        public ResponseEntity<ApiResponse<Void>> deletereminder(@PathVariable UUID reminderId){
            service.delete(reminderId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                    new ApiResponse<>(true, null, "Lembrete deletado com sucesso")
            );
        }

}
