INSERT INTO rules (rule_name, priority, strategy_key, active_from, active_until, conditions, parameters, region)
VALUES ('Diwali Offer', 10, 'FestivalPricingStrategy', '2025-10-01 00:00:00', '2025-10-31 23:59:59',
        '{"category":"Electronics"}', '{"discountPercentage":20}', 'IN');

INSERT INTO products (id, product_code, name, base_price, currency, product_category, available_stock)
VALUES (1, 'iphone17', 'iPhone 17', 15000, 'INR', 'Electronics', 100);

INSERT INTO customers (id, name, tier, email, joined_at, metadata)
VALUES (101, 'Omkar', 'PRIME', 'omkar@example.com', '2024-01-01 00:00:00', '{"region":"IN"}');
