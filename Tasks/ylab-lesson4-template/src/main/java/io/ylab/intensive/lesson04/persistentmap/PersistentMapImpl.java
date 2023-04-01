package io.ylab.intensive.lesson04.persistentmap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * Класс, методы которого надо реализовать
 */
public class PersistentMapImpl implements PersistentMap {
    private String mapName;
    private final DataSource dataSource;

    public PersistentMapImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void init(String name) {
        this.mapName = name;  //имя активной Map
    }

    @Override
    public boolean containsKey(String key) throws SQLException {
        String insertQuery = "SELECT key FROM persistent_map WHERE map_name = ? AND key = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, mapName);
            preparedStatement.setString(2, key);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            return resultSet.next();
        }
    }

    @Override
    public List<String> getKeys() throws SQLException {
        List<String> keys = new ArrayList<>();
        String insertQuery = "SELECT key FROM persistent_map WHERE map_name = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, mapName);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                keys.add(resultSet.getString("KEY"));
            }
            if (keys.isEmpty()) {
                return null;
            } else {
                return keys;
            }
        }
    }

    @Override
    public String get(String key) throws SQLException {
        String insertQuery = "SELECT value FROM persistent_map WHERE map_name = ? AND key = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, mapName);
            preparedStatement.setString(2, key);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return resultSet.getString("value");
            } else {
                return null;
            }
        }
    }

    @Override
    public void remove(String key) throws SQLException {
        String insertQuery = "DELETE FROM persistent_map WHERE map_name = ? AND key = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, mapName);
            preparedStatement.setString(2, key);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void put(String key, String value) throws SQLException {

        if (containsKey(key)) {
            remove(key);
        }
        String insertQuery = "INSERT INTO persistent_map (map_name,key,value) "
                + "VALUES (?,?,?) ";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, mapName);
            preparedStatement.setString(2, key);
            preparedStatement.setString(3, value);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void clear() throws SQLException {
        String insertQuery = "DELETE FROM persistent_map WHERE map_name = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, mapName);
            preparedStatement.executeUpdate();
        }
    }
}
