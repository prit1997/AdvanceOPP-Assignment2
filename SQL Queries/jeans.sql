Create database jeansMarket;
use jeansMarket;

create table jeans
(

brandName varchar(200),
jeanstypes varchar(200),
colour varchar(200),
price double,
soldPrice double,
size int(200),
itemNumber int(200) primary key,
image varchar(200)

);

select * from jeans;

insert into jeans 
(brandName, jeanstypes, colour ,price, soldPrice, size, itemNumber, image)
values ('TommyHilfigure','Deniem','black', 45, 54, 28, 1001, 'sufile');
insert into jeans 
(brandName, jeanstypes, colour ,price, soldPrice, size, itemNumber, image)
values ("Us Polo","Denim","Black",14.20,17.40,5,1002, 'vfhv');
insert into jeans 
(brandName, jeanstypes, colour ,price, soldPrice, size, itemNumber, image)
values ("TommyHillfigure","Trouser","Blue",16.20,18.20,7,1003, 'ghufg');
insert into jeans 
(brandName, jeanstypes, colour ,price, soldPrice, size, itemNumber, image)
values ("Gap","Formal","Grey",11.20,15.10,6,1004, 'vfhv');
insert into jeans 
(brandName, jeanstypes, colour ,price, soldPrice, size, itemNumber, image)
values ("Guess","Chinos","Brown",18.10,17.20,8,1005, 'vfhv');


/* create second table users */
create table users
(
userID int(200) not null  Auto_Increment Primary Key,
firstName varchar(200),
lastName varchar(200),
phoneNumber varchar(200),
birthDay date,
imageFile varchar(200)
);

insert into users
(firstName, lastName, phoneNumber, birthDay, imageFile)
values ("Prit","Patel","444-666-4545", '2002-01-02', 'some file');
insert into users
(firstName, lastName, phoneNumber, birthDay, imageFile)
values ("Kalp","Patel","555-666-5454", '2001-06-08', 'img file');

alter table users 
ADD column password varchar(200);

SET SQL_SAFE_UPDATES=0;

update users SET password = 'simple';

alter table users add column salt blob;

alter table users add column admin Boolean default false;
update users 
set admin = true 
where userID = 1;
select * from users;