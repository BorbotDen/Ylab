package io.ylab.intensive.lesson04.persistentmap;

import java.sql.SQLException;
import javax.sql.DataSource;

import io.ylab.intensive.lesson04.DbUtil;

public class PersistenceMapTest {
  public static void main(String[] args) throws SQLException {
    DataSource dataSource = initDb();
    PersistentMap persistentMap = new PersistentMapImpl(dataSource);

    persistentMap.init("set1");
    persistentMap.put("1","one");
    persistentMap.put("2","two");
    persistentMap.put("3","three");

    System.out.println("Ключи в set1 - "+persistentMap.getKeys());
    persistentMap.remove("2");
    System.out.println("Ключи в set1 после удаления 2 - "+persistentMap.getKeys());
    System.out.println("Есть ли ключь 1 в set1? - "+persistentMap.containsKey("1"));
    persistentMap.init("set2");
    System.out.println("сть ли ключь 1 в set2? - "+persistentMap.containsKey("1"));
    persistentMap.put("1","one");
    System.out.println("Добавили 1 в set2 . А теперь ? "+ persistentMap.containsKey("1"));
    persistentMap.put("1","newOne");
    System.out.println("Обновили значение по ключу 1 - "+persistentMap.get("1"));
    persistentMap.init("set1");
    persistentMap.clear();
    System.out.println("Очистили set1, вот список всех key - "+persistentMap.getKeys());
  }
  
  public static DataSource initDb() throws SQLException {
    String createMapTable = "" 
                                + "drop table if exists persistent_map; " 
                                + "CREATE TABLE if not exists persistent_map (\n"
                                + "   map_name varchar,\n"
                                + "   KEY varchar,\n"
                                + "   value varchar\n"
                                + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(createMapTable, dataSource);
    return dataSource;
  }
}
