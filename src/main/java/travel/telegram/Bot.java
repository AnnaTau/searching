package travel.telegram;

import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import travel.App;

import java.io.File;

public class Bot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update e) {
        Message msg = e.getMessage();
        String txt = msg.getText();
        if (txt.equals("/start")) {
            sendMsg(msg, "Hello, "+msg.getFrom().getFirstName()+" "+msg.getFrom().getLastName());
        }
        if (txt.contains("/findme")) {
            String to = txt.substring(8, txt.length());
            sendMsg(msg, "Wait, please...");
            sendDoc(msg, App.startSearch(to));
        }
    }

    @Override
    public String getBotUsername() {
        return "TravelCrawlerBot";
    }

    @Override
    public String getBotToken() {
        return "498129911:AAGXbnlBPmnt81AbNCUZ9RvC-VCH4VNx0x0";
    }

    private void sendMsg(Message msg, String text){
        SendMessage s = new SendMessage();
        s.setChatId(msg.getChatId()); // Боту может писать не один человек, и поэтому чтобы отправить сообщение, грубо говоря нужно узнать куда его отправлять
        s.setText(text);
        try {
            sendMessage(s);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    private void sendDoc(Message msg, File file){
        SendDocument s = new SendDocument();
        s.setChatId(msg.getChatId());
        s.setNewDocument(file);
        try {
            sendDocument(s);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
