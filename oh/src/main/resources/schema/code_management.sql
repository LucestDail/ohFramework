-- 코드 그룹 테이블
CREATE TABLE IF NOT EXISTS code_group (
    group_id BIGINT AUTO_INCREMENT NOT NULL COMMENT '코드 그룹 ID',
    group_name VARCHAR(100) NOT NULL COMMENT '코드 그룹명',
    description VARCHAR(500) COMMENT '설명',
    use_yn CHAR(1) DEFAULT 'Y' COMMENT '사용여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    PRIMARY KEY (group_id)
) COMMENT '코드 그룹';

-- 코드 테이블
CREATE TABLE IF NOT EXISTS code (
    code_id BIGINT AUTO_INCREMENT NOT NULL COMMENT '코드 ID',
    group_id BIGINT NOT NULL COMMENT '코드 그룹 ID',
    code_name VARCHAR(100) NOT NULL COMMENT '코드명',
    code_value VARCHAR(100) NOT NULL COMMENT '코드값',
    parent_code_id BIGINT COMMENT '상위 코드 ID',
    description VARCHAR(500) COMMENT '설명',
    use_yn CHAR(1) DEFAULT 'Y' COMMENT '사용여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    PRIMARY KEY (code_id),
    FOREIGN KEY (group_id) REFERENCES code_group(group_id),
    FOREIGN KEY (parent_code_id) REFERENCES code(code_id)
) COMMENT '코드';

-- 코드 이력 테이블
CREATE TABLE IF NOT EXISTS code_history (
    history_id BIGINT AUTO_INCREMENT COMMENT '이력 ID',
    code_id BIGINT NOT NULL COMMENT '코드 ID',
    change_type VARCHAR(20) NOT NULL COMMENT '변경유형 (INSERT/UPDATE/DELETE)',
    before_value TEXT COMMENT '변경전 값',
    after_value TEXT COMMENT '변경후 값',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    PRIMARY KEY (history_id),
    FOREIGN KEY (code_id) REFERENCES code(code_id)
) COMMENT '코드 이력'; 