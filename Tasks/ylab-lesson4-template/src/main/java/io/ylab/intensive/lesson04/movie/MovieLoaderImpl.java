package io.ylab.intensive.lesson04.movie;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;

public class MovieLoaderImpl implements MovieLoader {
    private final DataSource dataSource;

    public MovieLoaderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void loadData(File file) {
        readFile(file);
    }

    private void readFile(File file) {
        List<Movie> movies=new ArrayList<>();
        try (BufferedReader bufReader = new BufferedReader(new FileReader(file))) {
            String line = bufReader.readLine();//пропускаем 2 первые строки
            line = bufReader.readLine();
            while ((line = bufReader.readLine()) != null) {
                String[] movieFields = line.split(";");
                for (int i = 0; i < movieFields.length; i++) {
                    if (movieFields[i].isEmpty()) {
                        movieFields[i] = null;
                    }
                }
                Movie movie = parseRow(movieFields);
                movies.add(movie);
            }
                addToDataBase(movies);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addToDataBase(List<Movie> movies) {
        String insertQuery = "INSERT INTO movie (year,length,title,subject,actors,actress,director,popularity,awards) "
                           + "VALUES (?,?,?,?,?,?,?,?,?) ";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            connection.setAutoCommit(false);
            for (Movie movie: movies) {
                if (movie.getYear() != null) {
                    preparedStatement.setInt(1, movie.getYear());
                } else {
                    preparedStatement.setNull(1, Types.INTEGER);
                }
                if (movie.getLength() != null) {
                    preparedStatement.setInt(2, movie.getLength());
                } else {
                    preparedStatement.setNull(2, Types.INTEGER);
                }
                if (movie.getTitle() != null) {
                    preparedStatement.setString(3, movie.getTitle());
                } else {
                    preparedStatement.setNull(3, Types.VARCHAR);
                }
                if (movie.getSubject() != null) {
                    preparedStatement.setString(4, movie.getSubject());
                } else {
                    preparedStatement.setNull(4, Types.VARCHAR);
                }
                if (movie.getActors() != null) {
                    preparedStatement.setString(5, movie.getActors());
                } else {
                    preparedStatement.setNull(5, Types.VARCHAR);
                }
                if (movie.getActress() != null) {
                    preparedStatement.setString(6, movie.getActress());
                } else {
                    preparedStatement.setNull(6, Types.VARCHAR);
                }
                if (movie.getDirector() != null) {
                    preparedStatement.setString(7, movie.getDirector());
                } else {
                    preparedStatement.setNull(7, Types.VARCHAR);
                }
                if (movie.getPopularity() != null) {
                    preparedStatement.setInt(8, movie.getPopularity());
                } else {
                    preparedStatement.setNull(8, Types.INTEGER);
                }
                preparedStatement.setBoolean(9, movie.getAwards());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Movie parseRow(String[] movieFields) {
        System.out.println(Arrays.toString(movieFields));
        Movie movie = new Movie();
        if (movieFields[0] != null) {
            movie.setYear(Integer.parseInt(movieFields[0]));
        }
        if (movieFields[1] != null) {
            movie.setLength(Integer.parseInt(movieFields[1]));
        }
        movie.setTitle(movieFields[2]);
        movie.setSubject(movieFields[3]);
        movie.setActors(movieFields[4]);
        movie.setActress(movieFields[5]);
        movie.setDirector(movieFields[6]);
        if (movieFields[7] != null) {
            movie.setPopularity(Integer.parseInt(movieFields[7]));
        }
        movie.setAwards(movieFields[8] != null &&
                movieFields[8].equalsIgnoreCase("yes"));
        return movie;
    }
}
