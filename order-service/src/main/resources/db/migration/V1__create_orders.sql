CREATE TABLE IF NOT EXISTS orders (
                                      id BIGSERIAL PRIMARY KEY,
                                      user_email TEXT NOT NULL,
                                      total NUMERIC,
                                      status TEXT,
                                      payment_method VARCHAR(255),
                                      tracking_number VARCHAR(255),
                                      shipping NUMERIC,
                                      created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS order_items (
                                           id BIGSERIAL PRIMARY KEY,
                                           order_id BIGINT REFERENCES orders(id) ON DELETE CASCADE,
                                           product_id BIGINT NOT NULL,
                                           product_name TEXT NOT NULL,
                                           product_image TEXT,
                                           quantity INT,
                                           price NUMERIC
);
