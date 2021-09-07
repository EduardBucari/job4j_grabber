package ru.job4j.grabber;

import java.util.List;

/**
 * Хранилище.
 * Интерфейсы позволяют избавиться от прямой зависимости.
 * На первом этапе можно использовать MemStore - хранение данных в памяти.
 */
public interface Store {

    // Метод save() - сохраняет объявление в базе.
    void save(Post post);

    // Метод getAll() - позволяет извлечь объявления из базы.
    List<Post> getAll();

    // Метод findById(int id) - позволяет извлечь объявление из базы по id.
    Post findById(int id);
}
