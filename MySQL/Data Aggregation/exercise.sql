use gringotts;

-- 1. Records' Count
select count(*) as 'count' from wizzard_deposits;

-- 2. Longest Magic Wand
select max(magic_wand_size) as longest_magic_wand from wizzard_deposits;

-- 3. Longest Magic Wand Per Deposit Groups
select deposit_group, max(magic_wand_size) as longest_magic_wand from wizzard_deposits
group by deposit_group
order by longest_magic_wand, deposit_group;

-- 4. Smallest Deposit Group Per Magic Wand Size*
select deposit_group from wizzard_deposits
group by deposit_group
order by avg(magic_wand_size) 
limit 1;

-- 5. Deposits Sum
select deposit_group, sum(deposit_amount) as 'total_sum' from wizzard_deposits
group by deposit_group
order by total_sum;

-- 6. Deposits Sum for Ollivander Family
select deposit_group, sum(deposit_amount) as 'total_sum' from wizzard_deposits
where magic_wand_creator = 'Ollivander family'
group by deposit_group
order by deposit_group;

-- 7. Deposits Filter
select deposit_group, sum(deposit_amount) as 'total_sum' from wizzard_deposits
where magic_wand_creator = 'Ollivander family'
group by deposit_group
having total_sum < 150000
order by total_sum desc;

-- 8. Deposit Charge
select deposit_group, magic_wand_creator, min(deposit_charge) as min_deposit_charge from wizzard_deposits
group by deposit_group, magic_wand_creator
order by magic_wand_creator,deposit_group;

-- 9. Age Groups
select 
case 
	when age between 0 and 10 then '[0-10]'
    when age between 11 and 20 then '[11-20]'
    when age between 21 and 30 then '[21-30]'
    when age between 31 and 40 then '[31-40]'
    when age between 41 and 50 then '[41-50]'
    when age between 51 and 60 then '[51-60]'
    else '[61+]'
end as 'age_group',
count(*) as 'wizard_count'
from wizzard_deposits
group by age_group
order by wizard_count;

-- 10. First Letter
select distinct(left(first_name, 1)) as 'first_letter' from wizzard_deposits
where deposit_group = 'Troll Chest'
group by first_letter
order by first_letter;

-- 11. Average Interest 
select deposit_group, is_deposit_expired, avg(deposit_interest) as average_interest from wizzard_deposits
where deposit_start_date > '1985-01-01'
group by deposit_group, is_deposit_expired
order by deposit_group desc, is_deposit_expired;

-- 12. Employees Minimum Salaries
use soft_uni;

select department_id, min(salary) as minimum_salary from employees 
where department_id in (2, 5, 7) and hire_date > '2000-01-01'
group by department_id
order by department_id;

-- 13. Employees Average Salaries
CREATE TABLE high_paid_employees AS
SELECT *
FROM employees
WHERE salary > 30000;

DELETE FROM high_paid_employees
WHERE manager_id = 42;

UPDATE high_paid_employees
SET salary = salary + 5000
WHERE department_id = 1;

SELECT
    department_id,
    AVG(salary) AS avg_salary
FROM high_paid_employees
GROUP BY department_id
ORDER BY department_id ASC;

-- 14. Employees Maximum Salaries
select department_id, max(salary) as max_salary from employees
group by department_id
having not max_salary between 30000 and 70000
order by department_id;

-- 15. Employees Count Salaries
select count(*) as '' from employees where manager_id is null;

-- 16. 3rd Highest Salary*
select 	department_id, max(salary) as third_highest_salary from employees e
where salary = (
	select distinct salary from employees 
    where department_id = e.department_id
    order by salary desc
    limit 1 offset 2
)
order by department_id;

-- 17. Salary Challenge**
select first_name, last_name, department_id from employees e
where salary > (select avg(salary) from employees where department_id = e.department_id)
order by department_id, employee_id
limit 10;

-- 18. Departments Total Salaries
select department_id, sum(salary) as total_salary from employees
group by department_id
order by department_id;