-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('josgarber6','276591',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('frabenrui1','z3bas',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('lucperrom','laquesea',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('juaanjimdel','laquesea2',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('p','p',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('p1','p1',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('p2','p2',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('p3','p3',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('p4','p4',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('p5','p5',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('p6','p6',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('p7','p7',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'owner1','owner');
INSERT INTO authorities(id,username,authority) VALUES (11,'josgarber6','player');
INSERT INTO authorities(id,username,authority) VALUES (12,'frabenrui1','player');
INSERT INTO authorities(id,username,authority) VALUES (13,'p','player');
INSERT INTO authorities(id,username,authority) VALUES (14,'lucperrom','player');

-- Achievement Type

INSERT INTO achievement_types(id,name) VALUES (1,'Explorador'), (2,'Aprendiz'), (3,'Experimentado'), (4,'Gran Jugador'), (5,'Maestro');

-- Achievements

INSERT INTO achievements(id,name,threshold,description,badge_image,type) VALUES 
(1, 'Viciado', 1, 'Si juegas <THRESHOLD> partidas o más, consideramos que ya estás enganchado.', 'https://bit.ly/certifiedGamer',1),
(2, 'Triunfador', 2, 'Si ganas <THRESHOLD> o más partidas es que eres todo un triunfador.', 'https://bit.ly/proGamer',1);

-- Player

INSERT INTO players(id,first_name,last_name,username,description,status,profile_image) VALUES (1,'Francisco Sebastian','Benitez Ruis Diaz','frabenrui1','Me encanta este juego',TRUE,'/resources/images/user.png');
INSERT INTO players(id,first_name,last_name,username,description,status,profile_image) VALUES (2,'Jose Maria','Garcia Berdejo','josgarber6','Lets gooo',TRUE,'');
INSERT INTO players(id,first_name,last_name,username,description,status,profile_image) VALUES (3,'Lucia','Perez Romero','lucperrom','A ganar',TRUE,'');
INSERT INTO players(id,first_name,last_name,username,description,status,profile_image) VALUES (4,'Juan Antonio','Jiménez del Villar','juaanjimdel','Nadie puede ganarme',TRUE,'');
INSERT INTO players(id,first_name,last_name,username,description,status,profile_image) VALUES (5,'p','p','p','pp',TRUE,'');
INSERT INTO players(id,first_name,last_name,username,description,status,profile_image) VALUES (6,'p1','p1','p1','pp1',TRUE,'');

-- GamePlayer

INSERT INTO game_players(id, player_id) VALUES (1,1);
INSERT INTO game_players(id, player_id) VALUES (2,2);
INSERT INTO game_players(id, player_id) VALUES (3,3);
INSERT INTO game_players(id, player_id) VALUES (4,4);
INSERT INTO game_players(id, player_id) VALUES (5,5);

-- Room
INSERT INTO room(id,num_max_players,room_name,is_private,player_id) VALUES (1,6,'prueba',FALSE,5);

--Modificacion de player con la room creada
UPDATE players Set room_id=1 WHERE id=5;

--Modificacion de player con la room creada
UPDATE players Set room_id=1 WHERE id=5;

--Modificacion de player con la room creada
UPDATE players Set room_id=1 WHERE id=5;

-- Games

INSERT INTO games(id, duration, initial_hour, is_running, round, room_id, winner_id) VALUES (1,6000000000,'2022-02-01 15:15:15',FALSE,4,1,1);
INSERT INTO games(id, duration, initial_hour, is_running, round, room_id, winner_id) VALUES (2,6000000000,'2022-02-01 16:15:15',FALSE,3,1,2);
INSERT INTO games(id, duration, initial_hour, is_running, round, room_id, winner_id) VALUES (3,6000000000,'2022-02-01 17:15:15',FALSE,5,1,1);
INSERT INTO games(id, duration, initial_hour, is_running, round, room_id, winner_id) VALUES (4,6000000000,'2022-02-01 18:15:15',FALSE,4,1,2);
INSERT INTO games(id, duration, initial_hour, is_running, round, room_id, winner_id) VALUES (5,6000000000,'2022-02-01 19:15:15',FALSE,4,1,1);
INSERT INTO games(id, duration, initial_hour, is_running, round, room_id, winner_id) VALUES (6,6000000000,'2022-02-01 20:15:15',FALSE,4,1,3);
INSERT INTO games(id, duration, initial_hour, is_running, round, room_id, winner_id) VALUES (7,6000000000,'2022-02-02 15:15:15',FALSE,4,1,1);
INSERT INTO games(id, duration, initial_hour, is_running, round, room_id, winner_id) VALUES (8,6000000000,'2022-02-02 16:15:15',FALSE,3,1,2);
INSERT INTO games(id, duration, initial_hour, is_running, round, room_id, winner_id) VALUES (9,6000000000,'2022-02-02 17:15:15',FALSE,5,1,3);
INSERT INTO games(id, duration, initial_hour, is_running, round, room_id, winner_id) VALUES (10,6000000000,'2022-02-02 18:15:15',FALSE,4,1,4);
INSERT INTO games(id, duration, initial_hour, is_running, round, room_id, winner_id) VALUES (11,6000000000,'2022-02-02 19:15:15',FALSE,4,1,5);
INSERT INTO games(id, duration, initial_hour, is_running, round, room_id, winner_id) VALUES (12,6000000000,'2022-02-02 20:15:15',FALSE,4,1,4);


-- GamePlayerGames

INSERT INTO games_game_player VALUES (1,1), (1,2);
INSERT INTO games_game_player VALUES (2,1), (2,2);
INSERT INTO games_game_player VALUES (3,1), (3,2), (3,3), (3,4);
INSERT INTO games_game_player VALUES (4,1), (4,2), (4,3), (4,4);
INSERT INTO games_game_player VALUES (5,1), (5,2), (5,3), (5,4);
INSERT INTO games_game_player VALUES (6,1), (6,2), (6,3);
INSERT INTO games_game_player VALUES (7,1), (7,2);
INSERT INTO games_game_player VALUES (8,1), (8,2);
INSERT INTO games_game_player VALUES (9,1), (9,2), (9,3), (9,4);
INSERT INTO games_game_player VALUES (10,1), (10,2), (10,3), (10,4);
INSERT INTO games_game_player VALUES (11,1), (11,2), (11,3), (11,4);
INSERT INTO games_game_player VALUES (12,1), (12,2), (12,3);

-- PlayerAchievements

-- Statitics

--Friends

INSERT INTO FRIEND(ID,STATUS,PLAYER_REC_ID,PLAYER_SEND_ID) VALUES(1,TRUE,1,5);
INSERT INTO FRIEND(ID,STATUS,PLAYER_REC_ID,PLAYER_SEND_ID) VALUES(2,FALSE,2,1);
INSERT INTO FRIEND(ID,STATUS,PLAYER_REC_ID,PLAYER_SEND_ID) VALUES(3,TRUE,3,1);
INSERT INTO FRIEND(ID,STATUS,PLAYER_REC_ID,PLAYER_SEND_ID) VALUES(4,null,1,4);


--Invitations