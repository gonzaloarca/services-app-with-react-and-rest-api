TRUNCATE TABLE job_post RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE job_package RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE post_zone RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE contract RESTART IDENTITY AND COMMIT NO CHECK;
INSERT INTO users(id,username,email,phone,is_professional,is_active) VALUES (DEFAULT,'Francisco Quesada','franquesada@gmail.com','1147895678',true,true);
INSERT INTO users(id,username,email,phone,is_professional,is_active) VALUES (DEFAULT,'Manuel Rodriguez','manurodriguez@gmail.com','1109675432',false,true);
INSERT INTO users(id,username,email,phone,is_professional,is_active) VALUES (DEFAULT,'Gonzalo Arca','gonzaarca@gmail.com','0549940406521',false,true);
INSERT INTO users(id,username,email,phone,is_professional,is_active) VALUES (DEFAULT,'Manuel Parma','manuparma@gmail.com','1158586363',true,true);

INSERT INTO job_post(id,user_id,title,available_hours,job_type,is_active) VALUES (DEFAULT,1,'Electricista Matriculado','Lun a Viernes 10hs - 14hs',1,true);
INSERT INTO post_zone(post_id, zone_id) VALUES (1,1);
INSERT INTO post_zone(post_id, zone_id) VALUES (1,2);
INSERT INTO job_package(id,post_id,title,description,price,rate_type,is_active) VALUES (DEFAULT,1,'Trabajo Simple','Arreglos de tomacorrientes',200.00,0,true);
INSERT INTO job_package(id,post_id,title,description,price,rate_type,is_active) VALUES (DEFAULT,1,'Trabajo no tan simple','Instalacion de cableado electrico',850.00,0,true);
INSERT INTO contract(id,client_id,package_id,creation_date,description) VALUES (DEFAULT,2,1,NOW(),'Se me rompio una zapatilla');
INSERT INTO contract(id,client_id,package_id,creation_date,description) VALUES (DEFAULT,4,1,NOW(),'Arreglo de fusibles');

INSERT INTO job_post(id,user_id,title,available_hours,job_type,is_active) VALUES (DEFAULT,4,'Plomero Matriculado','Miercoles a Viernes 10hs - 14hs',2,true);
INSERT INTO post_zone(post_id, zone_id) VALUES (2,3);
INSERT INTO job_package(id,post_id,title,description,price,rate_type,is_active) VALUES (DEFAULT,2,'Trabajo Complejo','Arreglos de canerias',500.00,0,true);
INSERT INTO contract(id,client_id,package_id,creation_date,description) VALUES (DEFAULT,3,3,NOW(),'Se me rompieron las tuberias del banio');
INSERT INTO contract(id,client_id,package_id,creation_date,description) VALUES (DEFAULT,2,3,NOW(),'Se me rompio una tuberia en la cocina');

INSERT INTO contract(id,client_id,package_id,creation_date,description) VALUES (DEFAULT,3,2,NOW(),'Instalacion de tomacorrientes');

COMMIT;