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
 */
public class SqlRuParse {
    public static void main(String[] args) throws Exception {

        ////получаем document
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();

        // Ячейка с именем имеет аттрибут class=postslisttopic
        // jsoup может извлечь все элементы с этим аттрибутом.
        // выбираем в document элементы с необходимым id
        Elements row = doc.select(".postslisttopic");

        // выполним поиск следующим образом:
        for (Element td : row) {   // проходимся по этим элементам
            Element href = td.child(0); // спускаемся вниз по дереву (child) и берем 0 элемент (в нашем случае их два)
            System.out.println(href.attr("href")); // первый элемент - ссылка,
            System.out.println(href.text()); // второй элемент - текст ссылки.

            // Выводим дату обновления поста:
            // поднимаемся вверх по дереву и берем пятый элемент,
            Element date = td.parent().child(5);
            System.out.println(date.text()); //выводим дату в виде текста.
        }
    }
}
