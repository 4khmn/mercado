ALTER TABLE users
    add COLUMN balance numeric(19,2) not null default 0;

ALTER TABLE orders
    add COLUMN status VARCHAR(255);