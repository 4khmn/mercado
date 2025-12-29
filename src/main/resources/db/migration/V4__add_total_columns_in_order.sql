ALTER TABLE orders
    add COLUMN "total_price" numeric(19,2) not null default 0;

ALTER TABLE orders
    add COLUMN total_items_quantity INTEGER not null default 0;
