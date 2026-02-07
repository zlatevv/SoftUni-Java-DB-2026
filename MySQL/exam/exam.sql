-- Section 1: Data Definition Language (DDL) – 40 pts
-- 01.	Table Design
drop database if exists go_roadie;
create database go_roadie;
use go_roadie;

create table cities (
	id int primary key auto_increment,
    name varchar(40) not null unique
);

create table cars (
	id int primary key auto_increment,
    brand varchar(20) not null,
    model varchar(20) not null unique
);

create table instructors (
	id int primary key auto_increment,
    first_name varchar(40) not null,
    last_name varchar(40) not null unique,
    has_a_license_from date not null
);

create table driving_schools (
	id int primary key auto_increment,
    name varchar(40) not null unique,
    night_time_driving boolean not null,
    average_lesson_price decimal(10, 2) null,
    car_id int not null,
    city_id int not null,
    
    constraint fk_car_school foreign key (car_id) references cars(id),
    constraint fk_school_city foreign key (city_id) references cities(id)
);

create table students (
	id int primary key auto_increment,
    first_name varchar(40) not null,
    last_name varchar(40) not null unique,
    age int null,
    phone_number varchar(20) null unique
);

create table instructors_driving_schools (
	instructor_id int null,
    driving_school_id int not null,
    
    constraint fk_instructor_driving_school foreign key (instructor_id) references instructors(id),
    constraint fk_driving_school foreign key (driving_school_id) references driving_schools(id)
);

create table instructors_students (
	instructor_id int not null,
    student_id int not null,
    
    constraint fk_instructor_student foreign key (instructor_id) references instructors(id),
    constraint fk_student_inctructor foreign key (student_id) references students(id)
);

-- Section 2: Data Manipulation Language (DML) – 30 pts
-- 02. Insert
insert into students (first_name, last_name, age, phone_number) 
select 
	lower(reverse(first_name)) as first_name,
    lower(reverse(last_name)) as last_name,
    age + cast(substring(phone_number, 1, 1) as unsigned) as age,
    concat_ws("", '1+', phone_number) as phone_number
from students
where age < 20;

-- 03. Update
update driving_schools ds
join cities c on ds.city_id = c.id
set average_lesson_price = average_lesson_price + 30
where c.name = 'London' and night_time_driving = true;

-- 04. Delete
delete from driving_schools ds
where ds.night_time_driving = false;

-- Section 3: Querying – 50 pts
select 
	concat_ws(" ", first_name, last_name) as full_name,
    age
from students
where first_name like "%a%"
order by age asc, id asc
limit 3;

-- 06. Driving schools without instructors
select ds.id, ds.name, c.brand from driving_schools ds
join cars c on c.id = ds.car_id
left join instructors_driving_schools ids on ids.driving_school_id = ds.id
where ids.instructor_id is null
order by c.brand asc, ds.id asc
limit 5;

-- 07. Instructors with more than one student
select i.first_name as first_name, i.last_name as last_name, count(ins.instructor_id) as students_count, c.name as city 
from instructors i
join instructors_driving_schools ids on ids.instructor_id = i.id
join driving_schools ds on ds.id = ids.driving_school_id
join cities c on c.id = ds.city_id
join instructors_students ins on ins.instructor_id = i.id
group by i.first_name, i.last_name, c.name
having count(ins.instructor_id) > 1
order by students_count desc, i.first_name asc;

-- 08. Instructors' count by city
select c.name, count(ids.driving_school_id) as instructors_count from cities c
join driving_schools ds on ds.city_id = c.id
join instructors_driving_schools ids on ids.driving_school_id = ds.id
group by c.name
having count(ids.driving_school_id) > 0
order by instructors_count desc;

-- 09. Instructors' experience level
select 
	concat_ws(" ", first_name, last_name) as full_name,
    case 
		when year(has_a_license_from) between 1980 and 1989 then 'Specialist'
        when year(has_a_license_from) between 1990 and 1999 then 'Advanced'
        when year(has_a_license_from) between 2000 and 2007 then 'Experienced'
        when year(has_a_license_from) between 2008 and 2014 then 'Qualified'
        when year(has_a_license_from) between 2015 and 2019 then 'Provisional'
        when year(has_a_license_from) >= 2020 then 'Trainee'
	end as level
from instructors
order by year(has_a_license_from) asc, first_name asc;

-- Section 4: Programmability – 30 pts
-- 10. Extract the average lesson price by city
delimiter //
create function udf_average_lesson_price_by_city (name VARCHAR(40))
returns decimal(10, 2)
deterministic
begin
	declare avg_price decimal(10, 2);
    
	select avg(ds.average_lesson_price) into avg_price from driving_schools ds 
    join cities c on ds.city_id = c.id
    where c.name = name;
    
    return avg_price;
end//

SELECT c.name, udf_average_lesson_price_by_city('London') as average_lesson_price
FROM cities c
WHERE c.name = 'London';

-- 11. Find a driving school by the desired car brand
delimiter //
create procedure udp_find_school_by_car(in brand varchar(20))
begin
	select ds.name, ds.average_lesson_price from driving_schools ds
    join cars c on c.id = ds.car_id
    where c.brand = brand
    order by ds.average_lesson_price desc;
end//

CALL udp_find_school_by_car('Mercedes-Benz');