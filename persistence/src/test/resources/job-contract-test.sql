TRUNCATE TABLE job_post RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE users RESTART IDENTITY AND COMMIT NO CHECK;
TRUNCATE TABLE job_package RESTART IDENTITY AND COMMIT NO CHECK;
INSERT INTO users(id,username,email,phone,is_professional,is_active) VALUES (DEFAULT,'Francisco Quesada','franquesada@gmail.com','1147895678',true,true);
INSERT INTO users(id,username,email,phone,is_professional,is_active) VALUES (DEFAULT,'Manuel Rodriguez','manurodriguez@gmail.com','1109675432',false,true);
INSERT INTO job_post(id,user_id,title,available_hours,job_type,is_active) VALUES (DEFAULT,1,'Electricisista Matriculado','Lun a Viernes 10hs - 14hs',0,true);
INSERT INTO job_package(id,post_id,title,description,price,rate_type,is_active) VALUES (DEFAULT,1,'Trabajo Simple','Arreglos de tomacorrientes',200.00,0,true);
INSERT INTO contract(id,post_id,client_id,package_id,creation_date,description) VALUES (DEFAULT,1,2,1,NOW(),'Se me rompio una zapatilla');
COMMIT;