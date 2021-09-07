package ru.job4j.grabber;

import java.util.List;

/**
 * Интерфейс описывающий парсинг сайта.
 */
public interface Parse {

    /**
     * Метод list загружает список всех постов.
     *
     * @param link Ссылка на страницу с постами.
     * @return Список постов.
     */
    List<Post> list(String link);

    /**
     * Метод detail загружает все детали одного поста.
     *
     * @param link Ссылка на страницу поста.
     * @return Объект с деталями поста.
     */
    Post detail(String link);
}
