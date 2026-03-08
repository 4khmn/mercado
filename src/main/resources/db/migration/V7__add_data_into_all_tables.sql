-- Insert users
INSERT INTO users (id, username, email, password, roles, balance) VALUES
    (1, 'admin', 'admin@example.com', '$2a$10$abcdefghijklmnopqrstuv', 'ROLE_ADMIN', 1000.00),
    (2, 'john_doe', 'john.doe@example.com', '$2a$10$abcdefghijklmnopqrstuv', 'ROLE_USER', 250.00),
    (3, 'jane_doe', 'jane.doe@example.com', '$2a$10$abcdefghijklmnopqrstuv', 'ROLE_USER', 500.00);

-- Insert shops
INSERT INTO shops (id, name, description) VALUES
    (1, 'Tech Store', 'Electronics and gadgets'),
    (2, 'Book House', 'Books and stationery');

-- Insert products
INSERT INTO products (id, title, description, price, stock, shop_id, category) VALUES
    (1, 'Laptop', 'Powerful laptop for work and gaming', 1500.00, 10, 1, 'ELECTRONICS'),
    (2, 'Headphones', 'Noise-cancelling over-ear headphones', 200.00, 50, 1, 'ELECTRONICS'),
    (3, 'Programming Book', 'Java programming book for beginners', 40.00, 100, 2, 'BOOKS'),
    (4, 'Notebook', 'Hardcover notebook for notes', 10.00, 200, 2, 'BOOKS');

-- Insert orders
INSERT INTO orders (id, user_id, created_at, total_price, total_items_quantity, status) VALUES
    (1, 2, NOW(), 1740.00, 2, 'COMPLETED'),
    (2, 3, NOW(), 50.00, 2, 'PENDING');

-- Insert order_items
INSERT INTO order_items (id, order_id, product_id, quantity, price) VALUES
    (1, 1, 1, 1, 1500.00), -- Laptop
    (2, 1, 2, 1, 240.00),  -- Headphones (with discount applied in total)
    (3, 2, 3, 1, 40.00),   -- Programming Book
    (4, 2, 4, 1, 10.00);   -- Notebook

-- Insert cart items
INSERT INTO cart_item (id, product_id, user_id, quantity) VALUES
    (1, 2, 2, 2), -- John has 2 headphones in cart
    (2, 3, 2, 1), -- John has 1 programming book in cart
    (3, 4, 3, 3); -- Jane has 3 notebooks in cart

