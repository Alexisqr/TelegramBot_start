package io.proj3ct.TelegramBot_start.service;

import io.proj3ct.TelegramBot_start.config.BotConfig;
import io.proj3ct.TelegramBot_start.entity.User;
import io.proj3ct.TelegramBot_start.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.sqlite.util.StringUtils.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;


    private final UserRepository userRepository;

    public TelegramBot(BotConfig config, UserRepository userRepository) {
        super(config.getToken());
        this.config = config;
        this.userRepository = userRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start" -> {
                    if (update.getMessage().getChat().getFirstName() != null) {

                        registerUser(update.getMessage());
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    } else {
                        startCommandReceived(chatId, "NoName");
                    }
                }
                case "/get_all_users" -> {

                    AllUsers(chatId);

                }
                default -> {
                    UpdateReputation(messageText, chatId);
                    int point = AddPoint(messageText, chatId);
                    sendMessage(chatId, "Three won " + point + " points");
                    if (point > 90) {
                        sendMessage(chatId, "You are lucky");
                    }
                }
            }
        }

    }

    private void UpdateReputation(String messageText, long chatId) {
        int min = messageText.indexOf("-");
        int plus = messageText.indexOf("+");
        if (plus >= 0 || min >= 0) {
            User myUser = new User();
            myUser = userRepository.findById(chatId).orElse(new User());
            if (plus >= 0) {
                myUser.setReputation(myUser.getReputation() + 1);
            } else {
                myUser.setReputation(myUser.getReputation() - 1);
            }
            userRepository.save(myUser);
        }

    }

    private void AllUsers(long chatId) {
        List<User> users = userRepository.findAll();

        StringBuilder userList = new StringBuilder("List of all users:\n");

        for (User user : users) {
            userList.append("Username: ").append(user.getUserName()).append("\n");
            userList.append("First Name: ").append(user.getFirstName()).append("\n");
            userList.append("Last Name: ").append(user.getLastName()).append("\n");
            userList.append("Chat ID: ").append(user.getId()).append("\n\n");
        }

        sendMessage(chatId, userList.toString());
    }

    private int AddPoint(String messageText, long chatId) {
        messageText = " " + messageText;
        User myUser = new User();
        myUser = userRepository.findById(chatId).orElse(new User());

        var index = StringUtils.contains(messageText,"cat");
        int n = 50;
        if (index) {
            log.info("Cat index is: " +index);
            int x = ThreadLocalRandom.current().nextInt(0, 3);
            switch (x) {
                case 1 -> {
                    n = n + 10;
                    break;
                }
                case 2 -> {
                    n = n + 20;
                    break;
                }
                default -> {

                }
            }

        }
        int point = ThreadLocalRandom.current().nextInt(n, 101);
        myUser.setScore(myUser.getScore() + point);
        userRepository.save(myUser);
        return point;
    }

    private void registerUser(Message message) {
        if (userRepository.findById(message.getChatId()).isEmpty()) {
            var chatId = message.getChatId();
            var chat = message.getChat();
            User user = new User();
            user.setId(chatId);
            user.setUserName(chat.getUserName());
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());

            userRepository.save(user);
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!";
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to Message the bot");
        }
    }



    @Override
    public String getBotUsername() {

        return config.getBotName();
    }
}
