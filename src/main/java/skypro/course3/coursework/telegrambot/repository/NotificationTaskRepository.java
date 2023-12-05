package skypro.course3.coursework.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skypro.course3.coursework.telegrambot.entity.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask,Long> {
    List<NotificationTask> findAllByExecDateLessThan(LocalDateTime timePoint);
}