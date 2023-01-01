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
INSERT INTO authorities(id,username,authority) VALUES (2,'owner1','owner');
INSERT INTO authorities(id,username,authority) VALUES (11,'josgarber6','player');
INSERT INTO authorities(id,username,authority) VALUES (12,'frabenrui1','player');
INSERT INTO authorities(id,username,authority) VALUES (13,'p','player');

-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'vet1','veterinarian');

INSERT INTO vets(id, first_name,last_name) VALUES (1, 'James', 'Carter');
INSERT INTO vets(id, first_name,last_name) VALUES (2, 'Helen', 'Leary');
INSERT INTO vets(id, first_name,last_name) VALUES (3, 'Linda', 'Douglas');
INSERT INTO vets(id, first_name,last_name) VALUES (4, 'Rafael', 'Ortega');
INSERT INTO vets(id, first_name,last_name) VALUES (5, 'Henry', 'Stevens');
INSERT INTO vets(id, first_name,last_name) VALUES (6, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

-- Achievement Type

INSERT INTO achievement_types(id,name) VALUES (1,'Explorador'), (2,'Aprendiz'), (3,'Experimentado'), (4,'Gran Jugador'), (5,'Maestro');

-- Achievements

INSERT INTO achievements(id,name,threshold,description,badge_image,type) VALUES 
(1, 'Viciado', 10.0, 'Si juegas <THRESHOLD> partidas o más, consideramos que ya estás enganchado.', 'https://bit.ly/certifiedGamer',1),
(2, 'Triunfador', 20.0, 'Si ganas <THRESHOLD> o más partidas es que eres todo un triunfador.', 'https://bit.ly/proGamer',1);

-- Player

INSERT INTO players(id,first_name,last_name,username,description,status) VALUES (1,'Francisco Sebastian','Benitez Ruis Diaz','frabenrui1','Me encanta este juego',TRUE);
INSERT INTO players(id,first_name,last_name,username,description,status) VALUES (2,'Jose Maria','Garcia Berdejo','josgarber6','Lets gooo',TRUE);
INSERT INTO players(id,first_name,last_name,username,description,status) VALUES (3,'Lucia','Perez Romero','lucperrom','A ganar',TRUE);
INSERT INTO players(id,first_name,last_name,username,description,status) VALUES (4,'Juan Antonio','Jiménez del Villar','juaanjimdel','Nadie puede ganarme',TRUE);
INSERT INTO players(id,first_name,last_name,username,description,status) VALUES (5,'p','p','p','pp',TRUE);

-- PlayerAchievements

INSERT INTO player_achievements(player_id,achievement_id) VALUES (1,1);

-- Statitics
INSERT INTO players_statistics(id, player_id, num_played_games, num_won_games, points) VALUES (1, 1, 10, 5, 30);



-- Rooms

--Friends

INSERT INTO FRIEND(ID,STATUS,PLAYER_REC_ID,PLAYER_SEND_ID) VALUES(1,TRUE,1,5);
INSERT INTO FRIEND(ID,STATUS,PLAYER_REC_ID,PLAYER_SEND_ID) VALUES(2,FALSE,2,1);
INSERT INTO FRIEND(ID,STATUS,PLAYER_REC_ID,PLAYER_SEND_ID) VALUES(3,TRUE,3,1);
INSERT INTO FRIEND(ID,STATUS,PLAYER_REC_ID,PLAYER_SEND_ID) VALUES(4,null,1,4);