package com.VastaImoveis.CRM.Reminder.Repository;

import com.VastaImoveis.CRM.Reminder.entity.domain.Reminder;
import com.VastaImoveis.CRM.Users.Entity.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReminderRepository extends JpaRepository<Reminder, UUID> {
    List<Reminder> findByUserOrderByAlarmAtDesc(User user);
}
