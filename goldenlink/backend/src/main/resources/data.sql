INSERT INTO EntityUser (userid, userpw, name, phone, email, address, role)
SELECT 'root',
    '1234',
    '관리자',
    '010-0000-0000',
    'admin@goldenlink.com',
    '서울특별시',
    'ADMIN'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM EntityUser WHERE userid = 'root');
INSERT INTO EntityUser (userid, userpw, name, phone, email, address, role)
VALUES ('user1',
        '1234',
        '홍길동',
        '010-1111-1111',
        'user@test.com',
        '부산광역시',
        'USER'
        );
INSERT INTO boards (category, content, createdAt, title, userid)
VALUES ('NOTICE',
        '점검 시간이 변경되었습니다. 02시 ~ 04시',
        NOW(),
        '[공지/수정] 전체 서비스 점검 안내',
        1
       );
INSERT INTO boards (category, content, createdAt, title, userid)
VALUES ('QNA',
        '일반유저의 글입니다.',
        NOW(),
        '문의합니다',
        2
       );
INSERT INTO boards (category, content, createdAt, title, userid)
VALUES ('INFO',
        '이제 세션이 유지된 상태로 수정합니다!',
        NOW(),
        '정보공유 수정 완료',
        2
       );
INSERT INTO comments (boardid, createdAt, userid, content)
VALUES (2,
        NOW(),
        1,
        '관리자 답변입니다.'
       );