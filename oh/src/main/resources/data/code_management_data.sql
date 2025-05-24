-- 코드 그룹 더미 데이터
INSERT INTO code_group (group_name, description) VALUES
('사용자 유형', '시스템 사용자 유형 코드'),
('사용자 상태', '사용자 계정 상태 코드'),
('메뉴 유형', '시스템 메뉴 유형 코드');

-- 코드 더미 데이터
INSERT INTO code (group_id, code_name, code_value, description) VALUES
-- 사용자 유형 코드
(1, '관리자', 'ADMIN', '시스템 관리자'),
(1, '일반사용자', 'USER', '일반 사용자'),
(1, '게스트', 'GUEST', '게스트 사용자'),

-- 사용자 상태 코드
(2, '활성', 'ACTIVE', '활성 상태'),
(2, '비활성', 'INACTIVE', '비활성 상태'),
(2, '잠금', 'LOCKED', '계정 잠금 상태'),

-- 메뉴 유형 코드
(3, '메뉴', 'MENU', '일반 메뉴'),
(3, '버튼', 'BUTTON', '버튼 메뉴'),
(3, '링크', 'LINK', '링크 메뉴');

-- 하위 코드 예시 (메뉴 유형의 하위 코드)
INSERT INTO code (group_id, code_name, code_value, parent_code_id, description) VALUES
(3, '메인 메뉴', 'MAIN', 7, '메인 메뉴'),
(3, '서브 메뉴', 'SUB', 7, '서브 메뉴'),
(3, '추가 버튼', 'ADD', 8, '데이터 추가 버튼'),
(3, '수정 버튼', 'EDIT', 8, '데이터 수정 버튼'); 