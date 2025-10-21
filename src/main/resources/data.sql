--INSERT INTO rules (rule_name, priority, strategy_key, active_from, active_until, conditions, parameters, region)
--VALUES ('Diwali Offer', 10, 'FestivalPricingStrategy', '2025-10-01 00:00:00', '2025-10-31 23:59:59',
--        '{"category":"Electronics"}', '{"discountPercentage":20}', 'IN');

--INSERT INTO rules (rule_name, priority, strategy_key, active_from, active_until, conditions, parameters, region)
--VALUES ('Diwali Offer', 1, 'FESTIVAL_DISCOUNT', '2025-10-01', '2025-10-31', '{"category":"Electronics"}', '{"festivalDiscount":10}', 'IN'),
--       ('Seasonal Offer', 2, 'SEASONAL_DISCOUNT', '2025-09-01', '2025-12-31', '{"category":"Electronics"}', '{"seasonalDiscount":5}', 'IN'),
--       ('Stock Clearance', 3, 'CLEARANCE_DISCOUNT', '2025-10-15', '2025-10-25', '{"category":"Electronics"}', '{"clearanceDiscount":20}', 'IN'),
--       ('Gold Member', 4, 'MEMBERSHIP_DISCOUNT', '2025-01-01', '2025-12-31', '{"tier":"GOLD"}', '{"membershipTier":"GOLD"}', 'IN'),
--       ('Referral Bonus', 5, 'REFERRAL_DISCOUNT', '2025-01-01', '2025-12-31', '{"referred":true}', '{"isReferred":true}', 'IN');

--INSERT INTO products (id, product_code, product_name, base_price, currency, product_category, available_stock)
--VALUES (1, 'iphone17', 'iPhone 17', 15000, 'INR', 'Electronics', 100);
--
--INSERT INTO customers (id, name, tier, email, joined_at, metadata)
--VALUES (101, 'Omkar', 'PRIME', 'omkar@example.com', '2024-01-01 00:00:00', '{"region":"IN"}');
