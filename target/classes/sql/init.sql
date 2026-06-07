-- 创建用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `id_card` VARCHAR(18) NOT NULL UNIQUE COMMENT '身份证号',
    `phone` VARCHAR(11) NOT NULL UNIQUE COMMENT '手机号',
    `trade_password` VARCHAR(100) NOT NULL COMMENT '交易密码',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_id_card` (`id_card`),
    UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 创建账户表
CREATE TABLE IF NOT EXISTS `bank_account` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `account_no` VARCHAR(20) NOT NULL UNIQUE COMMENT '账号',
    `balance` DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-冻结',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_account_no` (`account_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户表';

-- 创建交易记录表
CREATE TABLE IF NOT EXISTS `bank_transaction` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `account_id` BIGINT NOT NULL COMMENT '账户ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `trans_type` TINYINT NOT NULL COMMENT '交易类型：1-存款，2-取款',
    `amount` DECIMAL(18,2) NOT NULL COMMENT '交易金额',
    `balance_after` DECIMAL(18,2) NOT NULL COMMENT '交易后余额',
    `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_account_id` (`account_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易记录表';
