package io.ylab.intensive.lesson05.sqlquerybuilder;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SQLQueryExtenderTest {
  public static void main(String[] args) throws SQLException {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();
    System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));
    SQLQueryBuilder queryBuilder = applicationContext.getBean(SQLQueryBuilder.class);
    List<String> tables = queryBuilder.getTables();
    tables.add("noExistTable");
    // вот так сгенерируем запросы для всех таблиц что есть в БД
    for (String tableName : tables) {
      System.out.println(queryBuilder.queryForTable(tableName));
    }
  }
}
