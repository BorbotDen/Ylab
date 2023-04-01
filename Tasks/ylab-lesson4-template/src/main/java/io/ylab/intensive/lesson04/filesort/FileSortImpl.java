package io.ylab.intensive.lesson04.filesort;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class FileSortImpl implements FileSorter {
    private static final String SORTED_FILE_DIR = "sortedFile.txt";
    private final DataSource dataSource;

    public FileSortImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Long> values = new ArrayList<>();

    @Override
    public File sort(File data) throws SQLException, IOException {
        long start = System.currentTimeMillis();
        readFile(data);
        long end = System.currentTimeMillis();
        System.out.println("Время чтения - " + (end - start) + " ms");

        start = System.currentTimeMillis();
        addValuesToDataBase();
        end = System.currentTimeMillis();
        System.out.println("Время записи в БД - " + (end - start) + " ms");

        start = System.currentTimeMillis();
        sortingValues();
        end = System.currentTimeMillis();
        System.out.println("Время сортировки - " + (end - start) + " ms");


        start = System.currentTimeMillis();
        File file = writeSortedFile();
        end = System.currentTimeMillis();
        System.out.println("Время записи нового файла - " + (end - start) + " ms");
        return file;
    }

    private void readFile(File data) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(data))) {
            String line;
            while ((line = reader.readLine()) != null) {
                values.add(Long.valueOf(line));
            }
        }
    }

    private void addValuesToDataBase() throws SQLException {
        String insertQuery = "INSERT INTO numbers (val) VALUES (?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            connection.setAutoCommit(false);
            for (Long val : values) {
                preparedStatement.setLong(1, val);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        }
    }

    private void sortingValues() throws SQLException {
        values.clear();
        String insertQuery = "SELECT val FROM numbers ORDER BY val DESC";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                values.add(resultSet.getLong("val"));
            }
        }
    }

    private File writeSortedFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SORTED_FILE_DIR))) {
            for (Long line : values) {
                writer.write(String.valueOf(line));
                writer.newLine();
            }
        }
        return new File(SORTED_FILE_DIR);
    }
}
