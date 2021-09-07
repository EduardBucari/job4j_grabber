-- sql schema для проекта агрегатор.
-- В базе должна быть таблица post (id, name, text, link, created)
-- id - первичный ключ
-- name - имя вакансии
-- text - текст вакансии
-- link - текст, ссылка на вакансию
-- created - дата первого поста.
-- Нужно учесть дубликаты. Поле link будет уникальным кроме id

create table post(
id serial primary key,
name text,
text text,
link text unique,
created date
);