package io.ylab.intensive.lesson05.messagefilter;

import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    private final Channel channel;

    @Autowired
    public MessageFilterImpl(WordsRepository wordsRepository, ConnectionFactory connectionFactory) {
        this.wordsRepository = wordsRepository;
        this.connectionFactory = connectionFactory;
        try {
            this.channel = connectionFactory.newConnection().createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(QUEUE_OUTPUT, false, false, false, null);
            channel.queueDeclare(QUEUE_INPUT, true, false, false, null);
            channel.queueBind(QUEUE_OUTPUT, EXCHANGE_NAME, KEY_OUTPUT);
            channel.queueBind(QUEUE_INPUT, EXCHANGE_NAME, KEY_INPUT);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                GetResponse message = channel.basicGet(QUEUE_INPUT, true);
                if (message != null) {
                    filterApply(new String(message.getBody()));
                }
            }
            channel.close();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private void filterApply(String oldMess) {
        String[] splitMessage = oldMess.split("[ ,.!?;\\r:\\n]+");
        List<String> wordsForReplace = wordsRepository.checkMessage(splitMessage);
        String newMess = oldMess;
        if (splitMessage.length != 0) {
            for (String word : splitMessage) {
                if (wordsForReplace.contains(word.toLowerCase())) {
                    StringBuilder strBuild = new StringBuilder(word);
                    strBuild.replace(1, (strBuild.length() - 1), "*".repeat(strBuild.length() - 2));
                    newMess = newMess.replaceAll(word, strBuild.toString());
                }
            }
        }
        outPut(wordsForReplace.toString() + "\n" + newMess);
    }

    private void outPut(String newMess) {
        try {
            channel.basicPublish(EXCHANGE_NAME, KEY_OUTPUT, null, newMess.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
