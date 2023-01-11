INSERT INTO manufacturer (name) VALUES
    ('MAFRA'),
    ('Pro Staff'),
    ('MAROLEX');

INSERT INTO category (name) VALUES
    ('프리워시'),
    ('본세차'),
    ('LSP'),
    ('세차용품');

DO $$
DECLARE
    M0 bigint := (SELECT id FROM manufacturer WHERE name = 'MAFRA');
    M1 bigint := (SELECT id FROM manufacturer WHERE name = 'Pro Staff');
    M2 bigint := (SELECT id FROM manufacturer WHERE name = 'MAROLEX');
    C0 bigint := (SELECT id FROM category WHERE name = '프리워시');
    C1 bigint := (SELECT id FROM category WHERE name = '본세차');
    C2 bigint := (SELECT id FROM category WHERE name = 'LSP');
    C3 bigint := (SELECT id FROM category WHERE name = '세차용품');
BEGIN
    INSERT INTO product (name, price, manufacturer_id, category_id, image_link, simple_detail, registered_date) VALUES
        ('라보코스메티카 프리머스 1000mL', 28000, M0, C0, '/images/product/b0d9d2e0.jpeg', '약알칼리성 프리워시, 스프레이/폼건 가용가능', CURRENT_TIMESTAMP),
        ('라보코스메티카 프리머스 4.5L', 98000, M0, C0, '/images/product/03e4d408.jpeg', '약알칼리성 프리워시, 스프레이/폼건 가용가능', CURRENT_TIMESTAMP),
        ('마프라 휠앤타이어클리너 400mL', 14000, M0, C1, '/images/product/01d61812.jpeg', '변색된 타이어를 본연의 색으로, 터치 없이 휠 분진 제거', CURRENT_TIMESTAMP),
        ('마프라 휠앤타이어클리너 4.5L', 74000, M0, C1, NULL, '변색된 타이어를 본연의 색으로, 터치 없이 휠 분진 제거', CURRENT_TIMESTAMP),
        ('라보코스메티카 퓨리피카 1000mL', 40000, M0, C0, '/images/product/fd00b760.jpeg', '석회 및 물때만을 제거하여 무너진 비딩 재생 탁월한 산성 카 샴푸', CURRENT_TIMESTAMP),
        ('라보코스메티카 퓨리피카 4.5L', 108000, M0, C0, '/images/product/006b50b8.jpeg', '석회 및 물때만을 제거하여 무너진 비딩 재생 탁월한 산성 카 샴푸', CURRENT_TIMESTAMP),
        ('라보코스메티카 셈퍼 500mL', 30000, M0, C1, '/images/product/06f36c22.jpeg', '희석비 깡패, 뛰어난 윤할력의 중성 카 샴푸', CURRENT_TIMESTAMP),
        ('라보코스메티카 비트레오 250g', 38000, M0, C1, '/images/product/80e5387c.jpeg', '라보코스메티카 유막제거+글라스 폴리쉬', CURRENT_TIMESTAMP),
        ('흑광 고체왁스 180g', 24900, M1, C2, '/images/product/42bded84.jpeg', '차량 본연의 색을 더욱 깊고 진하게!, 블랙 계열 차량 전용 왁스', CURRENT_TIMESTAMP),
        ('백광 고체왁스 200g', 24900, M1, C2, '/images/product/45577e20.jpeg', '"차량 본연의 색을 더욱 깊고 진하게!, 화이트 계열 차량 전용 왁스', CURRENT_TIMESTAMP),
        ('CC 워터골드 480mL', 29000, M1, C2, '/images/product/489a760a.jpeg', '젖은 상태 OK!, 광택/코팅/발수, 연마제 無', CURRENT_TIMESTAMP),
        ('인더스트리 에르고 EPDM 3000', 41000, M2, C3, '/images/product/dfe75a2e.png', '마로렉스 알칼리 내성 압축분무기', CURRENT_TIMESTAMP);
END $$;

DO $$
DECLARE
    P0 bigint := (SELECT id FROM product WHERE name = '라보코스메티카 프리머스 1000mL');
    P1 bigint := (SELECT id FROM product WHERE name = '라보코스메티카 프리머스 4.5L');
    P2 bigint := (SELECT id FROM product WHERE name = '마프라 휠앤타이어클리너 400mL');
    P3 bigint := (SELECT id FROM product WHERE name = '마프라 휠앤타이어클리너 4.5L');
    P4 bigint := (SELECT id FROM product WHERE name = '라보코스메티카 퓨리피카 1000mL');
    P5 bigint := (SELECT id FROM product WHERE name = '라보코스메티카 퓨리피카 4.5L');
BEGIN
    INSERT INTO product_options (product_id, option_id) VALUES
        (P0, P1),
        (P2, P3),
        (P4, P5);
END $$;
