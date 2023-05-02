package io.proj3ct.RandomTaroBot.service;

import io.proj3ct.RandomTaroBot.config.BotConfig;
import io.proj3ct.RandomTaroBot.model.TaroCardsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private BotConfig config;
    static final String HELP_TXT = "Добро пожаловать в чат-бота по раскладам Таро! \n\n" +
            "Здесь вы можете посмотреть расклады составленные бездушной и непредвзятой машиной! \n\n" +
            "Поддерживаемые команды: \n" +
            "/start - получить приветсвенное сообщение \n" +
            "/help- получить подсказку о командах(вы это видите сейчас) \n" +
            "/general - Получить общий расклад! \n" +
            "/love - Получить расклад на любовь! \n" +
            "/work - Получить расклад на работу! \n" +
            "/situation - Получить расклад на конкретную ситуацию \n\n\n"+
            "Приятного использования!";
    @Autowired
    private TaroCardsRepository taroCardsRepository;

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Получить приветственное сообщение"));
        listOfCommands.add(new BotCommand("/help", "Получить подсказку по командам"));
        listOfCommands.add(new BotCommand("/general", "Получить общий расклад"));
        listOfCommands.add(new BotCommand("/love", "Получить расклад на любовь"));
        listOfCommands.add(new BotCommand("/situation", "Получить расклад на конкретную ситуацию"));
        listOfCommands.add(new BotCommand("/work", "Получить расклад на работу"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {

        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        var r = new Random();

        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            switch (message) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/general":
                    var randomId = r.nextInt();
                    var taroCard = taroCardsRepository.findById(randomId);
                    sendMessage(chatId, "Ваша Общая карта: " + taroCard.get().getName());
                    sendMessage(chatId, taroCard.get().getLoveSense());
                    break;
                case "/love":
                    int randomIdForLoveOne = r.nextInt();
                    int randomIdForLoveTwo = r.nextInt();
                    int randomIdForLoveThree = r.nextInt();
                    int[] chosenCardsLove = {randomIdForLoveOne, randomIdForLoveTwo, randomIdForLoveThree};
                    sendMessage(chatId, threeCards(chosenCardsLove, "love"));
                    break;
                case "/situation":
                    int randomIdForSitOne = r.nextInt();
                    int randomIdForSitTwo = r.nextInt();
                    int randomIdForSitThree = r.nextInt();
                    int[] chosenCardsSit = {randomIdForSitOne, randomIdForSitTwo, randomIdForSitThree};
                    sendMessage(chatId, threeCards(chosenCardsSit, "situation"));
                    break;
                case "/work":
                    int randomIdForWorkOne = r.nextInt();
                    int randomIdForWorkTwo = r.nextInt();
                    int randomIdForWorkThree = r.nextInt();
                    int[] chosenCardsWork = {randomIdForWorkOne, randomIdForWorkTwo, randomIdForWorkThree};
                    sendMessage(chatId, threeCards(chosenCardsWork, "work"));
                    break;
                case "/help" :
                    sendMessage(chatId, HELP_TXT);
                    break;
                default:
                    sendMessage(chatId, "Sorry, this command isn't supported yet!");
                    break;
            }
        }

    }
    private void startCommandReceived(long chatId, String name) {
        String answer = "Привет, " + name + ", рады тебя видеть! \n Здесь ты можешь узнать своё будущее! \n напиши /help чтобы начать!";
        sendMessage(chatId, answer);

    }
    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/start");
        row.add("/help");

        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("/general");
        row.add("/love");
        row.add("/work");
        row.add("/situation");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }
    private String threeCards(int[] numberOfCards, String command) {
        StringBuilder result = new StringBuilder();
        result.append("Ваши карты: \n");
        for(int i = 0; i < numberOfCards.length; i++) {
            var taroCard = taroCardsRepository.findById(numberOfCards[i]);
            result.append(taroCard.get().getName() + " \n");
        }
        for(int i = 0; i < numberOfCards.length; i++) {
            var taroCard = taroCardsRepository.findById(numberOfCards[i]);
            switch (command){
                case "love" :
                    result.append(taroCard.get().getLoveSense() + " \n");
                    break;
                case "work" :
                    result.append(taroCard.get().getWorkSense() + " \n");
                    break;
                case "situation" :
                    result.append(taroCard.get().getGeneralSense());
                    break;
                default:
                    result.append("Something went wrong");
            }
        }
        return result.toString();

    }
}
