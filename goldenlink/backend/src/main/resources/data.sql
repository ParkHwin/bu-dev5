INSERT INTO entity_user (userid, userpw, name, phone, email, address, role)
SELECT 'root',
    '1234',
    '관리자',
    '010-0000-0000',
    'admin@goldenlink.com',
    '서울특별시',
    'ADMIN'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM entity_user WHERE userid = 'root');