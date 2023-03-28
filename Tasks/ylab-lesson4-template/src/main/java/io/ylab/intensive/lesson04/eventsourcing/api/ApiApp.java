package io.ylab.intensive.lesson04.eventsourcing.api;

import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.RabbitMQUtil;

public class ApiApp {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = initMQ();
        PersonApi messageOut = new PersonApiImpl(connectionFactory);
        //Добавление в БД
        messageOut.savePerson(5L, "Den", "Bor", "Anatolich");
        messageOut.savePerson(6L, "Jon", "Karter", "Marsianin");
        messageOut.savePerson(7L, "RIKI", "TIKI", "TAVI");
        //перезапись
        messageOut.savePerson(7L, "Riki", "Tiki", "Tavi");
        //Методы принимают данные на прямую из БД
        System.out.println(messageOut.findPerson(7L));
        System.out.println(messageOut.findAll());
        //Удаление несуществующего
        messageOut.deletePerson(1L);
    }

    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }
}
