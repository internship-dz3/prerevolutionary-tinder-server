ALTER
    DATABASE "tinder-server" OWNER TO postgres;

SET
    statement_timeout = 0;
SET
    lock_timeout = 0;
SET
    idle_in_transaction_session_timeout = 0;
SET
    client_encoding = 'UTF8';
SET
    standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET
    check_function_bodies = false;
SET
    xmloption = content;
SET
    client_min_messages = warning;
SET
    row_security = off;

CREATE SCHEMA IF NOT EXISTS tinder;

ALTER
    SCHEMA tinder OWNER TO postgres;

SET
    default_tablespace = '';

SET
    default_table_access_method = heap;

CREATE TABLE tinder.favorite
(
    user_id     integer NOT NULL,
    favorite_id integer NOT NULL
);

ALTER TABLE tinder.favorite
    OWNER TO postgres;

CREATE TABLE tinder.dislike
(
    user_id          integer NOT NULL,
    disliked_user_id integer NOT NULL
);

ALTER TABLE tinder.dislike
    OWNER TO postgres;


CREATE TABLE tinder."user"
(
    id          integer                NOT NULL,
    telegram_id integer                NOT NULL,
    username    character varying(100) NOT NULL,
    age         integer                NOT NULL,
    description text                   NOT NULL,
    gender      character varying(10)  NOT NULL,
    look        character varying(10)  NOT NULL
);


ALTER TABLE tinder."user"
    OWNER TO postgres;


CREATE SEQUENCE tinder.user_id_seq
    AS integer
    START WITH 1000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER TABLE tinder.user_id_seq
    OWNER TO postgres;

ALTER SEQUENCE tinder.user_id_seq OWNED BY tinder."user".id;

ALTER TABLE ONLY tinder."user"
    ALTER COLUMN id SET DEFAULT nextval('tinder.user_id_seq'::regclass);

INSERT INTO tinder."user" (id, telegram_id, username, age, description, gender, look)
VALUES (1, 234, 'Инокентий', 32,
        'желает посредством брака сделать богатую невесту графиней. Затем согласен дать свободный вид на жительство',
        'MALE', 'ALL');
INSERT INTO tinder."user" (id, telegram_id, username, age, description, gender, look)
VALUES (2, 2342, 'Анна', 26,
        'Только что кончившая гимназию девица желает выйти замуж за холостого или бездетного вдовца с состоянием. Возраста не стесняться',
        'FEMALE', 'ALL');
INSERT INTO tinder."user" (id, telegram_id, username, age, description, gender, look)
VALUES (3, 23422, 'Гаврил', 42,
        'Поэт-безумец, мистический анархист, ходящий над безднами, призывает из далей ту, что дерзнет с ним рука об руку пройти житейский путь и познать все. Предложение серьезно',
        'MALE', 'ALL');
INSERT INTO tinder."user" (id, telegram_id, username, age, description, gender, look)
VALUES (4, 23224, 'Николай', 35,
        'Если бы я был богат, взял бы только бедную девушку в жены; но я бедный, интел. с высш. образов., агроном-техник, поляк, 35 лет, предлагаю себя в мужья только богатой девушке (не менее 100 000 р. капит.). Согласившаяся не пожалеет никогда сделанного выбора; анонимам не отвечу',
        'MALE', 'ALL');
INSERT INTO tinder."user" (id, telegram_id, username, age, description, gender, look)
VALUES (5, 2234, 'Екатерина', 32,
        'Так жизнь молодая проходит бесследно. А там и скоро конец. Мои девичьи грезы изменили мне. Стремилась к семейному очагу, но все рассеялось, как дым. И я одна, я всем чужая. Ищу мужа-друга',
        'FEMALE', 'ALL');
INSERT INTO tinder."user" (id, telegram_id, username, age, description, gender, look)
VALUES (6, 3478, 'Гоша', 28,
        'Молодой человек красивой наружности, имеющий своё крупное дело по издательству, желает жениться на особе, возраст безразличен, только здоровой.',
        'MALE', 'ALL');
INSERT INTO tinder."user" (id, telegram_id, username, age, description, gender, look)
VALUES (7, 323234, 'Виктор', 32,
        'Унтер-офицер желал бы сочетаться брачными узами с девицей, имеющей свой капитал. Свадьба на счёт невесты.',
        'MALE', 'ALL');
INSERT INTO tinder."user" (id, telegram_id, username, age, description, gender, look)
VALUES (8, 23234, 'Гаврил', 35,
        'Жену, компаньонку с капиталом в 5 тысяч, ищет солидный господин 35 лет, открывающий столовую. Дело обещает громадный успех.',
        'MALE', 'ALL');
INSERT INTO tinder."user" (id, telegram_id, username, age, description, gender, look)
VALUES (9, 1212313, 'Дарья', 32,
        'Согласна выйти замуж за того, кто обеспечит мне честное существование и защитит от обид.', 'ALL', 'ALL');
INSERT INTO tinder."user" (id, telegram_id, username, age, description, gender, look)
VALUES (10, 233223, 'Афананасий', 42,
        'Вдовец 42 лет желает жениться на девушке «без прошлого», образованной, знающей музыку и обладающей приятным голосом. Тёща не желательна.',
        'FEMALE', 'ALL');

SELECT pg_catalog.setval('tinder.user_id_seq', 10, true);

ALTER TABLE ONLY tinder.favorite
    ADD CONSTRAINT "user-favorite_pk" PRIMARY KEY (user_id, favorite_id);

ALTER TABLE ONLY tinder.dislike
    ADD CONSTRAINT dislike_pk PRIMARY KEY (user_id, disliked_user_id);


ALTER TABLE ONLY tinder."user"
    ADD CONSTRAINT user_pk PRIMARY KEY (id);

ALTER TABLE ONLY tinder.favorite
    ADD CONSTRAINT "user-favorite_fav_id_fk" FOREIGN KEY (favorite_id) REFERENCES tinder."user" (id);

ALTER TABLE ONLY tinder.favorite
    ADD CONSTRAINT "user-favorite_user_id_fk" FOREIGN KEY (user_id) REFERENCES tinder."user" (id);

ALTER TABLE ONLY tinder.dislike
    ADD CONSTRAINT dislike_user_id_fk FOREIGN KEY (user_id) REFERENCES tinder."user" (id);

ALTER TABLE ONLY tinder.dislike
    ADD CONSTRAINT dislike_user_id_fk_2 FOREIGN KEY (disliked_user_id) REFERENCES tinder."user" (id);