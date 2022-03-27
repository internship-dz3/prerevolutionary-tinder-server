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

ALTER TABLE tinder.favorite OWNER TO postgres;

CREATE TABLE tinder.dislike
(
    user_id          integer NOT NULL,
    disliked_user_id integer NOT NULL
);

ALTER TABLE tinder.dislike OWNER TO postgres;

CREATE TABLE tinder.params
(
    id      integer                          NOT NULL,
    min_age integer               DEFAULT 14 NOT NULL,
    max_age integer               DEFAULT 100,
    gender  character varying(50) DEFAULT 'ALL':: character varying NOT NULL
);

ALTER TABLE tinder.params OWNER TO postgres;

CREATE SEQUENCE tinder."search-params_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

ALTER TABLE tinder."search-params_id_seq" OWNER TO postgres;

ALTER SEQUENCE tinder."search-params_id_seq" OWNED BY tinder.params.id;

CREATE TABLE tinder."user"
(
    id          integer                NOT NULL,
    username    character varying(100) NOT NULL,
    speciality  character varying(100) NOT NULL,
    age         integer                NOT NULL,
    description text                   NOT NULL,
    gender      character varying(10)  NOT NULL,
    params_id   integer
);


ALTER TABLE tinder."user" OWNER TO postgres;


CREATE SEQUENCE tinder.user_id_seq
    AS integer
    START WITH 1000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;


ALTER TABLE tinder.user_id_seq OWNER TO postgres;

ALTER SEQUENCE tinder.user_id_seq OWNED BY tinder."user".id;

ALTER TABLE ONLY tinder.params ALTER COLUMN id SET DEFAULT nextval('tinder."search-params_id_seq"'::regclass);

ALTER TABLE ONLY tinder."user" ALTER COLUMN id SET DEFAULT nextval('tinder.user_id_seq'::regclass);

INSERT INTO tinder.params (id, min_age, max_age, gender)
VALUES (1, 14, 100, 'ALL'),
       (2, 14, 100, 'ALL'),
       (3, 14, 100, 'ALL'),
       (4, 14, 100, 'ALL'),
       (5, 14, 100, 'ALL'),
       (6, 14, 100, 'ALL'),
       (7, 14, 100, 'ALL'),
       (8, 14, 100, 'ALL'),
       (9, 14, 100, 'ALL'),
       (10, 14, 100, 'ALL');

INSERT INTO tinder."user" (id, username, speciality, age, description, gender, params_id)
VALUES (1, 'Инокентий', 'Граф', 32,
        'желает посредством брака сделать богатую невесту графиней. Затем согласен дать свободный вид на жительство',
        'MALE', 1);
INSERT INTO tinder."user" (id, username, speciality, age, description, gender, params_id)
VALUES (2, 'Анна', 'Богиня', 26,
        'Только что кончившая гимназию девица желает выйти замуж за холостого или бездетного вдовца с состоянием. Возраста не стесняться',
        'FEMALE', 2);
INSERT INTO tinder."user" (id, username, speciality, age, description, gender, params_id)
VALUES (3, 'Гаврил', 'Офицер', 42,
        'Поэт-безумец, мистический анархист, ходящий над безднами, призывает из далей ту, что дерзнет с ним рука об руку пройти житейский путь и познать все. Предложение серьезно',
        'MALE', 3);
INSERT INTO tinder."user" (id, username, speciality, age, description, gender, params_id)
VALUES (4, 'Николай', 'Князь', 35,
        'Если бы я был богат, взял бы только бедную девушку в жены; но я бедный, интел. с высш. образов., агроном-техник, поляк, 35 лет, предлагаю себя в мужья только богатой девушке (не менее 100 000 р. капит.). Согласившаяся не пожалеет никогда сделанного выбора; анонимам не отвечу',
        'MALE', 4);
INSERT INTO tinder."user" (id, username, speciality, age, description, gender, params_id)
VALUES (5, 'Екатерина', 'Йоба', 32,
        'Так жизнь молодая проходит бесследно. А там и скоро конец. Мои девичьи грезы изменили мне. Стремилась к семейному очагу, но все рассеялось, как дым. И я одна, я всем чужая. Ищу мужа-друга',
        'FEMALE', 5);
INSERT INTO tinder."user" (id, username, speciality, age, description, gender, params_id)
VALUES (6, 'Гоша', 'Молодой миллионер', 28,
        'Молодой человек красивой наружности, имеющий своё крупное дело по издательству, желает жениться на особе, возраст безразличен, только здоровой.',
        'MALE', 6);
INSERT INTO tinder."user" (id, username, speciality, age, description, gender, params_id)
VALUES (7, 'Виктор', 'Солидный офицер', 32,
        'Унтер-офицер желал бы сочетаться брачными узами с девицей, имеющей свой капитал. Свадьба на счёт невесты.',
        'MALE', 7);
INSERT INTO tinder."user" (id, username, speciality, age, description, gender, params_id)
VALUES (8, 'Гаврил', 'Находка', 35,
        'Жену, компаньонку с капиталом в 5 тысяч, ищет солидный господин 35 лет, открывающий столовую. Дело обещает громадный успех.',
        'MALE', 8);
INSERT INTO tinder."user" (id, username, speciality, age, description, gender, params_id)
VALUES (9, 'Дарья', 'Жажду', 32,
        'Согласна выйти замуж за того, кто обеспечит мне честное существование и защитит от обид.', 'ALL', 9);
INSERT INTO tinder."user" (id, username, speciality, age, description, gender, params_id)
VALUES (10, 'Афананасий', 'Ничего не имею', 42,
        'Вдовец 42 лет желает жениться на девушке «без прошлого», образованной, знающей музыку и обладающей приятным голосом. Тёща не желательна.',
        'FEMALE', 10);

SELECT pg_catalog.setval('tinder."search-params_id_seq"', 1, true);

SELECT pg_catalog.setval('tinder.user_id_seq', 10, true);

ALTER TABLE ONLY tinder.params
    ADD CONSTRAINT "search-params_pk" PRIMARY KEY (id);

ALTER TABLE ONLY tinder.favorite
    ADD CONSTRAINT "user-favorite_pk" PRIMARY KEY (user_id, favorite_id);

ALTER TABLE ONLY tinder.dislike
    ADD CONSTRAINT dislike_pk PRIMARY KEY (user_id, disliked_user_id);


ALTER TABLE ONLY tinder."user"
    ADD CONSTRAINT user_pk PRIMARY KEY (id);

ALTER TABLE ONLY tinder.favorite
    ADD CONSTRAINT "user-favorite_fav_id_fk" FOREIGN KEY (favorite_id) REFERENCES tinder."user"(id);

ALTER TABLE ONLY tinder.favorite
    ADD CONSTRAINT "user-favorite_user_id_fk" FOREIGN KEY (user_id) REFERENCES tinder."user"(id);

ALTER TABLE ONLY tinder."user"
    ADD CONSTRAINT "user_search-params_id_fk" FOREIGN KEY (params_id) REFERENCES tinder.params(id);

ALTER TABLE ONLY tinder.dislike
    ADD CONSTRAINT dislike_user_id_fk FOREIGN KEY (user_id) REFERENCES tinder."user"(id);

ALTER TABLE ONLY tinder.dislike
    ADD CONSTRAINT dislike_user_id_fk_2 FOREIGN KEY (disliked_user_id) REFERENCES tinder."user"(id);