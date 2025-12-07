-- Таблица users
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255),
                       roles VARCHAR(255)
);

-- Таблица shops
CREATE TABLE shops (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description VARCHAR(255)
);

-- Таблица products
CREATE TABLE products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          description VARCHAR(255),
                          price DECIMAL(19,2) NOT NULL,
                          stock INT,
                          shop_id BIGINT NOT NULL,
                          CONSTRAINT fk_products_shop FOREIGN KEY (shop_id) REFERENCES shops(id)
);

-- Таблица orders
CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        created_at TIMESTAMP,
                        CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Таблица order_items
CREATE TABLE order_items (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             product_id BIGINT NOT NULL,
                             quantity BIGINT NOT NULL,
                             price DECIMAL(19,2) NOT NULL,
                             CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id),
                             CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Таблица cart_item
CREATE TABLE cart_item (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           product_id BIGINT NOT NULL,
                           user_id BIGINT NOT NULL,
                           quantity BIGINT NOT NULL,
                           price DECIMAL(19,2) NOT NULL,
                           CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES products(id),
                           CONSTRAINT fk_cart_item_user FOREIGN KEY (user_id) REFERENCES users(id)
);