-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('josgarber6','276591',TRUE);
INSERT INTO users(username,password,enabled) VALUES ('frabenrui1','z3bas',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'owner1','owner');
INSERT INTO authorities(id,username,authority) VALUES (11,'josgarber6','player');
INSERT INTO authorities(id,username,authority) VALUES (12,'frabenrui1','player');

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

INSERT INTO achievement_types(id,name) VALUES (1,'Explorador');

-- Achievements

INSERT INTO achievements(id,name,threshold,description,badge_image,achievement_type_id) VALUES 
(1, 'Viciado', 10.0, 'Si juegas <THRESHOLD> partidas o más, consideramos que ya estás enganchado.', 'https://bit.ly/certifiedGamer',1),
(2, 'Triunfador', 20.0, 'Si ganas <THRESHOLD> o más partidas es que eres todo un triunfador.', 'https://bit.ly/proGamer',1);

-- Player

INSERT INTO players(id,first_name,last_name,username,description,status) VALUES (1,'Francisco Sebastian','Benitez Ruis Diaz','frabenrui1','Me encanta este juego',TRUE);
INSERT INTO players(id,first_name,last_name,username,description,status) VALUES (2,'Jose Maria','Garcia Berdejo','josgarber6','Lets gooo',TRUE);

-- PlayerAchievements

INSERT INTO player_achievements(player_id,achievement_id) VALUES (1,1);



INSERT INTO generic_cards(id,colour,type) VALUES (1, 'RED', 'ORGAN');
INSERT INTO games(id,initial_hour,is_running,round,turn) VALUES(1,'2022-11-10 17:00:00',TRUE,0,0);
INSERT INTO game_players(id, host, turn, winner, game_id, game_player_id ) VALUES (1,FALSE,1,FALSE,1,1);
INSERT INTO cards(id, body, played, card_vaccine_id, card_virus_id, game_player_id, type_id ) VALUES (1,TRUE,FALSE,1,1,1,1);





