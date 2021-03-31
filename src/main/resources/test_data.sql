insert into groups(name) values('group1');
insert into groups(name) values('group2');
insert into groups(name) values('group3');

insert into students(name, last_name, age) values('studentName1', 'studentLastName1', 11);
insert into students(name, last_name, age) values('studentName2', 'studentLastName2', 22);
insert into students(name, last_name, age) values('studentName3', 'studentLastName3', 33);

insert into teachers(name, last_name) values('testName1', 'testLastName1');
insert into teachers(name, last_name) values('testName2', 'testLastName2');
insert into teachers(name, last_name) values('testName3', 'testLastName3');

insert into lessons(name, start_time, duration) values('testName1', '2021-01-11 11:11:11', 5400);
insert into lessons(name, start_time, duration) values('testName2', '2021-02-12 12:12:12', 5400);
insert into lessons(name, start_time, duration) values('testName3', '2021-03-13 13:13:13', 5400);

insert into timetables(creation_date) values('2021-01-11 11:11:11');
insert into timetables(creation_date) values('2021-02-12 12:12:12');
insert into timetables(creation_date) values('2021-03-13 13:13:13');

insert into teachers_lessons(teacher_id, lesson_id) values(1, 1);
insert into teachers_lessons(teacher_id, lesson_id) values(2, 2);
insert into teachers_lessons(teacher_id, lesson_id) values(1, 3);

insert into timetables_lessons(timetable_id, lesson_id) values(1, 1);
insert into timetables_lessons(timetable_id, lesson_id) values(1, 2);
insert into timetables_lessons(timetable_id, lesson_id) values(2, 3);

insert into timetables_groups(timetable_id, group_id) values(1, 1);
insert into timetables_groups(timetable_id, group_id) values(2, 2);
insert into timetables_groups(timetable_id, group_id) values(3, 3);

insert into departments(name) values('department1');
insert into departments(name) values('department2');
insert into departments(name) values('department3');

insert into groups_teachers(teacher_id, group_id) values(1, 1);
insert into groups_teachers(teacher_id, group_id) values(2, 2);
insert into groups_teachers(teacher_id, group_id) values(3, 3);

insert into students_groups(student_id, group_id) values(1, 1);
insert into students_groups(student_id, group_id) values(2, 1);
insert into students_groups(student_id, group_id) values(3, 2);