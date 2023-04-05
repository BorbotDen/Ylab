package io.ylab.intensive.lesson05.messagefilter;

import io.ylab.intensive.lesson05.DbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class WordsRepository {
    private static final String TABLE_NAME = "bad_words";
    private final File dataFile;
    private final DataSource dataSource;
    private final List<String> words = new ArrayList<>();
@Autowired
    public WordsRepository(File dataFile, DataSource dataSource) {
    this.dataFile = dataFile;
    this.dataSource = dataSource;
    try (Connection connection = dataSource.getConnection()) {
        DatabaseMetaData dbMetaData = connection.getMetaData();
        ResultSet resultSet = dbMetaData.getTables(null, null, TABLE_NAME.toUpperCase(), new String[]{"TABLE"});
        if (!resultSet.next()) {
            initDb();
        }
        readDataFile();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

    public void readDataFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        saveInDataBase();
    }

    public List<String> checkMessage(String[] splitMessage) {
        List<String> badWords = new ArrayList<>();
        String insertQuery = "SELECT word FROM bad_words WHERE LOWER (word) IN (" + getSomeQustChar(splitMessage.length) + ")";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            for (int i = 0; i < splitMessage.length; i++) {
                statement.setString(i + 1, splitMessage[i].toLowerCase());
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                badWords.add(resultSet.getString("word"));
            }
            return badWords;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getSomeQustChar(int length) {
        if (length < 1) {
            return "";
        }
        return "?" + ",?".repeat(length - 1);
    }

    private void saveInDataBase() {
        try (Connection connection = dataSource.getConnection()) {
            String insertQuery = ""
                    + "INSERT INTO "
                    + TABLE_NAME
                    + " (word) "
                    + "VALUES "
                    + "(?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            connection.setAutoCommit(false);
            for (String word : words) {
                preparedStatement.setString(1, word);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initDb() throws SQLException {
        String ddl = ""
                + "drop table if exists " + TABLE_NAME + ";"
                + "create table if not exists " + TABLE_NAME + " (\n"
                + "word varchar\n"
                + ")";
        DbUtil.applyDdl(ddl, dataSource);
    }
}
