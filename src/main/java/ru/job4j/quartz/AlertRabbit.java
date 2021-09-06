package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

/**
 * Quartz.
 * Задание.
 * 1. Доработать программу AlertRabbit, которая
 *    печатает на консоль текст через каждые 10 секунд.
 * 2. Нужно создать файл rabbit.properties
 *    При запуске программы нужно читать файл rabbit.properties
 *    и подставлять время периода запуска в расписание
 *    rabbit.interval=10
 * Важно! Чтение файла с настройками должно быть в отдельном методе.
 *
 */
public class AlertRabbit {

    // Дорабатываем программу:
    private static Properties properties = new Properties();
    private static JobDataMap data = new JobDataMap();

    public static Connection connect() {
        Connection connection = null;
        try {
            Class.forName(properties.getProperty("jdbc.driver"));
            connection = DriverManager.getConnection(
                    properties.getProperty("jdbc.url"),
                    properties.getProperty("jdbc.username"),
                    properties.getProperty("jdbc.password"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void main(String[] args) {
        readProperties();
        try (Connection connection = connect()) {
            /*
             * Разберем код:
             * 1. Конфигурирование.
             * Начало работы происходит с создания класса управляющего всеми работами.
             * В объект Scheduler мы будем добавлять задачи, которые хотим выполнять периодически.
             */
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            data.put("conn", connection);

            /*
             * 2. Создание задачи.
             * quartz каждый раз создает объект с типом org.quartz.Job.
             * Вам нужно создать класс реализующий этот интерфейс.
             */
            JobDetail job = newJob(Rabbit.class).build();

            /*
             * 3. Создание расписания.
             * Конструкция настраивает периодичность запуска.
             * В нашем случае, мы будем запускать задачу через 10 секунд
             * и делать это бесконечно.
             */
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(
                            Integer.parseInt(
                                    properties.getProperty("rabbit.interval")))
                    .repeatForever();

            /*
             * 4. Задача выполняется через триггер.
             * Здесь можно указать, когда начинать запуск.
             * Мы хотим сделать это сразу.
             */
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();

            /*
             * 5. Загрузка задачи и триггера в планировщик.
             * В итоге получаем программу, которая печатает
             * на консоль текст через 10 секунд.
             */
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (SchedulerException
                | InterruptedException
                | SQLException se) {
            se.printStackTrace();
        }
    }

    public static void readProperties() {
        try (InputStream in = AlertRabbit.class.getClassLoader()
                .getResourceAsStream("rabbit.properties")) {
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Внутри этого класса нужно описать требуемые действия.
     * В нашем случае - это вывод на консоль текста.
     */
    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ... ");
            Connection conn = (Connection) data.get("conn");
            try (PreparedStatement pStatement = conn.prepareStatement(
                  "insert into rabbit (created_date) values (?);")) {
                pStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                pStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}