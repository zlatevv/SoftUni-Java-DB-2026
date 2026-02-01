drop database if exists `Buhtig`;
create database `Buhtig`;
use `Buhtig`;

create table users (
	id int primary key auto_increment,
    username varchar(30) not null unique,
    password varchar(30) not null,
    email varchar(50) not null
);

create table repositories (
	id int primary key auto_increment,
    name varchar(50) not null
);

create table repositories_contributors (
	repository_id int not null,
    contributor_id int not null,
    
    primary key pk_repo_contrib(repository_id, contributor_id),
    constraint foreign key (repository_id)
		references repositories(id),
	constraint foreign key (contributor_id)
		references users(id)
);

create table issues (
	id int primary key auto_increment,
    title varchar(255) not null,
    issue_status varchar(6) not null,
    repository_id int not null,
    assignee_id int not null,
    
    constraint foreign key (repository_id)
		references repositories(id),
	constraint foreign key (assignee_id)
		references users(id)
);

create table commits(
	id int primary key auto_increment,
    message varchar(255) not null,
    issue_id int,
    repository_id int not null,
    contributor_id int not null,
    
    constraint foreign key (issue_id)
		references issues(id),
     constraint foreign key (repository_id)
		references repositories(id),
	constraint foreign key (contributor_id)
		references users(id)
);

create table files (
	id int primary key auto_increment,
    name varchar(100) not null,
    size decimal(10, 2) not null,
    parent_id int,
    commit_id int not null,
    
    constraint foreign key (parent_id) references files(id),
    constraint foreign key (commit_id) references commits(id)
);

-- Section 2: Data Manipulation Language (DML) – 30 pts
-- 02.	Data Insertion
insert into issues (title, issue_status, repository_id, assignee_id)
select concat("Critical Problem With ", f.name, "!"), 'open', ceil((f.id * 2) / 3), c.contributor_id
from files f join commits c on f.commit_id = c.id
where f.id between 46 and 50;

-- 03.	Data Update
UPDATE repositories_contributors rc
JOIN (
    SELECT r.id
    FROM repositories r
    LEFT JOIN repositories_contributors rc2
        ON r.id = rc2.repository_id
    WHERE rc2.repository_id IS NULL
    ORDER BY r.id
    LIMIT 1
) AS target_repo
SET rc.repository_id = target_repo.id
WHERE rc.repository_id = rc.contributor_id;

-- 04. Data Deletion	
delete r from repositories r
left join issues i on r.id = i.repository_id
where i.id is null;

-- Section 4: Programmability – 30 pts
-- 05. Commit
delimiter //

create procedure udp_commit(
    in p_username varchar(30),
    in p_password varchar(30),
    in p_message varchar(255),
    in p_issue_id int
)
begin
    declare v_user_id int;
    declare v_repo_id int;
    declare v_password varchar(30);

    select u.id, u.password
    into v_user_id, v_password
    from users u
    where u.username = p_username;

    if v_user_id is null then
        signal sqlstate '45000'
        set message_text = 'No such user!';
    end if;

    if v_password <> p_password then
        signal sqlstate '45000'
        set message_text = 'Password is incorrect!';
    end if;

    select i.repository_id
    into v_repo_id
    from issues i
    where i.id = p_issue_id;

    if v_repo_id is null then
        signal sqlstate '45000'
        set message_text = 'The issue does not exist!';
    end if;

    insert into commits (message, issue_id, repository_id, contributor_id)
    values (p_message, p_issue_id, v_repo_id, v_user_id);

    update issues
    set issue_status = 'closed'
    where id = p_issue_id;

end//

-- 06. Filter Extensions
delimiter //
create procedure udp_findbyextension(in extension varchar(15))
begin
	select id, 
    name as caption, 
    concat(size, "KB") as user from files
    where name like concat("%.", extension)
    order by id;
end//

call udp_findbyextension('html');