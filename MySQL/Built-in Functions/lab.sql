use book_library;

-- 1. Find Book Titles
select title from books
where title like "The%"
order by id;

-- 2. Replace Titles
select replace(title, "The", "***") 
as title from books
where title like "The%"
order by id;

-- 3. Sum Cost of All Books
select round(sum(cost), 2) as `Total Cost` from books;

-- 4. Days Lived
select 
concat_ws(" ", first_name, last_name) as `Full Name`,
timestampdiff(day, born, died) as `Days Lived`
from authors;

-- 5. Harry Potter Books
select title from books 
where title like "%Harry Potter%";
