insert into companies (id, name) values ('e15a8513-e02d-4a53-82d8-048adc6ff11d','myCompany'),
                                        ('5453f7fd-9c2a-4604-8e2b-5464f1513550','notMyCompany'),
                                        ('41d8cf82-1720-4cb4-9ff6-fac60f99fcc4', 'myCompany2');
insert into users (id, username, password, enabled, first_name, last_name, email, company_id)
values ('4bb6cb57-c22e-4d11-bba6-1a798966213e', 'admin@myCompany.net', 'dummyPass',
        true, 'Admin', 'Admin', 'admin@myCompany.net', 'e15a8513-e02d-4a53-82d8-048adc6ff11d'),
       ('f9885241-5517-48fd-9de0-f2971548dc99', 'user@myCompany.net', 'dummyPass',
        true, 'User', 'User', 'user@myCompany.net', 'e15a8513-e02d-4a53-82d8-048adc6ff11d'),
       ('c960cd84-c064-4d70-98b1-9a33d6368244', 'user@notMyCompany.net', 'dummyPass',
        true, 'User2', 'User2', 'user@notMyCompany.net', '5453f7fd-9c2a-4604-8e2b-5464f1513550');