use camp;

-- 1. Mountains and Peaks
create table if not exists mountains (
	id int primary key auto_increment,
    name varchar(20)
);

create table if not exists peaks (
	id int primary key auto_increment,
    name varchar(20),
    mountain_id int,
    
    constraint foreign key (mountain_id)
		references mountains(id)
);

-- 2. Trip Organization
select driver_id, vehicle_type, concat(first_name, ' ', last_name) as driver_name from vehicles v
join campers c on v.driver_id = c.id;

-- 3. SoftUni Hiking
select 
	starting_point as 'route_starting_point', 
	end_point as 'route_end_point', 
    leader_id, 
    concat(first_name, ' ', last_name) 
from routes r
join campers c on r.leader_id = c.id;

-- 4. Delete Mountains
drop table mountains, peaks;

create table if not exists mountains (
	id int primary key auto_increment,
    name varchar(20)
);

create table if not exists peaks (
	id int primary key auto_increment,
    name varchar(20),
    mountain_id int,
    
    constraint foreign key (mountain_id)
		references mountains(id)
        on delete cascade
);
