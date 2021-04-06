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
	department_id integer references departments(id) on delete set null on update cascade);
	
create table groups(
	id serial primary key,
	name varchar(255) not null,
	department_id integer references departments(id) on delete set null on update cascade);
	
create table students(
	id serial primary key,
	name varchar(255) not null,
	last_name varchar(255),
	age integer);
	
create table lessons(
	id serial primary key,
	name varchar(255) not null,
	start_time varchar(255) not null,
	duration integer not null,
	teacher_id integer references teachers(id) on delete set null on update cascade,
	group_id integer references groups(id) on delete set null on update cascade);
	
create table timetables(
	id serial primary key,
	creation_date varchar(20) not null);
	
	
create table students_groups(
	student_id integer references students(id) on delete cascade on update cascade,
	group_id integer references groups(id) on delete cascade on update cascade);
	
create table timetables_lessons(
	timetable_id integer references timetables(id) on delete cascade on update cascade,
	lesson_id integer references lessons(id) on delete cascade on update cascade);
	
create table teachers_lessons(
	teacher_id integer references teachers(id) on delete cascade on update cascade,
	lesson_id integer references lessons(id) on delete cascade on update cascade);
	
create table timetables_groups(
	timetable_id integer references timetables(id) on delete cascade on update cascade,
	group_id integer references groups(id) on delete cascade on update cascade);
	
create table timetables_teachers(
	timetable_id integer references timetables(id) on delete cascade on update cascade,
	teacher_id integer references teachers(id) on delete cascade on update cascade);
	
create table groups_teachers(
	group_id integer references groups(id) on delete cascade on update cascade,
	teacher_id integer references teachers(id) on delete cascade on update cascade);