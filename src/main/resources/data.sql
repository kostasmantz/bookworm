INSERT INTO author (id, first_name, last_name, email, date_of_birth)
    VALUES (1, 'John', 'Doe', 'john@doe.com', TO_DATE('20/09/1976', 'DD/MM/YYYY'));
INSERT INTO author (id, first_name, last_name, email, date_of_birth)
    VALUES (2, 'David', 'Jacobson', 'david.j@gmail.com', TO_DATE('01/03/1988', 'DD/MM/YYYY'));
INSERT INTO author (id, first_name, last_name, email, date_of_birth)
    VALUES (3, 'Maria', 'Green', 'mariag@yahoo.com', TO_DATE('28/11/1963', 'DD/MM/YYYY'));
INSERT INTO author (id, first_name, last_name, email, date_of_birth)
    VALUES (4, 'Suzan', 'Taylor', 's.taylor@verizon.com', TO_DATE('16/06/1992', 'DD/MM/YYYY'));
INSERT INTO author (id, first_name, last_name, email, date_of_birth)
    VALUES (5, 'Greg', 'Bush', 'gbush@apple.com', TO_DATE('21/05/1994', 'DD/MM/YYYY'));
ALTER SEQUENCE author_id_seq restart with 6;

INSERT INTO publisher (id, name, telephone, address)
    VALUES (1, 'Unicorn House', '510 451-5412', '700 Oak Street, Brockton MA 2301');
INSERT INTO publisher (id, name, telephone, address)
    VALUES (2, 'Layton Orient', '555 154-4656', '780 Lynnway, Lynn MA 1905');
INSERT INTO publisher (id, name, telephone, address)
    VALUES (3, 'Bruce', '455 154-7892', '200 Otis Street, Northborough MA 1532');
INSERT INTO publisher (id, name, telephone, address)
    VALUES (4, 'White House', '651 123-7851', '301 Falls Blvd, Quincy MA 2169');
INSERT INTO publisher (id, name, telephone, address)
    VALUES (5, 'Incorporate', '712 125-4589', '3949 Route 31, Clay NY 13041');
ALTER SEQUENCE publisher_id_seq restart with 6;

INSERT INTO book (id, title, description, status, created_at, isbn, author_id, publisher_id)
    VALUES (1, 'Effective Java', 'Java best practices etc...', 'LIVE', TO_DATE('16/06/2001', 'DD/MM/YYYY'), '9780137150021', 3, null);
INSERT INTO book (id, title, description, status, created_at, isbn, author_id, publisher_id)
    VALUES (2, 'Agile Principles, Patterns, and Practices in C#', 'Software engineering patterns and best practices', 'LIVE', TO_DATE('20/11/2005', 'DD/MM/YYYY'), '9780457150021', 3, 1);
INSERT INTO book (id, title, description, status, created_at, isbn, author_id, publisher_id)
    VALUES (3, 'Power of Habit', 'Learning the power of your own habits...', 'LIVE', TO_DATE('10/12/2017', 'DD/MM/YYYY'), '9780414520621', 3, 1);
INSERT INTO book (id, title, description, status, created_at, isbn, author_id, publisher_id)
    VALUES (4, 'The Siege', 'A story about the most cruel siege.','CREATING', TO_DATE('04/03/2021', 'DD/MM/YYYY'), null , 2, null);
INSERT INTO book (id, title, description, status, created_at, isbn, author_id, publisher_id)
    VALUES (5, 'On Writing', 'The best story ever written...','EDITING', TO_DATE('11/05/2018', 'DD/MM/YYYY'), '9784737150021', 3, null);
INSERT INTO book (id, title, description, status, created_at, isbn, author_id, publisher_id)
    VALUES (6, 'Night Watch', 'On a cold night, we had the night watch...','LIVE', TO_DATE('15/01/2019', 'DD/MM/YYYY'), '9784737147921', 5, 2);
INSERT INTO book (id, title, description, status, created_at, isbn, author_id, publisher_id)
    VALUES (7, 'The Argonauts', 'Jason and the argonauts.','LIVE', TO_DATE('20/09/2014', 'DD/MM/YYYY'), '9784149580021', 4, 4);
ALTER SEQUENCE book_id_seq restart with 8;