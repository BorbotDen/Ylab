package io.ylab.intensive.lesson04.eventsourcing.db;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonDbImpl implements PersonDb {
    private static final String QUEUE_NAME = "personQueue";
    private static final String KEY_ADD="person.add";
    private static final String KEY_DEL="person.del";
    private final ConnectionFactory connectionFactory;
    private final DataSource dataSource;

    public PersonDbImpl(ConnectionFactory connectionFactory, DataSource dataSource) {
        this.connectionFactory = connectionFactory;
        this.dataSource = dataSource;
    }

    @Override
    public void trackingMessages() throws Exception {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            while (!Thread.currentThread().isInterrupted()) {
                GetResponse message = channel.basicGet(QUEUE_NAME, true);
                if (message != null) {
                    String received = new String(message.getBody());
                    Person person = Person.fromJson(received);
                    String action = message.getEnvelope().getRoutingKey();
                    defineAction(person,action);
                }
            }
        }
    }

    private void defineAction(Person person,String action) {
        if (action.equalsIgnoreCase(KEY_DEL)) {
            delete(person);
        }
        if (action.equalsIgnoreCase(KEY_ADD)){
            add(person);
        }
    }

    private void add(Person person) {
        if (isExist(person.getId())) {
            delete(person);
        }
        String insertQuery = "INSERT INTO person (person_id,first_name,last_name,middle_name) "
                + "VALUES (?,?,?,?) ";
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setLong(1, person.getId());
            preparedStatement.setString(2, person.getName());
            preparedStatement.setString(3, person.getLastName());
            preparedStatement.setString(4, person.getMiddleName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении в БД");
        }
    }

        private boolean isExist(Long id) {
        String insertQuery = "SELECT * FROM person WHERE person_id = ? ";
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Ошибка при поиске id в БД");
            return false;
        }
    }

    private void delete(Person person) {
        if (isExist(person.getId())) {
            String insertQuery = "DELETE FROM person WHERE person_id = ?";
            try (java.sql.Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setLong(1, person.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Ошибка удаления из БД");
            }
        } else {
            System.out.println("Удаление не удалось т.к. id не найден в БД");
        }
    }
}