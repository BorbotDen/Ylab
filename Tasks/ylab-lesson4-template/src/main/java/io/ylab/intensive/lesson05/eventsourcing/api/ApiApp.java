package io.ylab.intensive.lesson05.eventsourcing.api;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApiApp {
    public static void main(String[] args) throws Exception {
        // Тут пишем создание PersonApi, запуск и демонстрацию работы
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        PersonApi personApi = applicationContext.getBean(PersonApi.class);
        // пишем взаимодействие с PersonApi
        //Добавление в БД
        personApi.savePerson(5L, "Den", "Bor", "Anatolich");
        personApi.savePerson(6L, "Jon", "Karter", "Marsianin");
        personApi.savePerson(7L, "RIKI", "TIKI", "TAVI");
        //перезапись
        personApi.savePerson(7L, "Riki", "Tiki", "Tavi");
        //Методы принимают данные на прямую из БД
        System.out.println(personApi.findPerson(7L).toString());
        System.out.println(personApi.findAll());
        //Удаление несуществующего
        personApi.deletePerson(1L);
    }
}
