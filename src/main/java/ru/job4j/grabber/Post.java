package ru.job4j.grabber;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Архитектура проекта - Аргегатор Java Вакансий.
 * Первичным элементом системы является модель данных - Post,
 * Модель должна иметь поля:
 *  - id типа int - идентификатор вакансии (берется из нашей базы данных);
 *  - title типа String - название вакансии;
 *  - link типа String - ссылка на описание вакансии;
 *  - description типа String - описание вакансии;
 *  - created типа LocalDateTime - дата создания вакансии.
 *
 *  Наш проект будет хранить данные в базе Postgresql.
 *  Связь с базой будет осуществляться через интерфейс.
 *  ru.job4j.grabber.Store.
 *
 *  Операция извлечения данных с  сайта описывается отдельным интерфейсом.
 *  ru.job4j.grabber.Parse
 *
 *  В этом проекты мы будем использовать quartz для запуска парсера.
 *  Но напрямую мы не будем его использовать.
 *  Абстрагируемся через интерфейс.
 *  ru.job4j.grabber.Grab
 */
public class Post {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private int id;
    private String title;
    private String link;
    private String description;
    private LocalDateTime created;

    public Post() {
    }

    // Constructor:
    public Post(int id, String title, String link,
                String description, LocalDateTime created) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.created = created;
    }

    // Getters & Setters:
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    // equals & hashCode:
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return id == post.id
                && Objects.equals(title, post.title)
                && Objects.equals(link, post.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, link);
    }

    // toString
    @Override
    public String toString() {
        return String.format(
                "id: %s%ntitle: %s%nlink: %s%ndescription: %s%ncreated: %s%n",
                id, title, link, description, FORMATTER.format(created));
    }
}
