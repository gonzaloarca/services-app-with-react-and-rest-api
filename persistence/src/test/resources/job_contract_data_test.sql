-- ELiminamos informacion en caso de que exista
TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK;

INSERT INTO users(user_id, user_name, user_email,user_password, user_phone, user_is_active, user_is_verified, user_creation_date) VALUES (NEXT VALUE FOR users_user_id_seq, 'Francisco Quesada', 'franquesada@gmail.com', 'password','1147895678', true, false, NOW());
INSERT INTO users(user_id, user_name, user_email,user_password, user_phone, user_is_active, user_is_verified, user_creation_date) VALUES (NEXT VALUE FOR users_user_id_seq, 'Manuel Rodriguez', 'manurodriguez@gmail.com','password', '1109675432', true, true, NOW());
INSERT INTO users(user_id, user_name, user_email,user_password, user_phone, user_is_active, user_is_verified, user_creation_date) VALUES (NEXT VALUE FOR users_user_id_seq, 'Gonzalo Arca', 'gonzaarca@gmail.com','password', '0549940406521', true, true, NOW());
INSERT INTO users(user_id, user_name, user_email,user_password, user_phone, user_is_active, user_is_verified, user_creation_date) VALUES (NEXT VALUE FOR users_user_id_seq, 'Manuel Parma', 'manuparma@gmail.com','password', '1158586363', true, true, NOW());
INSERT INTO users(user_id, user_name, user_email,user_password, user_phone, user_is_active, user_is_verified, user_creation_date) VALUES (NEXT VALUE FOR users_user_id_seq, 'Julian Sicardi', 'juliansicardi@gmail.com','password', '123123123', true, true, NOW());

INSERT INTO job_post(post_id, user_id, post_title, post_available_hours, post_job_type, post_is_active, post_creation_date) VALUES (NEXT VALUE FOR job_post_post_id_seq, 1, 'Electricista Matriculado', 'Lun a Viernes 10hs - 14hs', 1, true, NOW());
INSERT INTO job_post(post_id,user_id,post_title,post_available_hours,post_job_type,post_is_active, post_creation_date) VALUES (NEXT VALUE FOR job_post_post_id_seq,1,'Paseador de perros','Viernes a sabados 09hs - 14hs',3,true, NOW());
INSERT INTO job_post(post_id, user_id, post_title, post_available_hours, post_job_type, post_is_active, post_creation_date) VALUES (NEXT VALUE FOR job_post_post_id_seq, 1, 'Plomero Matriculado 2', 'Miercoles a Viernes 10hs - 14hs', 2, true, NOW());
INSERT INTO job_post(post_id, user_id, post_title, post_available_hours, post_job_type, post_is_active, post_creation_date) VALUES (NEXT VALUE FOR job_post_post_id_seq, 1, 'Electricista Matriculado 2', 'Lun a Viernes 10hs - 14hs', 1, true, NOW());
INSERT INTO job_post(post_id,user_id,post_title,post_available_hours,post_job_type,post_is_active, post_creation_date) VALUES (NEXT VALUE FOR job_post_post_id_seq,1,'Paseador de perros 2','Viernes a sabados 09hs - 14hs',3,true, NOW());
INSERT INTO job_post(post_id,user_id,post_title,post_available_hours,post_job_type,post_is_active, post_creation_date) VALUES (NEXT VALUE FOR job_post_post_id_seq,1,'Electricista no matriculado 2','Lun a Jueves 13hs - 14hs',1,true, NOW());
INSERT INTO job_post(post_id, user_id, post_title, post_available_hours, post_job_type, post_is_active, post_creation_date) VALUES (NEXT VALUE FOR job_post_post_id_seq, 1, 'Plomero Matriculado 2', 'Miercoles a Viernes 10hs - 14hs', 2, true, NOW());
INSERT INTO job_post(post_id, user_id, post_title, post_available_hours, post_job_type, post_is_active, post_creation_date) VALUES (NEXT VALUE FOR job_post_post_id_seq, 1, 'Plomero Matriculado 3', 'Miercoles a Viernes 10hs - 14hs', 2, true, NOW());
INSERT INTO job_post(post_id, user_id, post_title, post_available_hours, post_job_type, post_is_active, post_creation_date) VALUES (NEXT VALUE FOR job_post_post_id_seq, 1, 'Plomero Matriculado 4  ', 'Miercoles a Viernes 10hs - 14hs', 2, true, NOW());
INSERT INTO job_post(post_id, user_id, post_title, post_available_hours, post_job_type, post_is_active, post_creation_date) VALUES (NEXT VALUE FOR job_post_post_id_seq, 1, 'Plomero Inactivo', 'Miercoles a Viernes 10hs - 14hs', 2, false, NOW());
INSERT INTO job_post(post_id,user_id,post_title,post_available_hours,post_job_type,post_is_active, post_creation_date) VALUES (NEXT VALUE FOR job_post_post_id_seq,2,'Electricista no matriculado','Lun a Jueves 13hs - 14hs',1,true, NOW());

