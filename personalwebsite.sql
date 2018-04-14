SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

create Database db_personalwebsite DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use db_personalwebsite;


CREATE TABLE `simple_user` (
  `id`        int(11)     NOT NULL AUTO_INCREMENT
  COMMENT '管理员表id',
  `username`  varchar(50) NOT NULL
  COMMENT '账号',
  `password`  varchar(50) NOT NULL
  COMMENT '密码',
  update_time datetime    not null
  comment '最后一次登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_unique` (`username`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


CREATE TABLE `simple_message` (
  `id`          int(11)      NOT NULL AUTO_INCREMENT
  COMMENT '信息表id',
  `name`        varchar(100) NOT NULL
  COMMENT '用户姓名',
  `email`       varchar(50)           DEFAULT NULL
  COMMENT '邮箱',
  `phone`       varchar(20)           DEFAULT NULL
  COMMENT '手机',
  `message`     varchar(1000)         DEFAULT NULL
  COMMENT '信息',
  `create_time` datetime     NOT NULL
  COMMENT '创建时间',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

CREATE TABLE `simple_article` (
  `id`          int(11)        NOT NULL AUTO_INCREMENT
  COMMENT '文章表id',
  `title`       varchar(50)    NOT NULL
  COMMENT '标题',
  `content`     varchar(20000) NOT NULL
  COMMENT '内容',
  `tags`        varchar(50)             DEFAULT NULL
  COMMENT '类型',
  `status`      varchar(20)             DEFAULT NULL
  COMMENT '状态',
  `create_time` datetime       NOT NULL
  COMMENT '创建时间',
  `update_time` datetime       NOT NULL
  COMMENT '最后一次更新时间',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;