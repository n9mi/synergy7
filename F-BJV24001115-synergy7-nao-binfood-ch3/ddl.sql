create table users (
	id UUID primary key default gen_random_uuid(),
	username varchar(20),
	email_address varchar(20),
	password text
);

create table merchants (
	id UUID primary key default gen_random_uuid(),
	merchant_name varchar(50),
	merchant_location text,
	open bool
);

create table products (
	id UUID primary key default gen_random_uuid(),
	product_name varchar(50),
	price decimal,
	merchant_id UUID,
	foreign key (merchant_id) references merchants(id)
);

create table orders (
	id UUID primary key default gen_random_uuid(),
	order_time timestamp,
	destination_address text,
	user_id UUID,
	completed bool,
	foreign key (user_id) references users(id)
);

create table order_details(
	id UUID primary key default gen_random_uuid(),
	order_id UUID,
	product_id UUID,
	quantity integer,
	total_price decimal,
	foreign key (order_id) references orders(id),
	foreign key (product_id) references products(id)
);

