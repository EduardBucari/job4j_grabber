package ru.job4j.utils;

import org.junit.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class SqlRuDateTimeParserTest {


    @Test
    public void yesterday() {
        LocalDateTime yday = new SqlRuDateTimeParser().parse("вчера, 04:01");
        LocalDateTime res = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(4, 1));
        assertEquals(res, yday);
    }

    @Test
    public void today() {
        LocalDateTime today = new SqlRuDateTimeParser().parse("сегодня, 04:01");
        LocalDateTime res = LocalDateTime.of(LocalDate.now(), LocalTime.of(4, 1));
        assertEquals(res, today);
    }

}