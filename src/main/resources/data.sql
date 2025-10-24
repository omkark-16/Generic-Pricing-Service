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


-- 1. Festival Discount Strategy (e.g. Diwali Offer)
INSERT INTO rules (rule_name, priority, strategy_key, active_from, active_until, conditions, parameters, region)
VALUES ('Diwali Offer', 1, 'FESTIVAL_DISCOUNT', '2025-10-01', '2025-10-31', '{"category":"Electronics"}', '{"festivalDiscount":10}', 'IN');

-- 2. Seasonal Discount Strategy
INSERT INTO rules (rule_name, priority, strategy_key, active_from, active_until, conditions, parameters, region)
VALUES ('Winter Sale', 2, 'SEASONAL_DISCOUNT', '2025-12-01', '2026-02-28', '{"category":"Clothing"}', '{"seasonalDiscount":15}', 'IN');

-- 3. Clearance Discount Strategy
INSERT INTO rules (rule_name, priority, strategy_key, active_from, active_until, conditions, parameters, region)
VALUES ('Stock Clearance', 3, 'CLEARANCE_DISCOUNT', '2025-10-15', '2025-10-25', '{"category":"Electronics"}', '{"clearanceDiscount":20}', 'IN');

-- 4. New Product Introduction Strategy
INSERT INTO rules (rule_name, priority, strategy_key, active_from, active_until, conditions, parameters, region)
VALUES ('New Launch Offer', 4, 'NEW_PRODUCT_INTRO', '2025-11-01', '2025-12-15', '{"category":"Gadgets"}', '{"introDiscount":8}', 'IN');

-- 5. Volume Based Strategy
INSERT INTO rules (rule_name, priority, strategy_key, active_from, active_until, conditions, parameters, region)
VALUES ('Bulk Purchase Offer', 5, 'VOLUME_BASED_DISCOUNT', '2025-01-01', '2025-12-31', '{"minQuantity":5}', '{"volumeDiscount":12}', 'IN');

-- 6. Loyalty Strategy
INSERT INTO rules (rule_name, priority, strategy_key, active_from, active_until, conditions, parameters, region)
VALUES ('Loyalty Customer Offer', 6, 'LOYALTY_DISCOUNT', '2025-01-01', '2025-12-31', '{"customerType":"LOYAL"}', '{"loyaltyDiscount":10}', 'IN');

-- 7. Loyalty Points Strategy
INSERT INTO rules (rule_name, priority, strategy_key, active_from, active_until, conditions, parameters, region)
VALUES ('Loyalty Points Redemption', 7, 'LOYALTY_POINTS_DISCOUNT', '2025-01-01', '2025-12-31', '{"hasPoints":true}', '{"pointsValue":200}', 'IN');

-- 8. Membership Strategy
INSERT INTO rules (rule_name, priority, strategy_key, active_from, active_until, conditions, parameters, region)
VALUES ('Gold Membership Discount', 8, 'MEMBERSHIP_DISCOUNT', '2025-01-01', '2025-12-31', '{"tier":"GOLD"}', '{"membershipDiscount":15}', 'IN');

-- 9. Referral Strategy
INSERT INTO rules (rule_name, priority, strategy_key, active_from, active_until, conditions, parameters, region)
VALUES ('Referral Bonus', 9, 'REFERRAL_DISCOUNT', '2025-01-01', '2025-12-31', '{"referred":true}', '{"referralDiscount":5}', 'IN');

-- 10. Festival + Membership Combined Strategy (example composite rule)
INSERT INTO rules (rule_name, priority, strategy_key, active_from, active_until, conditions, parameters, region)
VALUES ('Diwali Gold Combo Offer', 10, 'FESTIVAL_MEMBERSHIP_COMBO', '2025-10-01', '2025-10-31', '{"tier":"GOLD","category":"Electronics"}', '{"comboDiscount":20}', 'IN');

--
-- -- Products
-- INSERT INTO products (id, product_code, product_name, base_price, currency, product_category, available_stock)
-- VALUES
-- (1, 'iphone17', 'iPhone 17', 150000, 'INR', 'Electronics', 100),
-- (2, 'sweater2025', 'Winter Sweater', 2500, 'INR', 'Clothing', 200),
-- (3, 'airpodsmax', 'AirPods Max', 45000, 'INR', 'Electronics', 50);
--
-- -- Customers
-- INSERT INTO customers (id, name, tier, email, joined_at, metadata)
-- VALUES
-- (101, 'Omkar', 'GOLD', 'omkar@example.com', '2024-01-01 00:00:00', '{"region":"IN","hasPoints":true,"referred":true}'),
-- (102, 'Hrutwik', 'PRIME', 'hrutwik@example.com', '2023-12-15 00:00:00', '{"region":"IN","hasPoints":false,"referred":false}');
