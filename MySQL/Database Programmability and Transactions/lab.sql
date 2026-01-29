
-- Task 01
delimiter //
create function ufn_count_employees_by_town(town_name varchar(50))
returns int
deterministic
begin
	declare employees_count int;
    select count(*) into employees_count from employees e
    join addresses a on a.address_id = e.address_id
    join towns t on t.town_id = a.town_id
    where t.name = town_name;
	return employees_count;
end//

-- Task 02
delimiter //
create procedure usp_raise_salaries(department_name varchar(50))
begin
	update employees e
    join departments d on d.department_id = e.department_id
    set salary = salary * 1.05
    where d.name = department_name;
end//

-- Task 03
delimiter //
create procedure usp_raise_salary_by_id(id int)
begin
	update employees e
    set salary = salary * 1.05
    where employee_id = id;
end//

-- Task 04
CREATE TABLE deleted_employees(
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    middle_name VARCHAR(20),
    job_title VARCHAR(50),
    department_id INT,
    salary DOUBLE
);

CREATE TRIGGER tr_deleted_employees
AFTER DELETE
ON employees
FOR EACH ROW
BEGIN
    INSERT INTO deleted_employees (first_name,last_name,
    middle_name,job_title,department_id,salary)
    VALUES(OLD.first_name,OLD.last_name,OLD.middle_name,
    OLD.job_title,OLD.department_id,OLD.salary);
END