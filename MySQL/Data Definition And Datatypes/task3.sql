create database gamebar;
use gamebar;

create table employees(
	id int primary key auto_increment,
    first_name varchar(255) not null,
    last_name varchar(255) not null
);

create table categories (
	id int primary key auto_increment,
    name varchar(255) not null
);

create table products (
	id int primary key auto_increment,
    name varchar(255) not null,
    category_id int not null
);

insert into employees (id, first_name, last_name) values
(1, 'ivan', 'ivanov'),
(2, 'ivan', 'ivanov'),
(3, 'ivan', 'ivanov');

alter table employees add column middle_name varchar(255) not null;

alter table employees modify column middle_name varchar(100) not null