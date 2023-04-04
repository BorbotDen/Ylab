package io.ylab.intensive.lesson05.messagefilter;

import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Component
public class MessageFilterImpl implements MessageFilter {
    private static final String EXCHANGE_NAME = "firstChange";
    private static final String QUEUE_INPUT = "inFilter";
    private static final String QUEUE_OUTPUT = "outFilter";
    private static final String KEY_INPUT = "message.in";
    private static final String KEY_OUTPUT = "message.out";
    private final WordsRepository wordsRepository;
    private final ConnectionFactory connectionFactory;

    @Autowired
    public MessageFilterImpl(WordsRepository wordsRepository, ConnectionFactory connectionFactory) {
        this.wordsRepository = wordsRepository;
        this.connectionFactory = connectionFactory;
    }

    public void input() {

    }

    @Override
    public void start() {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            while (!Thread.currentThread().isInterrupted()) {
                GetResponse message = channel.basicGet(QUEUE_INPUT, true);
                if (message != null) {
                    filterApply(new String(message.getBody()));
                }
            }
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private void filterApply(String oldMess) {
        String[] splitMessage = oldMess.split("[ ,.!?;:\\n]+");//пробела/точкой/запятой/точкой с запятой/вопросительным знаком/восклицательным знаком/концом строки
        //проверить в бд слова
        List<String> wordsForReplace = wordsRepository.checkMessage(splitMessage);
        //если есть сделать замену
        String newMess = oldMess;
        if (splitMessage.length != 0) {
            for (String word : splitMessage) {
                if (wordsForReplace.contains(word.toLowerCase())) {
                    StringBuilder strBuild = new StringBuilder(word);
                    strBuild.replace(1, (strBuild.length()-1),"*".repeat(strBuild.length()-2));
                    System.out.println(strBuild);
                    newMess = newMess.replaceAll(word,strBuild.toString());
                }
            }
            //  oldMess.replaceFirst(badWord,badWord.replaceAll("(?<=\\b\\w)\\w(?=\\w*\\b)", "*"));
        }
        outPut(wordsForReplace.toString()+"\n"+newMess);
    }

    private void outPut(String newMess) {
        try (Connection connection = connectionFactory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(QUEUE_OUTPUT, false, false, false, null);
            channel.queueBind(QUEUE_OUTPUT, EXCHANGE_NAME, KEY_OUTPUT);
            channel.basicPublish(EXCHANGE_NAME, KEY_OUTPUT, null, newMess.getBytes());
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
