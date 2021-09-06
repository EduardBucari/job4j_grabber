package ru.job4j.post;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Загрузка деталей поста.
 * Задание.
 * Создайте метод для загрузки деталей объявления.
 *
 * Мы научились получать ссылку для поста.
 * Теперь нужно научиться извлекать данные по этой ссылке:
 * https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t
 * Что нужно для Post?
 * - Описание.
 * - Дата создания.
 */
public class SqlRuPostParser {

    public void postParser(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements row = doc.select(".msgBody");
            Element description = row.get(1);
            System.out.println(description.text());
            Elements dates = doc.select(".msgFooter");
            String date = dates.get(0).text();
            int indexLast = date.indexOf("[") - 1;
            date = date.substring(0, indexLast);
            System.out.println(date);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SqlRuPostParser postParser = new SqlRuPostParser();
        postParser.postParser("https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t");
    }
}
