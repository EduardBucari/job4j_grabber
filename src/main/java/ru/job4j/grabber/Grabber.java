package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import ru.job4j.html.SqlRuParse;

import java.io.*;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Grabber.
 * Задание:
 * В этом задании мы соединим отдельные части в целое приложение.
 * Реализовать класс Grabber. Он должен выполнять все действия из технического задания.
 *
 * Описание:
 * Программа считывает все вакансии относящиеся к Java и записывает их в базу.
 * Первый сайт sql.ru. В нем есть раздел job. "https://www.sql.ru/forum/job-offers/1"
 * Система запускается по расписанию.
 * Период запуска указывается в настройках - app.properties.
 * Доступ к интерфейсу через REST API.
 * Приложение собирается в jar.
 *
 * Расширение:
 * 1. Можно добавить новые сайты без изменения кода.
 * 2. Можно сделать параллельный парсинг сайтов.
 */
public class Grabber implements Grab {
    private final Properties cfg = new Properties();
    private static String link;
    private static String request;

    public Grabber(String link, String request) {
        Grabber.link = link;
        Grabber.request = request;
    }

    /**
     * Метод store() инициализации хранилища в базе данных.
     * @return Объект хранилища.
     */
    public Store store() {
        return new PsqlStore(cfg);
    }

    /**
     * Метод scheduler() создания планировщика.
     * @return Объект планировщика.
     * @throws SchedulerException Possible exception.
     */
    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    /**
     * Инициализация проперти.
     * @throws IOException Possible exception.
     */
    public void cfg() throws IOException {
        try (InputStream in = Grabber.class.getClassLoader()
             .getResourceAsStream("app.properties")) {
            cfg.load(in);
            request = request.toLowerCase();
        }
    }

    /**
     * Метод init() инициализации граббера.
     *
     * @param parse    Объект типа Parse.
     * @param store     Объект типа Store.
     * @param scheduler Объект типа Scheduler.
     * @throws SchedulerException Possible exception.
     */
    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(cfg.getProperty("time")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static class GrabJob implements Job {

        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            /* TODO impl logic */
            List<Post> posts = parse.list(link);
            for (Post p : posts) {
                if (p.getTitle().toLowerCase().contains(request)) {
                    store.save(parse.detail(p.getLink()));
                }
            }
        }
    }

    public static void main(String[] args) throws  Exception {
        Grabber grab = new Grabber(
                "https://www.sql.ru/forum/job-offers/1",
                "Java");
        grab.cfg();
        Scheduler scheduler = grab.scheduler();
        Store store = grab.store();
        grab.init(new SqlRuParse(new SqlRuDateTimeParser()), store, scheduler);
    }
}