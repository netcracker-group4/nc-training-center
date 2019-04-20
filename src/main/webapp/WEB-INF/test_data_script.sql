insert into usr (user_id, email, password, firstname, lastname, token, date_created, 
				manager_id, is_active, is_on_landing_page, description)
values
	(1, 'theking@gmail.com', 'kingthe', 'THE', 'KING', 'jt4RFkGzmaSq',
	 '2018-04-11 21:06:00', null, true, false, 'ADMIN'),
	(2, 'voodyallen@gmail.com', 'allenvoody', 'VOODY', 'ALLEN', 'WCl5WhuVC9ej',
	 '2014-04-30 08:42:00', null, true, false, 'Vooden Allen'),	
	(3, 'jamieward@gmail.com', 'wardjamie', 'JAMIE', 'WARD', '05iQZp4y6CeV',
	 '2014-07-30 10:44:00', null, true, false, 'Jamie Wardie'),
	(4, 'jackjones@gmail.com', 'jonesjack', 'JACK', 'JONES', '8N00odJBX3bY',
	 '2014-12-26 11:09:00', null, true, true, 'Jacky Jonesy'),
	(5, 'luthermartin@gmail.com', 'martinluther', 'LUTHER', 'MARTIN', 'i158YBo5aRpQ',
	 '2015-06-10 11:51:00', null, true, false, 'Martiny Lutherini'),
	(6, 'jakeblake@gmail.com', 'blakejake', 'JAKE', 'BLAKE', 'hHTD00GfhopU',
	 '2015-06-27 13:24:00', 2, true, false, 'Jaken Blaken'),
	(7, 'darkclark@gmail.com', 'clarkdark', 'DARK', 'CLARK', 'sN0C2mQ7LJyU',
	 '2016-10-30 17:22:00', 3, true, false, 'Darken Clarken'),
	(8, 'wasleyscott@gmail.com', 'scottwasley', 'WASLEY', 'SCOTT', 'q5rcNUMepEPj',
	 '2017-10-20 20:04:00', 2, true, false, 'Scottish Wasley'),
	(9, 'johnsmith@gmail.com', 'smithjohn', 'JOHN', 'SMITH', 'gcMg0GR5zf60',
 	 '2012-12-16 00:12:00', 3, false, false, 'Johnyy Smith');


insert into role (role_id, title)
values
	(1, 'admin'),
	(2, 'manager'),
	(3, 'trainer'),
	(4, 'employee');


insert into assigned_role (role_id, user_id)
values 
	(1, 1),
	(1, 3)
	(2, 2),
	(2, 4),
	(3, 2),
	(3, 4),
	(4, 3),
	(5, 3),
	(6, 4),
	(7, 4),
	(8, 4),
	(9, 4);


insert into level (level_id, title)
values 
	(1, 'beginner'),
	(2, 'junior'),
	(3, 'middle'),
	(4, 'advanced');


insert into course_status (course_status_id, title, description)
values 
	(1, 'planned', 'The registration has not started yet'),
	(2, 'registration', 'The registrion is open. No schedule yet'),
	(3, 'scheduled', 'Registration is closed. Schedule is formed. The lessons have not started yet'),
	(4, 'ongoing', 'The lessons have started'),
	(5, 'ended', 'The lessons have ended');
	
	
insert into course (course_id, title, level_id, course_status_id, trainer_id, 
					start_date, end_date, is_on_landing_page)
values
	(1, 'Java Beginner Course', 1, 2, 4, '2019-08-29', '2019-12-05', true),
	(2, 'Java Standart Edition', 2, 3, 4, '2019-07-23', '2019-10-25', true),
	(3, 'Java Senior', 4, 4, 5, '2019-02-28', '2019-08-14', false);


insert into problem_status (problem_status_id, title, description)
values
	(1, 'draft', 'The employee created request but the request is not submitted.'),
	(2, 'open', 'The employee submitted the request. The system displays notification regarding new request.'),
	(3, 'in progress', 'The admin starts to answer to employee’s response. The system saves intermediate status of the answer.'),
	(4, 'answered', 'The employee marked request as answered. The admin cannot type anything in this request.'),
	(5, 'reopened', 'The employee reopened the request.');


insert into abscence_reason (abscence_reason_id, title)
values 
	(1, 'No reason'),
	(2, 'Sick'),
	(3, 'Business trip'),
	(4, 'Project activities');


insert into attendance_status (attendance_status_id, title)
values 
	(1, 'present'),
	(2, 'absent'),
	(3, 'late');


insert into grup (group_id, course_id, title)
values
	(1, 3, 'Java Seniors');


insert into user_group (user_id, group_id, attending)
values
	(2, 1, true),
	(6, 1, true),
	(7, 1, true);


insert into lesson (lesson_id, group_id, topic, trainer_id, date_time)
values 
	(1, 1, 'Generics', 4, '2012-05-29 18:00:00'),
	(2, 1, 'Java EE', 5, '2012-05-26 18:00:00');


insert into attendance (lesson_id, student_id, reason_id, status_id)
values
	(1, 2, null, 1),
	(1, 6, null, 1),
	(1, 7, null, 3),
	(2, 2, null, 1),
	(2, 6, 2, 2),
	(2, 7, null, 3);


insert into suitability (suitability_id, title, priority)
values 
	(1, 'perfect', 3),
	(2, 'suitable', 2),
	(3, 'normal', 3);
	






