package com.VastaImoveis.CRM.LeadBusinessRules.Notification.Controller;

import com.VastaImoveis.CRM.LeadBusinessRules.Notification.Entity.DTO.NotificationReadDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.Notification.Entity.DTO.NotificationRequestDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.Notification.Entity.DTO.NotificationResponseDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.Notification.Service.NotificationService;
import com.VastaImoveis.CRM.shared.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('GERENTE','CORRETOR')")
    public ResponseEntity<ApiResponse<List<NotificationResponseDTO>>> findByUser(
            @PathVariable UUID userId
    ) {

        List<NotificationResponseDTO> list = service.getByUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true, list, "Notificações buscadas com sucesso")
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GERENTE','CORRETOR')")
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> createNotification(
            @RequestBody NotificationRequestDTO dto
    ) {

        NotificationResponseDTO created = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true, created, "Notificação criada com sucesso")
        );
    }

    @PatchMapping("/{notificationId}")
    @PreAuthorize("hasAnyRole('GERENTE','CORRETOR')")
    public ResponseEntity<ApiResponse<NotificationResponseDTO>> patchRead(
            @RequestBody NotificationReadDTO dto,
            @PathVariable UUID notificationId
    ) {

        NotificationResponseDTO notification =
                service.read(notificationId, dto);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true, notification, "Notificação alterada com sucesso")
        );
    }

    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasAnyRole('GERENTE','CORRETOR')")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable UUID notificationId
    ) {

        service.delete(notificationId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                new ApiResponse<>(true, null, "Notificação deletada com sucesso")
        );
    }

}