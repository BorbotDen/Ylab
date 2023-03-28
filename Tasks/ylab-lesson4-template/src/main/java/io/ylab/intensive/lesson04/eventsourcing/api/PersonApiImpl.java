package io.ylab.intensive.lesson04.eventsourcing.api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.eventsourcing.Person;

import javax.sql.DataSource;

/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {
    private static final String EXCHANGE_NAME = "firstChange";
    private static final String QUEUE_NAME = "personQueue";
    private static final String KEY_ADD = "person.add";
    private static final String KEY_DEL = "person.del";
    ConnectionFactory connectionFactory;
    DataSource dataSource = DbUtil.buildDataSource();//для прямого подключения к БД

    public PersonApiImpl(ConnectionFactory connectionFactory) throws SQLException {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void deletePerson(Long personId) {
        Person person = new Person();
        person.setId(personId);
        try {
            sendMessage(person.toJson(), KEY_DEL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName) {
        Person person = new Person(personId, firstName, lastName, middleName);
        try {
            sendMessage(person.toJson(), KEY_ADD);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person findPerson(Long personId) {
        String insertQuery = "SELECT * FROM person WHERE person_id = ? ";
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setLong(1, personId);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getLong("person_id"));
                person.setName(resultSet.getString("first_name"));
                person.setLastName(resultSet.getString("last_name"));
                person.setMiddleName(resultSet.getString("middle_name"));
                return person;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Person> findAll() {
        List<Person> people = new ArrayList<>();
        String insertQuery = "SELECT * FROM person ";
        try (java.sql.Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getLong("person_id"));
                person.setName(resultSet.getString("first_name"));
                person.setLastName(resultSet.getString("last_name"));
                person.setMiddleName(resultSet.getString("middle_name"));
                people.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return people;
    }

    private void sendMessage(String message, String key) throws Exception {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, KEY_ADD);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, KEY_DEL);
            channel.basicPublish(EXCHANGE_NAME, key, null, message.getBytes());
        }
    }
}