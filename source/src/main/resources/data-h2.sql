INSERT INTO manufacturer (name) VALUES
    ('MAFRA'),
    ('Pro Staff'),
    ('MAROLEX');

INSERT INTO category (name) VALUES
    ('프리워시'),
    ('본세차'),
    ('LSP'),
    ('세차용품');

SET @M0 = SELECT id FROM manufacturer WHERE name = 'MAFRA';
SET @M1 = SELECT id FROM manufacturer WHERE name = 'Pro Staff';
SET @M2 = SELECT id FROM manufacturer WHERE name = 'MAROLEX';
SET @C0 = SELECT id FROM category WHERE name = '프리워시';
SET @C1 = SELECT id FROM category WHERE name = '본세차';
SET @C2 = SELECT id FROM category WHERE name = 'LSP';
SET @C3 = SELECT id FROM category WHERE name = '세차용품';

INSERT INTO product (name, price, manufacturer_id, category_id, image_link, simple_detail, registered_date) VALUES
    ('라보코스메티카 프리머스 1000mL', 28000, @M0, @C0, '/images/product/b0d9d2e0.jpeg', '약알칼리성 프리워시, 스프레이/폼건 가용가능', CURRENT_TIMESTAMP),
    ('라보코스메티카 프리머스 4.5L', 98000, @M0, @C0, '/images/product/03e4d408.jpeg', '약알칼리성 프리워시, 스프레이/폼건 가용가능', CURRENT_TIMESTAMP),
    ('마프라 휠앤타이어클리너 400mL', 14000, @M0, @C1, '/images/product/01d61812.jpeg', '변색된 타이어를 본연의 색으로, 터치 없이 휠 분진 제거', CURRENT_TIMESTAMP),
    ('마프라 휠앤타이어클리너 4.5L', 74000, @M0, @C1, NULL, '변색된 타이어를 본연의 색으로, 터치 없이 휠 분진 제거', CURRENT_TIMESTAMP),
    ('라보코스메티카 퓨리피카 1000mL', 40000, @M0, @C0, '/images/product/fd00b760.jpeg', '석회 및 물때만을 제거하여 무너진 비딩 재생 탁월한 산성 카 샴푸', CURRENT_TIMESTAMP),
    ('라보코스메티카 퓨리피카 4.5L', 108000, @M0, @C0, '/images/product/006b50b8.jpeg', '석회 및 물때만을 제거하여 무너진 비딩 재생 탁월한 산성 카 샴푸', CURRENT_TIMESTAMP),
    ('라보코스메티카 셈퍼 500mL', 30000, @M0, @C1, '/images/product/06f36c22.jpeg', '희석비 깡패, 뛰어난 윤할력의 중성 카 샴푸', CURRENT_TIMESTAMP),
    ('라보코스메티카 비트레오 250g', 38000, @M0, @C1, '/images/product/80e5387c.jpeg', '라보코스메티카 유막제거+글라스 폴리쉬', CURRENT_TIMESTAMP),
    ('흑광 고체왁스 180g', 24900, @M1, @C2, '/images/product/42bded84.jpeg', '차량 본연의 색을 더욱 깊고 진하게!, 블랙 계열 차량 전용 왁스', CURRENT_TIMESTAMP),
    ('백광 고체왁스 200g', 24900, @M1, @C2, '/images/product/45577e20.jpeg', '"차량 본연의 색을 더욱 깊고 진하게!, 화이트 계열 차량 전용 왁스', CURRENT_TIMESTAMP),
    ('CC 워터골드 480mL', 29000, @M1, @C2, '/images/product/489a760a.jpeg', '젖은 상태 OK!, 광택/코팅/발수, 연마제 無', CURRENT_TIMESTAMP),
    ('인더스트리 에르고 EPDM 3000', 41000, @M2, @C3, '/images/product/dfe75a2e.png', '마로렉스 알칼리 내성 압축분무기', CURRENT_TIMESTAMP);

SET @P0 = SELECT id FROM product WHERE name = '라보코스메티카 프리머스 1000mL';
SET @P1 = SELECT id FROM product WHERE name = '라보코스메티카 프리머스 4.5L';
SET @P2 = SELECT id FROM product WHERE name = '마프라 휠앤타이어클리너 400mL';
SET @P3 = SELECT id FROM product WHERE name = '마프라 휠앤타이어클리너 4.5L';
SET @P4 = SELECT id FROM product WHERE name = '라보코스메티카 퓨리피카 1000mL';
SET @P5 = SELECT id FROM product WHERE name = '라보코스메티카 퓨리피카 4.5L';

INSERT INTO product_options (product_id, option_id) VALUES
    (@P0, @P1),
    (@P2, @P3),
    (@P4, @P5);


INSERT INTO account (name, username, password, registered_date) VALUES ('User', 'user', '', CURRENT_TIMESTAMP);
SET @A0 = SELECT id FROM account WHERE username = 'user';

INSERT INTO account_roles (account_id, role) VALUES
    (@A0, 'ROLE_USER');
INSERT INTO member (account_id, email, mobile_number, zipcode, address) VALUES
    (@A0, 'user@domain.com', '010-1200-3400', '42700', '대구광역시 달서구');
INSERT INTO agreement (member_id, terms1, terms2, terms3, acceptance_date, acceptance, update_date) VALUES
    (@A0, true, true, true, CURRENT_TIMESTAMP, true, CURRENT_TIMESTAMP)
