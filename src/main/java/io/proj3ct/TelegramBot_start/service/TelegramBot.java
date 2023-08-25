package io.proj3ct.TelegramBot_start.service;

import io.proj3ct.TelegramBot_start.config.BotConfig;
import io.proj3ct.TelegramBot_start.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;




@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;


    private  final UserRepository userRepository;
    public TelegramBot(BotConfig config, UserRepository userRepository) {
        super(config.getToken());
        this.config = config;
        this.userRepository = userRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
if(update.hasMessage() && update.getMessage().hasText()){
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
        default -> sendMessage(chatId, "Sorry, command was not recognized");
    }
}

    }

    private void registerUser(Message message) {
if (userRepository.findById(message.getChatId()).isEmpty()){
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

    private void startCommandReceived(long chatId,String name){
           String answer = "Hi, " + name +", nice to meet you!";
           sendMessage(chatId, answer);
    }
    private void sendMessage (long chatId , String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        }
        catch (TelegramApiException e){

        }
    }
@Override
public String getBotToken(){

        return config.getToken();
}
    @Override
    public String getBotUsername() {

        return config.getBotName();
    }
}
