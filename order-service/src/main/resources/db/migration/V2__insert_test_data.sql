-- Insert 100 test orders
DO $$
    BEGIN
        FOR i IN 1..100 LOOP
                INSERT INTO orders (
                    id, user_email, total, status, payment_method,
                    tracking_number, shipping, created_at
                )
                VALUES (
                           i,
                           CONCAT('user', '@example.com'),
                           ROUND((10 + RANDOM() * 90)::numeric, 2),
                           CASE i % 3
                               WHEN 0 THEN 'PLACED'
                               WHEN 1 THEN 'SHIPPED'
                               ELSE 'DELIVERED'
                               END,
                           CASE WHEN RANDOM() < 0.5 THEN 'Apple Pay' ELSE 'Credit Card' END,
                           CONCAT('TRK', LPAD(i::text, 6, '0')),
                           10,
                           NOW() - (i || ' days')::INTERVAL
                       );
            END LOOP;
    END $$;

-- Insert 2 to 4 order_items per order
DO $$
    DECLARE
        order_id INT;
        item_count INT;
        product_idx INT;
    BEGIN
        FOR order_id IN 1..100 LOOP
                item_count := 2 + (order_id % 3);

                FOR i IN 1..item_count LOOP
                        product_idx := (order_id + i) % 50;

                        INSERT INTO order_items (
                            order_id, product_id, product_name, product_image,
                            quantity, price
                        )
                        VALUES (
                                   order_id,
                                   1000 + product_idx,
                                   CONCAT('Product ', product_idx),
                                   CONCAT('https://picsum.photos/seed/', product_idx, '/300/300'),
                                   1 + (order_id % 3),
                                   ROUND((10 + RANDOM() * 40)::numeric, 2)
                               );
                    END LOOP;
            END LOOP;
    END $$;
