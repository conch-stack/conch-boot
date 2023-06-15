-- ----------------------------
-- 账户
-- ----------------------------
CREATE TABLE `org_account`
(
    `account`      varchar(255) NOT NULL COMMENT '机构账号',
    `org_name`     varchar(255) NOT NULL COMMENT '机构名称',
    `short_name`   varchar(255) NOT NULL COMMENT '机构别名',
    `pwd`          varchar(255) NOT NULL COMMENT '机构账号密码',
    `signature`    varchar(255) NOT NULL COMMENT '机构账户签名',
    `amount`       double       NOT NULL COMMENT '余额',
    `link_name`    varchar(255) NOT NULL COMMENT '机构联系人姓名',
    `link_phone`   varchar(255) NOT NULL COMMENT '机构联系人电话',
    `link_email`   varchar(255) NULL COMMENT '机构联系人邮箱',
    `addr`         varchar(255) NOT NULL COMMENT '机构地址',
    `vip`          tinyint(4) NOT NULL COMMENT 'VIP等级：0普通会员,1超级会员',
    `status`       tinyint(4) NOT NULL COMMENT '可用状态：0不可用,1可用',
    `company_auth` tinyint(4) NOT NULL COMMENT '企业认证：0未认证,1已认证',
    `acc_desc`     varchar(255) NULL COMMENT '机构描述',
    `ct`           TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `ut`           TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `opt_id`       varchar(255) NOT NULL COMMENT '操作人',
    PRIMARY KEY (`account`),
    UNIQUE KEY `UK_org_signature` (`signature`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT '机构账户';