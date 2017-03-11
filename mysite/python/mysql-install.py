#!/usr/bin/python3
# mysql-install.py — скрипт для заповнення нової бази порожніми таблицями.
exec(open('mysql-connect.py').read()) # Підключення до MySQL. 
cursor.execute("""drop table if exists banip""") # Видалення таблиці "banip" якщо вона існує.
cursor.execute("""create table banip (ip VARCHAR (15) PRIMARY KEY, time INT);""") # Створення таблиці, в якій зберігатимуться дані про забанених по IP користувачів. В полі "ip" зберігатимуться IP-адреси; "time" — до якого часу забанено користувача, в кількості секунд від 1970 року.
cursor.execute("""drop table if exists users""") # Видалення таблиці "users" якщо існує.
cursor.execute("""create table users (login VARCHAR (32) PRIMARY KEY, pass VARCHAR (32), hash VARCHAR (32), ugroup VARCHAR (32), email VARCHAR (64));""") # Створення таблиці, в якій зберігатимуться дані про користувачів. У полі "login" — логін користувача, "pass" — пароль користувача, "hash" — хеш паролю, "ugroup" — група, до якої належить користувач, "email" — адреса елетронної пошти користувача.
cursor.execute("""insert into users (login,pass,ugroup,email) values ('b','a','admin','admin@localhost');""") # Внесення у БД запиту про акаунт адміністратора.
cursor.execute("""drop table if exists config;""") # Видалення таблиці "config" якщо існує.
cursor.execute("""create table config (parametr VARCHAR (16) PRIMARY KEY, znachen VARCHAR (128));""") # Створення таблиці, в якій зберігатимуться налаштування сайту. У полі "parametr" — назва параметру, "znachen" — його значення.
cursor.execute("""insert into config (parametr,znachen) values ('nazvasaitu','Мій сайт');""") # Внесення у таблицю з налаштуваннями сайту запису про назву сайту.
cursor.execute("""insert into config (parametr,znachen) values ('maxlist','2');""") # Внесення у таблицю з налаштуваннями сайту запису про максимальну кількість повідомлень на сторінці.
cursor.execute("""insert into config (parametr,znachen) values ('tryloginminute','3');""") # Внесення у таблицю з налаштуваннями сайту запису про максимальну кількість запитів на авторизацію, в хвилину.
cursor.execute("""insert into config (parametr,znachen) values ('opyssaitu','прівіт');""") # Внесення у таблицю з налаштуваннями сайту запису про опис сайту, який буде показаний в голові сторінки.
cursor.execute("""drop table if exists posts;""") # Видалення таблиці "posts" якщо існує.
cursor.execute("""create table posts (time INT PRIMARY KEY, text TEXT, zagolovok VARCHAR (128), cate VARCHAR (64), INDEX (cate));""") # Створення таблиці posts, де у полі "time" — зберігатиметься час повідомлень, "text" — їх текст, а у "zagolovok" — заголовки.
cursor.execute("""insert into posts (time,text,zagolovok,cate) values ('1325252516','<a href="http://www.ex.ua/view/75976">http://www.ex.ua/view/75976</a>','Музика із NFS: Underground 2','other');""") # Тест.
cursor.execute("""drop table if exists tryloginminute;""") # Видалення таблиці "tryloginminute" якщо існує.
cursor.execute("""create table tryloginminute (ip VARCHAR (15) PRIMARY KEY, lastminute INT, sprob INT (2));""") # Створення таблиці, в якій зберігатимуться кількість невдалих спроб спроб авторизації користувача за хвилину, з IP.  полі "ip" — IP-адреса користувача, "lastminute" — остання хвилина з авторизацією, "sprob" — кількість здійснених спроб за ту хвилину.
cursor.execute("""drop table if exists cate;""") # Видалення таблиці "cate" якщо існує.
cursor.execute("""create table cate (name VARCHAR (64), position INT, INDEX (position));""") # Створення таблиці, в якій зберігатиметься список категорій дописів, де у полі 'name' зберігатимуться імена категорій, 'position' — місцерозположення категорій (для сортування).
cursor.execute("""insert into cate (name,position) values ('other',0);""") # Створення першої категорії.