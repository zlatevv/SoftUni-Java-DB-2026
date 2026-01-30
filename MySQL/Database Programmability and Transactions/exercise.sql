-- Part I – Queries for SoftUni Database
use soft_uni

-- 1. Employees with Salary Above 35000
delimiter //
create procedure usp_get_employees_salary_above_35000()
begin
	select first_name, last_name from employees
    where salary > 35000
    order by first_name asc, last_name asc, employee_id asc;
end//

-- 2. Employees with Salary Above Number
delimiter //
create procedure usp_get_employees_salary_above(given_salary decimal(19, 4))
begin
	select first_name, last_name from employees
    where salary >= given_salary
    order by first_name asc, last_name asc, employee_id asc;
end//

call usp_get_employees_salary_above(45000)

-- 3. Town Names Starting With
delimiter //
create procedure usp_get_towns_starting_with(word varchar(50))
begin
	select name as town_name from towns
    where name like concat(word, '%')
    order by name asc;
end//

call usp_get_towns_starting_with("b")

-- 4. Employees from Town
delimiter //
create procedure usp_get_employees_from_town(town_name varchar(50))
begin
	select e.first_name, e.last_name from employees e
    join addresses a on e.address_id = a.address_id
    join towns t on t.town_id = a.town_id
    where t.name = town_name
    order by e.first_name asc, e.last_name asc, e.employee_id asc;
end//

call usp_get_employees_from_town('Sofia');

-- 5. Salary Level Function
delimiter //
create function ufn_get_salary_level(given_salary decimal(19, 4))
returns varchar(10)
deterministic
begin
    return case
		when given_salary < 30000 then 'Low'
        when given_salary between 30000 and 50000 then 'Average'
        else 'High'
	end;
end//

-- 6. Employees by Salary Level
delimiter //
create procedure usp_get_employees_by_salary_level(salary_level varchar(7))
begin
	select e.first_name, e.last_name from employees e
    where 
		(salary_level = 'Low' and e.salary < 30000)
        or
        (salary_level = 'Average' and e.salary between 30000 and 50000)
        or
        (salary_level = 'High' and e.salary > 50000)
    order by e.first_name desc, e.last_name desc;
end//

call usp_get_employees_by_salary_level('High');

-- 7. Define Function
delimiter //
create function ufn_is_word_comprised(set_of_letters varchar(50), word varchar(50))
returns int
deterministic
begin
	declare i int default 1;
    declare word_length int;
    declare current_char char(1);
    
    set word_length = length(word);
    
    while i <= word_length do
		set current_char = substring(word, i, 1);
        if locate(current_char, set_of_letters) = 0 then
			return 0;
		end if;
        
        set i = i + 1;
	end while;
    
    return 1;
end//

-- PART II – Queries for Bank Database
delimiter //
create procedure usp_get_holders_full_name()
begin
	select concat_ws(" ", a.first_name, a.last_name) as full_name from account_holders a
    order by full_name;
end//

call usp_get_holders_full_name();

-- 9. People with Balance Higher Than
delimiter //
create procedure usp_get_holders_with_balance_higher_than(number decimal(19, 4))
begin
	select ah.first_name, ah.last_name from account_holders ah
    join accounts a on ah.id = a.account_holder_id
    group by ah.id, ah.first_name, ah.last_name
    having sum(a.balance) > number
    order by ah.id asc;
end//

call usp_get_holders_with_balance_higher_than(7000);

-- 10. Future Value Function
delimiter //
create function ufn_calculate_future_value(sum decimal(19, 4), yearly_interest double, number_of_years int)
returns decimal(19, 4)
deterministic
begin
	declare fv decimal(19, 4);
    
    set fv = sum * (pow(1 + yearly_interest, number_of_years));
    return fv;
end//

select ufn_calculate_future_value(1000, 0.5, 5);

-- 11. Calculating Interest
delimiter //
create procedure usp_calculate_future_value_for_account(in id int, interest_rate decimal(19, 4))
begin
	select a.id, ah.first_name, ah.last_name, a.balance, 
		cast(round(a.balance * pow(1 + interest_rate, 5), 5) as decimal(19, 4)) AS balance_in_five_years
	from accounts a
    join account_holders ah on a.account_holder_id = ah.id
    where a.id = id;
end//

call usp_calculate_future_value_for_account(1, 0.1);

-- 12. Deposit Money
delimiter //
create procedure usp_deposit_money(in account_id int, money_amount decimal(19, 4))
begin
	start transaction;
    update accounts 
		set balance = balance + money_amount
        where account_id = id;
        
        if (money_amount <= 0) then rollback;
        else commit;
        end if;
end//

call usp_deposit_money(1, 10);
select * from accounts where id = 1;

-- 13. Withdraw Money
delimiter //
create procedure usp_withdraw_money(account_id int, money_amount decimal(19, 4))
begin
	declare current_balance decimal(19, 4);
    
    select balance into current_balance
    from accounts
    where id = account_id;
    
	start transaction;
    update accounts 
		set balance = balance - money_amount
        where account_id = id;
        
        if (money_amount <= 0 or current_balance < money_amount) then rollback;
        else commit;
        end if;
end//

call usp_withdraw_money(1, 100);
select * from accounts where id = 1;

-- 14. Money Transfer
delimiter //
create procedure usp_transfer_money(in from_account_id int, to_account_id int, amount decimal(19, 4))
begin
	declare from_temp_id int;
    declare to_temp_id int;
	declare current_balance decimal(19, 4);
    
    select id into from_temp_id from accounts
    where id = from_account_id;
    
    select id into to_temp_id from accounts
    where id = to_account_id;
    
    select balance into current_balance
    from accounts
    where id = from_account_id;
    
    start transaction;
    update accounts
		set balance = balance + amount 
        where id = to_account_id;
	
    update accounts
        set balance = balance - amount 
        where id = from_account_id;
        
		if (from_account_id = to_account_id or from_temp_id is null or to_temp_id is null or
		current_balance < amount or amount < 0) then rollback;
        else commit;
        end if;
end//

-- 15. Log Accounts Trigger
create table logs (
	log_id int primary key auto_increment,
    account_id int not null,
    old_sum decimal(19, 4) not null,
    new_sum decimal(19, 4) not null
);

delimiter //
create trigger tr_updated_balance_accounts
after update
on accounts
for each row
begin
	insert into logs(account_id, old_sum, new_sum) values(old.id, old.balance, new.balance);
end//

-- 16. Emails Trigger
create table notification_emails(
	id int primary key auto_increment,
    recipient int not null,
    subject varchar(50) not null,
    body text not null
);

delimiter //
create trigger tr_create_email_when_log_created
after insert
on `logs`
for each row
begin
	insert into notification_emails(recipient, subject, body) values(new.account_id, 
		concat_ws(" ", "Balance change for account:", new.account_id),
        concat_ws(" ", "On", now(), "your balance was changed from", new.old_sum, "to", new.new_sum));
end//