INSERT INTO post_zone(post_id, zone_id) VALUES (1, 1);
INSERT INTO post_zone(post_id, zone_id) VALUES (1, 2);
INSERT INTO post_zone(post_id, zone_id) VALUES (2, 1);
INSERT INTO post_zone(post_id, zone_id) VALUES (2, 2);
INSERT INTO post_zone(post_id, zone_id) VALUES (3,1);
INSERT INTO post_zone(post_id, zone_id) VALUES (3,2);
INSERT INTO post_zone(post_id, zone_id) VALUES (4,1);
INSERT INTO post_zone(post_id, zone_id) VALUES (4,2);
INSERT INTO post_zone(post_id, zone_id) VALUES (5, 1);
INSERT INTO post_zone(post_id, zone_id) VALUES (5, 2);
INSERT INTO post_zone(post_id, zone_id) VALUES (6, 1);
INSERT INTO post_zone(post_id, zone_id) VALUES (6,2);
INSERT INTO post_zone(post_id, zone_id) VALUES (7,1);
INSERT INTO post_zone(post_id, zone_id) VALUES (7,2);
INSERT INTO post_zone(post_id, zone_id) VALUES (8,1);
INSERT INTO post_zone(post_id, zone_id) VALUES (8,2);
INSERT INTO post_zone(post_id, zone_id) VALUES (9,1);
INSERT INTO post_zone(post_id, zone_id) VALUES (9,2);
INSERT INTO post_zone(post_id, zone_id) VALUES (10,1);
INSERT INTO post_zone(post_id, zone_id) VALUES (10,2);
INSERT INTO post_zone(post_id, zone_id) VALUES (11,1);
INSERT INTO post_zone(post_id, zone_id) VALUES (11,2);

INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 1, 'Trabajo Simple', 'Arreglo basico de electrodomesticos', 200.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 1, 'Trabajo no tan simple', 'Instalacion de cableado electrico', 850.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 1, 'Trabajo Simple', 'Arreglos de tomacorrientes', 200.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 1, 'Trabajo no tan simple 2', 'Instalacion de cableado electrico', 850.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 1, 'Trabajo Complejo 2', 'Arreglos de canerias', 500.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 1, 'Trabajo barato 2', 'Arreglos varios', 500.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 1, 'Trabajo barato 2', 'Arreglos varios', 500.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 1, 'Trabajo Experto 2', 'Presupuesto y desarrollo de proyectos', 500.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 1, 'Trabajo Experto 2', 'Presupuesto y desarrollo de proyectos', 500.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 2, 'Trabajo Complejo', 'Arreglos de canerias', 500.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 3, 'Trabajo barato', 'Arreglos varios', 500.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 4, 'Trabajo Experto', 'Presupuesto y desarrollo de proyectos', 500.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 5, 'Trabajo Simple', 'Arreglos de tomacorrientes', 200.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 6, 'Trabajo Simple', 'Arreglos de tomacorrientes', 200.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 7, 'Trabajo Simple', 'Arreglos de tomacorrientes', 200.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 8, 'Trabajo Simple', 'Arreglos de tomacorrientes', 200.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 9, 'Trabajo Simple', 'Arreglos de tomacorrientes', 200.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 10, 'Trabajo Simple', 'Arreglos de tomacorrientes', 200.00, 0, true);
INSERT INTO job_package(package_id, post_id, package_title, package_description, package_price, package_rate_type, package_is_active) VALUES (NEXT VALUE FOR job_package_package_id_seq, 11, 'Trabajo Simple', 'Arreglos de tomacorrientes', 200.00, 0, true);

INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,image_data,contract_image_type) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 1, '2021-05-04 01:09:46.0', 'Se me rompio una zapatilla',0,hextoraw('010203040506'),'png');
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 1, '2021-05-04 01:09:46.0', 'Arreglo de fusibles facil',0, NOW() - INTERVAL 1 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 1, '2021-05-04 01:09:46.0', 'Arreglo de fusibles',0,NOW() - INTERVAL 2 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 1, '2021-05-04 01:09:46.0', 'Se me rompio una zapatilla',0,NOW() - INTERVAL 3 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 1, '2021-05-04 01:09:46.0', 'Arreglo de fusibles facil',0,NOW() - INTERVAL 4 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 1, '2021-05-04 01:09:46.0', 'Arreglo de fusibles',0,NOW() - INTERVAL 5 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 1, '2021-05-04 01:09:46.0', 'Instalacion de tomacorrientes',0,NOW() - INTERVAL 6 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 1, '2021-05-04 01:09:46.0', 'Se me rompio una tuberia en la cocina',0,NOW() - INTERVAL 7 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 1, '2021-05-04 01:09:46.0', 'Se me rompio la caldera denuevo',0,NOW() - INTERVAL 8 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 1, '2021-05-04 01:09:46.0', 'Se me rompieron las tuberias del baño',0,NOW() - INTERVAL 9 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 1, '2021-05-04 01:09:46.0', 'Se me rompio la caldera',0,NOW() - INTERVAL 10 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 1, '2021-05-04 01:09:46.0', 'Se me rompio la caldera denuevo',0,NOW() - INTERVAL 11 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 10, '2021-05-04 01:09:46.0', 'Instalacion de tomacorrientes',0,NOW() - INTERVAL 13 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 10, '2021-05-04 01:09:46.0', 'Se me rompio una tuberia en la cocina',0,NOW() - INTERVAL 14 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 10, '2021-05-04 01:09:46.0', 'Se me rompieron las tuberias del banio',0,NOW() - INTERVAL 15 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 10, '2021-05-04 01:09:46.0', 'Se me rompio la caldera',0,NOW() - INTERVAL 16 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 10, '2021-05-04 01:09:46.0', 'Se me rompio la caldera de nuevo',0,NOW() - INTERVAL 17 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 10, '2021-05-04 01:09:46.0', 'Se me rompio la caldera de nuevo',0,NOW() - INTERVAL 18 DAY);
INSERT INTO contract(contract_id, client_id, package_id, contract_creation_date, contract_description, contract_state,contract_last_modified_date) VALUES (NEXT VALUE FOR contract_contract_id_seq, 2, 1, '2021-05-04 01:09:46.0', 'Se me rompio la caldera de nuevo',1,NOW() - INTERVAL 12 DAY);

COMMIT;