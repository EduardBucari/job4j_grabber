package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Парсинг HTML страницы.
 * Задание.
 * Добавьте в программу вывод даты.
 * По техническому заданию мы должны получит данные с сайта - https://www.sql.ru/forum/job-offers
 * Библиотека jsoup позволяет извлечь текст из HTML по атрибутам тегов HTML.
 *
 * Задание.
 * Доработайте метод main. Парсить нужно первые 5 страниц.
 *
 */
public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= 5; i++) {
            String url = String.format("https://www.sql.ru/forum/job-offers/%s", i);
            Document doc = Jsoup.connect(url).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                Element element = td.parent();
                System.out.println(element.child(5).text());
            }
        }
    }
}
