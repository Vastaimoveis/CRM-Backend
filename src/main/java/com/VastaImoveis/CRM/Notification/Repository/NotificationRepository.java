package com.VastaImoveis.CRM.Notification.Repository;

import com.VastaImoveis.CRM.Notification.Entity.Domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository
        extends JpaRepository<Notification, UUID> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId);

}
