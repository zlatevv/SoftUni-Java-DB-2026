use soft_uni;

-- 1. Employee Address
select e.employee_id, e.job_title, e.address_id, a.address_text
from employees e
join addresses a on e.address_id = a.address_id
order by a.address_id
limit 5;

-- 2. Addresses with Towns
select e.first_name, e.last_name, t.name as town, a.address_text
from employees e
join addresses a on e.address_id = a.address_id
join towns t on t.town_id = a.town_id
order by first_name asc, last_name asc 
limit 5;

-- 3. Sales Employee
select e.employee_id, e.first_name, e.last_name, d.name as department_name
from employees e
join departments d on e.department_id = d.department_id
where d.name = "Sales"
order by e.employee_id desc;

-- 4. Employee Departments
select e.employee_id, e.first_name, e.salary, d.name as department_name
from employees e
join departments d on e.department_id = d.department_id
where e.salary > 15000 
order by d.department_id desc
limit 5;

-- 5. Employees Without Project
select e.employee_id, e.first_name from employees e
left join employees_projects ep on ep.employee_id = e.employee_id
where ep.employee_id is null
order by e.employee_id desc
limit 3;


-- 6. Employees Hired After
select e.first_name, e.last_name, e.hire_date, d.name as dept_name
from employees e
join departments d on e.department_id = d.department_id
where d.name in ("Sales", "Finance") and e.hire_date > '1999-01-01'
order by e.hire_date asc;

-- 7. Employees with Project
select e.employee_id, e.first_name, p.name as project_name from employees e
join employees_projects ep on ep.employee_id = e.employee_id
join projects p on p.project_id = ep.project_id
where p.start_date >= '2002-08-14' and p.end_date is null
order by e.first_name asc, project_name asc
limit 5;

-- 8. Employee 24
select e.employee_id, e.first_name, 
case 
	when p.start_date > '2004-12-31 23:59:59' then NULL
    else p.name
	end as project_name
from employees e
join employees_projects ep on ep.employee_id = e.employee_id
join projects p on p.project_id = ep.project_id
where ep.employee_id = 24
order by p.name;

-- 9. Employee Manager
select e.employee_id, e.first_name, e.manager_id, m.first_name as manager_name from employees e
join employees m on e.manager_id = m.employee_id
where m.employee_id in (3, 7)
order by e.first_name asc;

-- 10. Employee Summary
select e.employee_id, 
concat_ws(" ", e.first_name, e.last_name) as employee_name, 
concat_ws(" ", m.first_name, m.last_name) as manager_name, 
d.name as department_name from employees e
join employees m on e.manager_id = m.employee_id
join departments d on e.department_id = d.department_id
order by e.employee_id
limit 5;

-- 11. Min Average Salary
select min(avg_salary) as lowest_average_salary 
from (
	select avg(salary) as avg_salary from employees group by department_id
) as dept_avg;

-- geography database tasks
use geography;

-- 12. Highest Peaks in Bulgaria
select c.country_code, m.mountain_range, p.peak_name, p.elevation from countries c 
join mountains_countries mc on mc.country_code = c.country_code
join mountains m on mc.mountain_id = m.id
join peaks p on p.mountain_id = m.id
where c.country_code = 'BG' and p.elevation > 2835
order by p.elevation desc;

-- 13. Count Mountain Ranges
select c.country_code, count(m.mountain_range) as mountain_range_count from countries c
join mountains_countries mc on mc.country_code = c.country_code
join mountains m on m.id = mc.mountain_id
where c.country_code in ('BG', 'RU', 'US')
group by c.country_code
order by mountain_range_count desc;

-- 14. Countries with Rivers
select c.country_name, r.river_name from countries c
left join countries_rivers cr on c.country_code = cr.country_code
left join rivers r on r.id = cr.river_id
where c.continent_code = 'AF'
order by c.country_name asc
limit 5;

-- 16. Countries Without Any Mountains
select count(*) from countries c
left join mountains_countries mc on mc.country_code = c.country_code
where mc.mountain_id is null;

-- 17. Highest Peak and Longest River by Country
select c.country_name, max(p.elevation) as highest_peak_elevation, max(r.length) as longest_river_length from countries c
join countries_rivers cr on cr.country_code = c.country_code
join mountains_countries mc on mc.country_code = c.country_code
join mountains m on m.id = mc.mountain_id
join rivers r on r.id = cr.river_id 
join peaks p on p.mountain_id = m.id
group by c.country_name
order by highest_peak_elevation desc, longest_river_length desc, c.country_name
limit 5;