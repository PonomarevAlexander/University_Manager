drop table students, groups, teachers,
lessons, departments;

create table departments(
	id int primary key,
	name varchar(255) not null);

create table teachers(
	id int primary key,
	name varchar(255) not null,
	last_name varchar(255) not null,
	department_id integer references departments(id));
	
create table groups(
	id int primary key,
	name varchar(255) not null,
	head integer references teachers(id),
	department_id integer references departments(id));
	
create table students(
	id int primary key,
	name varchar(255) not null,
	last_name varchar(255) not null,
	age integer,
	group_id integer references groups(id));
	
create table lessons(
	id int primary key,
	name varchar(255) not null,
	start_time varchar(255) not null,
	duration integer not null,
	teacher_id integer references teachers(id),
	group_id integer references groups(id));