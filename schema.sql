CREATE TYPE MEDIA_TYPE AS ENUM ('image', 'video');

CREATE TABLE IF NOT EXISTS Games(
    id_game SERIAL,
    title VARCHAR(50) NOT NULL,
    title_eng VARCHAR(50) NULL,
    description TEXT NOT NULL,
    description_eng TEXT NOT NULL,
    file varchar(255) NULL,
    is_third_party_link BOOLEAN not null DEFAULT(false),
    added_date TIMESTAMP NOT NULL DEFAULT(now()),
    year INT NOT NULL DEFAULT(date_part('year', CURRENT_DATE)),
    likes INT NOT NULL default(0),
    side_quest_link varchar(255) NULL,
    preview varchar(255) NULL,

    PRIMARY KEY (id_game),
    unique (title)
    );

CREATE TABLE IF NOT EXISTS Metrics(
    id_game INT NOT NULL,
    visits INT NOT NULL DEFAULT(0),
    downloads INT NOT NULL DEFAULT(0),
    last_change TIMESTAMP NOT NULL DEFAULT(now()),

    PRIMARY KEY (id_game),
    FOREIGN KEY (id_game) REFERENCES Games(id_game) ON DELETE CASCADE

);

CREATE TABLE IF NOT EXISTS Medias(
    id SERIAL,
    id_game INT NOT NULL,
    type MEDIA_TYPE NOT NULL,
    file varchar(255) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (id_game) REFERENCES Games(id_game) ON DELETE CASCADE
);
