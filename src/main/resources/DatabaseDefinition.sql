create table if not exists Documents (
DOCUMENT_ID int auto_increment not null primary key,
DATA LONGBLOB not null,	-- raw Data
AUTHOR_ID int not null references Authors(AUTHOR_ID),
TITLE_ID text not null references Titles(TITLE_ID),
DESCRIPTION text not null,
ADD_DATE date not null,
CREATE_DATE date default null, #--some PDFs hasn't this entry
SIZE int not null check (SIZE>0) );#	-- number of pages

#--drop index Documents_ADD_DATE on Documents;
#--create index  Documents_ADD_DATE on Documents (ADD_DATE);

create table if not exists Authors (
AUTHOR_ID int auto_increment primary key not null,
NAME text not null,
unique key (NAME(25)));

#--drop index Authors_AUTHOR_NAME on Authors; 
#--create index  Authors_AUTHOR_NAME on Authors(NAME(10));

create table if not exists Titles (
TITLE_ID int auto_increment  not null primary key,
NAME text(25) not null,
unique key (NAME(25)));

#--drop index Titles_Name on Titles;
#--create index  Titles_NAME on Titles(NAME(10));

create table if not exists Words (
WORD_ID int auto_increment not null primary key ,
NAME text not null,
unique key (NAME(25)));

#--drop index Words_Name on Words;
#--create index  Words_NAME on Words(NAME(10));

create table if not exists DocumentWord (
DOCUMENT_ID int not null references Documents(DOCUMENT_ID) ON DELETE CASCADE,
WORD_ID int not null references Words(WORD_ID) ON DELETE CASCADE );

create table if not exists IndexInfo (
DOCUMENT_ID int not null references Documents(DOCUMENT_ID) ON DELETE CASCADE,
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
DOCUMENT_ID int not null references Documents(DOCUMENT_ID) ON DELETE CASCADE,
TAG_ID int not null references Tags(TAG_ID) ON DELETE CASCADE );

create table if not exists Categories (
CATEGORY_ID int not null auto_increment primary key,
NAME text not null,
unique key (NAME(25)));

#--drop index Category_NAME on Categories;
#--create index  Category_NAME on Categories(NAME(10));

create table if not exists DocumentCategory (
DOCUMENT_ID int not null references Documents(DOCUMENT_ID) ON DELETE CASCADE,
CATEGORY_ID int not null references Categories(CATEGORY_ID) ON DELETE CASCADE );
