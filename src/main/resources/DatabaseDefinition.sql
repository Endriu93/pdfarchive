#--drop index Documents_ADD_DATE on Documents;
#--create index  Documents_ADD_DATE on Documents (ADD_DATE);


create table if not exists Authors (
AUTHOR_ID int auto_increment not null,
NAME text not null,
PRIMARY KEY(AUTHOR_ID),
unique key(NAME(25)));

#--drop index Authors_AUTHOR_NAME on Authors; 
#--create index  Authors_AUTHOR_NAME on Authors(NAME(10));

create table if not exists Titles (
TITLE_ID int auto_increment  not null primary key,
NAME text(25) not null,
unique key (NAME(25)));

#--drop index Titles_Name on Titles;
#--create index  Titles_NAME on Titles(NAME(10));

create table if not exists Words (
WORD_ID int auto_increment not null primary key,
NAME text not null,
unique key (NAME(25)));

#--drop index Words_Name on Words;
#--create index  Words_NAME on Words(NAME(10));

create table if not exists Documents (
DOCUMENT_ID int auto_increment not null primary key,
DATA LONGBLOB not null,	-- raw Data
AUTHOR_ID int not null,
TITLE_ID int not null,
DESCRIPTION text not null,
ADD_DATE date not null,
CREATE_DATE date default null, #--some PDFs hasn't this entry
SIZE int not null check (SIZE>0),
FOREIGN KEY(AUTHOR_ID) references Authors(AUTHOR_ID),
FOREIGN KEY(TITLE_ID) references Titles(TITLE_ID));#	-- number of pages

create table if not exists DocumentWord (
DOCUMENT_ID int not null,
WORD_ID int not null,
FOREIGN KEY(DOCUMENT_ID) references Documents(DOCUMENT_ID) ON DELETE CASCADE,
FOREIGN KEY(WORD_ID) references Words(WORD_ID) ON DELETE CASCADE);

create table if not exists IndexInfo (
DOCUMENT_ID int not null,
FOREIGN KEY(DOCUMENT_ID) references Documents(DOCUMENT_ID) ON DELETE CASCADE,
IS_INDEXED Bool not null #-- if is indexed to words table
);

create index  IndexInfo_IS_INDEXED on IndexInfo(IS_INDEXED);

create table if not exists Tags (
TAG_ID int not null auto_increment primary key,
NAME text not null,
unique key (NAME(25)));

#--drop index Tags_NAME on Tags;
#--create index  Tags_NAME on Tags(NAME(10));

create table if not exists DocumentTag (
DOCUMENT_ID int not null,
TAG_ID int not null,
FOREIGN KEY(DOCUMENT_ID) references Documents(DOCUMENT_ID) ON DELETE CASCADE,
FOREIGN KEY(TAG_ID) references Tags(TAG_ID) ON DELETE CASCADE,
PRIMARY KEY(DOCUMENT_ID,TAG_ID));

create table if not exists Categories (
CATEGORY_ID int not null auto_increment primary key,
NAME text not null,
unique key (NAME(25)));

insert into Categories values(1,'Rozne');
#--drop index Category_NAME on Categories;
#--create index  Category_NAME on Categories(NAME(10));

create table if not exists DocumentCategory (
DOCUMENT_ID int not null,
CATEGORY_ID int not null,
FOREIGN KEY(DOCUMENT_ID) references Documents(DOCUMENT_ID) ON DELETE CASCADE,
FOREIGN KEY(CATEGORY_ID) references Categories(CATEGORY_ID) ON DELETE CASCADE);

create table if not exists Users (
USER_ID int primary key not null, #-- it will be used as sessionid hence this number must be complicated
LOGIN text not null,
PASSWORD text not null,
unique key (LOGIN(30)));	#-- force in code to have login greater than 30 characters.

create table if not exists DocumentUser (
DOCUMENT_ID int not null,
USER_ID int not null,
FOREIGN KEY(DOCUMENT_ID) references Documents(DOCUMENT_ID) ON DELETE CASCADE,
FOREIGN KEY(USER_ID) references Users(USER_ID) ON DELETE CASCADE,
PRIMARY KEY(DOCUMENT_ID,USER_ID));

create table if not exists CategoryUser (
CATEGORY_ID int not null,
USER_ID int not null,
FOREIGN KEY(CATEGORY_ID) references Categories(CATEGORY_ID) ON DELETE CASCADE,
FOREIGN KEY(USER_ID) references Users(USER_ID) ON DELETE CASCADE,
PRIMARY KEY(CATEGORY_ID,USER_ID));

CREATE TRIGGER onUserCreate after INSERT on Users 
	FOR EACH ROW insert into CategoryUser values((select CATEGORY_ID from Categories where NAME='Rozne'),NEW.USER_ID);
	
#-- add users; 	
INSERT INTO Users values(182934,'user','user');	
INSERT INTO Users values(294562,'user1','user1');	
INSERT INTO Users values(125429034,'user2','user2');	

