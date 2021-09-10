package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Преобразование даты.
 * Задание.
 * Реализовать метод, преобразующий дату из формата sql.ru
 *
 * Сайт sql.ru отображает дату в формате удобном для человека.
 * Такой формат Java не может преобразовать.
 * Нужно через методы String преобразовать строку в дату.
 *
 * Для проверки напишем тест.
 *
 */
public class SqlRuDateTimeParser implements DateTimeParser {

    // В колекцию Map вносим календарные данные(месяц-ключ, номер месяца-значение):
    private static final Map<String, String> MONTHS = Map.ofEntries(
            Map.entry("янв", "1"),
            Map.entry("фев", "2"),
            Map.entry("мар", "3"),
            Map.entry("апр", "4"),
            Map.entry("май", "5"),
            Map.entry("июн", "6"),
            Map.entry("июл", "7"),
            Map.entry("авг", "8"),
            Map.entry("сен", "9"),
            Map.entry("окт", "10"),
            Map.entry("ноя", "11"),
            Map.entry("дек", "12")
    );

    /**
     * Метод parse() описывает преобразование строки в дату.
     * @param parse на входе строка, которую необходимо преобразовать в дату,
     * @return на выходе дата(календарная).
     * - разбиваем входящую строку на две части по запятой,
     * - определяем формат даты и времени,
     * - создаем переменную в которой будет храниться дата,
     * - создаем переменную в которой будет храниться время,
     * - определяем условия для "сегодня", "вчера" и обычное,
     * - возвращаем экземпляр LocalDateTime из даты и времени.
     */
    @Override
    public LocalDateTime parse(String parse) {
        String[] data = parse.split(", ");
        String[] time = data[0].split(" ");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d M yy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate localDate;
        LocalTime localTime = LocalTime.parse(data[1], timeFormatter);
        if (time[0].equals("сегодня")) {
            localDate = LocalDate.now();
        } else if (time[0].equals("вчера")) {
            localDate = LocalDate.now().minusDays(1);
        } else {
            localDate = LocalDate.parse(String.format("%s %s %s",
                    time[0], MONTHS.get(time[1]), time[2]), dateTimeFormatter);
        }
        return LocalDateTime.of(localDate, localTime);
    }
}
