drop table students, groups, teachers,
lessons, timetables, timetables_lessons,
teachers_lessons, timetables_groups,
timetables_teachers, groups_teachers, students_groups, departments if exists;

create table departments(
	id serial primary key,
	name varchar(255) not null);

create table teachers(
	id serial primary key,
	name varchar(255) not null,
	last_name varchar(255) not null,
	department_id integer references departments(id));
	
create table groups(
	id serial primary key,
	name varchar(255) not null,
	head integer references teachers(id),
	department_id integer references departments(id));
	
create table students(
	id serial primary key,
	name varchar(255) not null,
	last_name varchar(255),
	age integer,
	group_id integer references groups(id));
	
create table lessons(
	id serial primary key,
	name varchar(255) not null,
	start_time varchar(255) not null,
	duration integer not null,
	teacher_id integer references teachers(id),
	group_id integer references groups(id));