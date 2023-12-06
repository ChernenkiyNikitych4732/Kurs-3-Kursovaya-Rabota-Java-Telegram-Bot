package skypro.course3.coursework.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import skypro.course3.coursework.telegrambot.entity.NotificationTask;
import skypro.course3.coursework.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationsScheduler {
    private static Logger logger = LoggerFactory.getLogger(NotificationsScheduler.class);
    private final NotificationTaskRepository repository;
    private final TelegramBot bot;

    public NotificationsScheduler(NotificationTaskRepository repository, TelegramBot bot) {
        this.repository = repository;
        this.bot = bot;
    }

    @Scheduled(fixedDelay = 50000L)
    public void scheduler() {
        logger.info("Schedule");

        List<NotificationTask> tasks = repository.findAllByExecDateLessThan(LocalDateTime.now());
        for (NotificationTask task : tasks) {
            String text = task.getExecDate() + ": " + task.getMessage();
            SendResponse response = bot.execute(new SendMessage(task.getChatId(), text));
            if (response.isOk()) {
                repository.delete(task);
            }
        }
    }
}