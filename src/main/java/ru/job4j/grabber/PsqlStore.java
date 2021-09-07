package ru.job4j.grabber;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * PsqlStore
 * Задание.
 * 1. Реализуйте класс PsqlStore (Класс хранилища в базе данных) на основании интерфейса Store.
 * 2. Напишите метод main для демонстрации работы класса PsqlStore.
 */
public class PsqlStore implements Store, AutoCloseable {

    private final Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            /* cnn = DriverManager.getConnection(...); */
            cnn = DriverManager.getConnection(
                    cfg.getProperty("jdbc.url"),
                    cfg.getProperty("jdbc.username"),
                    cfg.getProperty("jdbc.password"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Метод save(Post post) сохраняет объявление в базу данных.
     * @param post Объявление типа Post.
     */
    @Override
    public void save(Post post) {
        try (PreparedStatement statement = cnn.prepareStatement(
                "insert into post (name, text, link, created) values (?, ?, ?, ?)")) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод getAll() читает все объявления из базы данных.
     * @return Список объявлений.
     */
    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = cnn.prepareStatement(
                "select * from post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("link"),
                            resultSet.getString("text"),
                            resultSet.getTimestamp("created").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * Метод findById(String id) обеспечивает поиск
     * записи в базе данных по уникальному идентификатору.
     * @param id Идентификатор записи.
     * @return Объявление найденное по id.
     */
    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement statement = cnn.prepareStatement(
                "select * from post where id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post = new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("link"),
                            resultSet.getString("text"),
                            resultSet.getTimestamp("created").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    /**
     * Метод close() обеспечивает закрытие ресурсов Connection.
     * @throws Exception Possible Exception.
     */
    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
        try (InputStream in = PsqlStore.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            Properties cfg = new Properties();
            cfg.load(in);
            PsqlStore psqlStore = new PsqlStore(cfg);
            Post post1 = new Post(
                    1,
                    "Заголовок1",
                    "Ссылка1",
                    "Описание1",
                    LocalDateTime.now().withNano(0)
            );
            Post post2 = new Post(
                    2,
                    "Заголовок2",
                    "Ссылка2",
                    "Описание2",
                    LocalDateTime.now().withNano(0).minusDays(1)
            );
            psqlStore.save(post1);
            psqlStore.save(post2);
            System.out.println(psqlStore.getAll());
            System.out.println("============");
            System.out.println(psqlStore.findById(post2.getId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}