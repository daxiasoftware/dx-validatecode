CREATE TABLE `dx_validate_code` (
  `id` varchar(255) NOT NULL,
  `account` varchar(100) NOT NULL COMMENT '帐户，手机或邮箱',
  `code` varchar(100) NOT NULL COMMENT '验证码',
  `type` int(11) NOT NULL COMMENT '类型',
  `create_time` datetime NOT NULL,
  `expire_time` datetime DEFAULT NULL,
  `failed_times` int(11) NOT NULL DEFAULT '0' COMMENT '验证失败次数',
  `is_used` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已经使用了',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;