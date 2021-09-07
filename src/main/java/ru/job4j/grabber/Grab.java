package ru.job4j.grabber;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * Использовать quartz для запуска парсера.
 * Но напрямую мы не будем его использовать.
 * Абстрагируемся через интерфейс.
 * ru.job4j.grabber.Grab
 */
public interface Grab {
    void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException;
}
