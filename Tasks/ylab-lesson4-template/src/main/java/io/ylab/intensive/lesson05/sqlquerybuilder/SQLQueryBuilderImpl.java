package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SQLQueryBuilderImpl implements SQLQueryBuilder {
    private DataSource dataSours;

    public SQLQueryBuilderImpl(DataSource dataSours) {
        this.dataSours = dataSours;
    }

    @Override
    public String queryForTable(String tableName) throws SQLException {
        try (Connection connection = dataSours.getConnection()) {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet resultSet = dbMetaData.getColumns(null, null, tableName, null);
            if (!resultSet.next()) {
                return null;
            }
            StringBuilder buildSqlQuery = new StringBuilder("SELECT ");
            do {
                buildSqlQuery.append(resultSet.getString("COLUMN_NAME"));
                buildSqlQuery.append(", ");
            } while (resultSet.next());
            int end = buildSqlQuery.length();
            buildSqlQuery.delete(end - 2, end - 1);//удаление последней запятой
            buildSqlQuery.append("FROM ");
            buildSqlQuery.append(tableName);
            return buildSqlQuery.toString();
        }
    }

    @Override
    public List<String> getTables() throws SQLException {
        try (Connection connection = dataSours.getConnection()) {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet resultSet = dbMetaData.getTables(null, null, "%", new String[]{"TABLE"});
            List<String> tables = new ArrayList<>();
            while (resultSet.next()) {
                tables.add(resultSet.getString("TABLE_NAME"));
            }
            return tables;
        }
    }
}
