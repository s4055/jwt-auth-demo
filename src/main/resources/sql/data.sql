INSERT INTO users (users_id, email, password, user_name, birth_dt, join_dt, reg_dt, status) VALUES ('3ef410d6-9abb-4f60-881e-1c37d8d1b5c0', 'user1@example.com', 'password123!!', '김철수', '1990-05-15', NOW(), NOW(), 'ACTIVE');
INSERT INTO users (users_id, email, password, user_name, birth_dt, join_dt, reg_dt, status) VALUES ('d1b1ea33-aaf1-44fb-857b-9923e2641c8f', 'user2@example.com', 'securepass456!!', '이영희', '1988-11-22', NOW(), NOW(), 'ACTIVE');
INSERT INTO users (users_id, email, password, user_name, birth_dt, join_dt, reg_dt, status) VALUES ('58a2cdd1-5eb4-4bc2-b9ed-c6d1082d1822', 'user3@example.com', 'mysecret789!!', '박민수', '1995-07-08', NOW(), NOW(), 'ACTIVE');
INSERT INTO users (users_id, email, password, user_name, birth_dt, join_dt, reg_dt, status) VALUES ('ae46453b-889d-4670-8132-9b9ffa3b4a68', 'user4@example.com', 'pass4322221!', '최지현', '1985-02-19', NOW(), NOW(), 'ACTIVE');
INSERT INTO users (users_id, email, password, user_name, birth_dt, join_dt, reg_dt, status) VALUES ('b241c05e-fb88-4106-ac72-073cb962228d', 'user5@example.com', 'qwerty9876!!!', '정우성', '1993-09-30', NOW(), NOW(), 'ACTIVE');

INSERT INTO board (title, contents, writer, users_id, reg_dt) VALUES ('첫 번째 게시글', '이것은 첫 번째 게시글의 내용입니다.', '김철수', '3ef410d6-9abb-4f60-881e-1c37d8d1b5c0', NOW());
INSERT INTO board (title, contents, writer, users_id, reg_dt) VALUES ('두 번째 게시글', '두 번째 게시글 내용입니다. 여러 줄 작성 가능.', '이영희', 'd1b1ea33-aaf1-44fb-857b-9923e2641c8f', NOW());
INSERT INTO board (title, contents, writer, users_id, reg_dt) VALUES ('세 번째 게시글', '세 번째 게시글에는 특별한 내용이 없습니다.', '박민수', '58a2cdd1-5eb4-4bc2-b9ed-c6d1082d1822', NOW());
INSERT INTO board (title, contents, writer, users_id, reg_dt) VALUES ('네 번째 게시글', '이것은 네 번째 게시글의 내용입니다.', '김철수', '3ef410d6-9abb-4f60-881e-1c37d8d1b5c0', NOW());
INSERT INTO board (title, contents, writer, users_id, reg_dt) VALUES ('다섯 번째 게시글', '다섯 번째 게시글 내용입니다. 여러 줄 작성 가능.', '이영희', 'd1b1ea33-aaf1-44fb-857b-9923e2641c8f', NOW());
INSERT INTO board (title, contents, writer, users_id, reg_dt) VALUES ('여섯 번째 게시글', '여섯 번째 게시글에는 특별한 내용이 없습니다.', '박민수', '58a2cdd1-5eb4-4bc2-b9ed-c6d1082d1822', NOW());
INSERT INTO board (title, contents, writer, users_id, reg_dt) VALUES ('일곱 번째 게시글', '일곱 번째 게시글 내용입니다. 여러 줄 작성 가능.', '이영희', 'd1b1ea33-aaf1-44fb-857b-9923e2641c8f', NOW());
INSERT INTO board (title, contents, writer, users_id, reg_dt) VALUES ('여덟 번째 게시글', '여덟 번째 게시글에는 특별한 내용이 없습니다.', '박민수', '58a2cdd1-5eb4-4bc2-b9ed-c6d1082d1822', NOW());
