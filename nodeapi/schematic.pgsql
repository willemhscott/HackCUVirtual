CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;

create table users
(
	username public.citext not null,
	display_name public.citext not null,
	age int not null,
	password text not null,
	email_address text not null,
	gender text not null,
	favorites json not null,
	allergens json not null,
	covid bool default false not null
);

create unique index users_email_address_uindex
	on users (email_address);

create unique index users_username_uindex
	on users (username);

alter table users
	add constraint users_pk
		primary key (email_address);

