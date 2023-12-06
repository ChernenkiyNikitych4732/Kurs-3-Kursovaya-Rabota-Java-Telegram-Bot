package skypro.course3.coursework.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import skypro.course3.coursework.telegrambot.entity.NotificationTask;
import skypro.course3.coursework.telegrambot.repository.NotificationTaskRepository;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private static Pattern PATTERN = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2})\\s+(.*)");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private NotificationTaskRepository repository;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            String text = update.message().text();
            Long chatId = update.message().chat().id();
            Matcher matcher = PATTERN.matcher(text);
            if ("/start".equalsIgnoreCase(text)) {
                sendMessage(chatId,"01.01.2022 20:00 Сделать домашнюю работу");
            } else if (matcher.matches()) {
                String dateStr = matcher.group(1);
                LocalDateTime execDate = LocalDateTime.parse(dateStr, FORMATTER);
                String message = matcher.group(2);
                NotificationTask task=  new NotificationTask();
                task.setChatId(chatId);
                task.setMessage(message);
                task.setExecDate(execDate);
                repository.save(task);
                sendMessage(chatId, "Событие сохранено на дату " + execDate);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private SendResponse sendMessage(Long chatId,String message) {
        SendMessage send = new SendMessage(chatId, message);
        return   telegramBot.execute(send);
    }

}