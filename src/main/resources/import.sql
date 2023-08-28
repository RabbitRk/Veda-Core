-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

INSERT INTO public.roles (active,created_at,updated_at,id,created_by,rolename,roletype,updated_by) VALUES
	 (true,NULL,NULL,'ea328808-930f-4c3a-9347-ca92805a2bcf',NULL,'admin','Admin',NULL),
	 (true,NULL,NULL,'294986aa-aba3-497d-8b33-8fb498ba2eb7',NULL,'guest','Guest',NULL),
	 (true,NULL,NULL,'032caa59-cc11-4eba-b9e8-ae4ad7a47588',NULL,'user','User',NULL);
