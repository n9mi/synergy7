--- Create procedure
create or replace procedure
	generate_dummy_user()
language sql
as $$
insert into users (
	id,
	created_date,
	updated_date,
	deleted_date,
	email,
	"password",
	"token",
	"username")
 values (
	gen_random_uuid(),
	now(),
	null,
	null,
	null,
	'',
	null,
	md5(random()::text));
$$;

-- Update procedure
create or replace procedure
	authorize_user("ID" uuid)
language sql
as $$
update users set token = gen_random_uuid() where id="ID";
$$;

-- Delete procedure
create or replace procedure
	clean_dummy_users()
language sql
as $$
delete from users where token is null or email is null;
$$;
