insert into usr (usr_id, email, password, firstname, lastname, token, date_created, 
				manager_id, is_active, is_on_landing_page, description)
values
 (1, 'johnsmith@gmail.com', 'smithjohn', 'JOHN', 'SMITH', 'gcMg0GR5zf60',
 '2012-12-16 00:12:00', null, false, false, 'Johnyy Smith'),
 (2, 'voodyallen@gmail.com', 'allenvoody', 'VOODY', 'ALLEN', 'WCl5WhuVC9ej',
 '2014-04-30 08:42:00', null, false, false, 'Vooden Allen'),
 (3, 'jamieward@gmail.com', 'wardjamie', 'JAMIE', 'WARD', '05iQZp4y6CeV',
 '2014-07-30 10:44:00', null, false, false, 'Jamie Wardie'),
 (4, 'jackjones@gmail.com', 'jonesjack', 'JACK', 'JONES', '8N00odJBX3bY',
 '2014-12-26 11:09:00', null, false, false, 'Jacky Jonesy'),
 (5, 'luthermartin@gmail.com', 'martinluther', 'LUTHER', 'MARTIN', 'i158YBo5aRpQ',
 '2015-06-10 11:51:00', null, false, false, 'Martiny Lutherini'),
 (6, 'jakeblake@gmail.com', 'blakejake', 'JAKE', 'BLAKE', 'hHTD00GfhopU',
 '2015-06-27 13:24:00', null, false, false, 'Jaken Blaken'),
 (7, 'darkclark@gmail.com', 'clarkdark', 'DARK', 'CLARK', 'sN0C2mQ7LJyU',
 '2016-10-30 17:22:00', null, false, false, 'Darken Clarken'),
 (8, 'wasleyscott@gmail.com', 'scottwasley', 'WASLEY', 'SCOTT', 'q5rcNUMepEPj',
 '2017-10-20 20:04:00', null, false, false, 'Scottish Wasley'),
 (9, 'theking@gmail.com', 'kingthe', 'THE', 'KING', 'jt4RFkGzmaSq',
 '2018-04-11 21:06:00', null, false, false, 'Jamie Wardie');


insert into role (role_id, title)
values
	(1, 'admin'),
	(2, 'manager'),
	(3, 'trainer'),
	(4, 'employee');

