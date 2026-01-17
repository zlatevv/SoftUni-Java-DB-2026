-- 1. Departments Info
select department_id, count(*) as 'Number of employees' from employees
group by department_id
order by department_id;

-- 2. Average Salary
select department_id, round(avg(salary), 2) as 'Average Salary' from employees
group by department_id
order by department_id;

-- 3. Min Salary
select department_id, round(min(salary), 2) as 'Min Salary' from employees
where salary > 800
group by department_id
order by department_id
limit 1;

-- 4. Appetizers Count
select count(*) from products
where category_id = 2 and price > 8;

-- 5. Menu Prices
select
category_id,
round(avg(price), 2) as 'Average Price',
round(min(price), 2) as 'Cheapest Product',
round(max(price), 2) as 'Most Expensive Product'
from products
group by category_id