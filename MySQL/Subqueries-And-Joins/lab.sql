use soft_uni;

-- 1. Managers
select 
e.employee_id, 
concat_ws(' ', e.first_name, e.last_name) as 'full_name',
d.department_id,
d.name as 'department_name'
from employees e 
join departments d on e.employee_id = d.manager_id
order by employee_id
limit 5;

-- 2. Towns Addresses
select t.town_id, t.name as 'town_name', a.address_text from towns t
join addresses a on a.town_id = t.town_id
where t.name in ('San Francisco', 'Sofia', 'Carnation')
order by town_id, address_id;

-- 3. Employees Without Managers
select e.employee_id, e.first_name, e.last_name, e.department_id, e.salary from employees e
where manager_id is null;

-- 4. Higher Salary
select count(*) as count from employees
where salary > (select avg(salary) from employees);