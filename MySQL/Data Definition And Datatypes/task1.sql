create database movies;
use movies;

create table directors (
	id int primary key auto_increment,
    director_name varchar(30) not null,
    notes text
);

create table genres (
	id int primary key auto_increment,
    genre_name varchar(30) not null,
    notes text
);

create table categories (
	id int primary key auto_increment,
    category_name varchar(30) not null,
    notes text
);

create table movies (
	id int primary key auto_increment,
    title varchar(50) not null,
    copyright_year int,
    length time,
    genre_id int,
    category_id int,
    rating int,
    notes text,
    
    constraint foreign key (genre_id)
		references genres(id),
	
     constraint foreign key (category_id)
		references categories(id)
);

-- directors
INSERT INTO directors (id, director_name, notes) VALUES
(1, 'Christopher Nolan', 'Known for complex narratives'),
(2, 'Steven Spielberg', 'Famous blockbuster director'),
(3, 'Quentin Tarantino', 'Stylized violence and dialogue'),
(4, 'Martin Scorsese', 'Crime and biographical films'),
(5, 'James Cameron', 'Epic and sci-fi films');

-- genres
INSERT INTO genres (id, genre_name, notes) VALUES
(1, 'Action', NULL),
(2, 'Drama', NULL),
(3, 'Sci-Fi', 'Science fiction movies'),
(4, 'Thriller', NULL),
(5, 'Adventure', NULL);

-- categories
INSERT INTO categories (id, category_name, notes) VALUES
(1, 'Feature Film', NULL),
(2, 'Short Film', NULL),
(3, 'Documentary', NULL),
(4, 'Animation', NULL),
(5, 'Indie', NULL);

-- movies
INSERT INTO movies (title, copyright_year, length, genre_id, category_id, rating, notes) VALUES
('Inception', 2010, '02:28:00', 3, 1, 9, 'Dream-based thriller'),
('Jurassic Park', 1993, '02:07:00', 5, 1, 8, 'Dinosaurs come to life'),
('Pulp Fiction', 1994, '02:34:00', 4, 1, 9, 'Non-linear storytelling'),
('The Wolf of Wall Street', 2013, '03:00:00', 2, 1, 8, 'Based on a true story'),
('Avatar', 2009, '02:42:00', 3, 1, 8, 'Visually groundbreaking');

select * from movies