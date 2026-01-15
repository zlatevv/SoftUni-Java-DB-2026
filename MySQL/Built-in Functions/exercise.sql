-- Part I – Queries for SoftUni Database
use soft_uni;

-- 1. Find Names of All Employees by First Name
select first_name, last_name from employees
where lower(first_name) like "sa%"
order by employee_id;


-- 2. Find Names of All Employees by Last Name
select first_name, last_name from employees
where lower(last_name) like "%ei%"
order by employee_id;

-- 3. Find First Names of All Employees
select first_name from employees
where department_id in (3, 10) and year(hire_date) between 1995 and 2005
order by employee_id;

-- 4. Find All Employees Except Engineers
select first_name, last_name from employees
where not job_title like "%engineer%"
order by employee_id;

-- 5. Find Towns with Name Length
select name from towns
where length(name) in (5, 6)
order by name;

-- 6. Find Towns Starting With
select * from towns 
where lower(name) regexp "^[mkbe]"
order by name;

-- 7. Find Towns Not Starting With
select * from towns 
where not lower(name) regexp "^[rbd]"
order by name;

-- 8. Create View Employees Hired After 2000 Year
create view v_employees_hired_after_2000 as
select first_name, last_name from employees
where year(hire_date) > 2000;

-- 9. Length of Last Name
select first_name, last_name from employees
where length(last_name) = 5;

-- Part II – Queries for Geography Database 
use geography;

-- 10. Countries Holding 'A' 3 or More Times
select country_name, iso_code from countries
where length(country_name) - length(replace(lower(country_name), 'a', '')) >= 3
order by iso_code;

-- 11. Mix of Peak and River Names
select 
	peak_name, 
	river_name, 
    lower(concat(peak_name, substring(river_name, 2))) as mix 
from 
	peaks, 
    rivers
where 
	lower(right(peak_name, 1)) = lower(left(river_name, 1))
order by 
	mix;

-- Part III – Queries for Diablo Database
use diablo;
-- 12.	Games from 2011 and 2012 Year
select name, 
date_format(start, '%Y-%m-%d') as start
from games
where year(start) in (2011, 2012)
order by start, name
limit 50;

-- 13. User Email Providers
select user_name, 
substring_index(email, '@', -1) as provider
from users
order by provider, user_name;

-- 14. Get Users with IP Address Like Pattern
select user_name, ip_address from users
where ip_address like "___.1%.%.___" 
order by user_name;

-- 15. Show All Games with Duration and Part of the Day
select name,
case 
	when hour(start) >= 0 and hour(start) < 12 then 'Morning'
    when hour(start) >= 12 and hour(start) < 18 then 'Afternoon'
    else 'Evening'
end as 'Part of the Day',
case
	when duration <= 3 then 'Extra Short'
    when duration <= 6 then 'Short'
    when duration <= 10 then 'Long'
    else 'Extra Long'
end as 'Duration'
from games;

-- Part IV – Date Functions Queries
use orders;

-- 16. Orders Table
select product_name, order_date, 
date_add(order_date, interval 3 day) as pay_due, 
date_add(order_date, interval 1 month) as deliver_due
from orders