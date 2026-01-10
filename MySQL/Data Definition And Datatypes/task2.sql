create database soft_uni;
use soft_uni;

create table towns (
	id int primary key auto_increment,
    name varchar(50)
);

create table addresses (
	id int primary key auto_increment,
    address_text text,
    town_id int,
    
    constraint foreign key (town_id) references towns(id)
);

create table departments (
	id int primary key auto_increment,
    name varchar(50)
);

create table employees (
	id int primary key auto_increment,
    first_name varchar(255),
    middle_name varchar(255),
    last_name varchar(255),
    job_title varchar(255),
    department_id int,
    hire_date date,
    salary decimal(7, 2),
    address_id int,
    
    constraint foreign key (department_id) references departments(id),
    constraint foreign key (address_id) references addresses(id)
);

insert into towns values (1, 'Sofia'), (2, 'Plovdiv'), (3, 'Varna'), (4, 'Burgas');
insert into departments values (1, 'Engineering'), (2, 'Sales'), (3, 'Marketing'), (4, 'Software Development'), (5, 'Quality Assurance');
insert into employees (first_name, middle_name, last_name, job_title, department_id, hire_date, salary) 
values
('Ivan', 'Ivanov', 'Ivanov', '.NET Developer', 4, '2013-02-01', 3500.00),
('Petar', 'Petrov', 'Petrov', 'Senior Engineer', 1, '2004-03-02', 4000.00),
('Maria', 'Petrova', 'Ivanova', 'Intern', 5, '2016-08-28', 525.25),
('Georgi', 'Terziev', 'Ivanov', 'CEO', 2, '2007-12-09', 3000.00),
('Peter', 'Pan', 'Pan', 'Intern', 3, '2016-08-28', 599.88);


select * from towns;
select * from departments;
select * from employees;

select * from towns
order by name;
select * from departments
order by name;
select * from employees
order by salary desc;

select name from towns order by name;
select name from departments order by name;
select first_name, last_name, job_title, salary from employees order by salary desc;

update employees
set salary = salary * 1.1;

select salary from employees;