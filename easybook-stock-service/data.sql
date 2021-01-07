ON UPDATE CURRENT_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ;

DROP TABLE IF EXISTS `admins`;
CREATE TABLE `admins` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `admin_name` varchar(32) UNIQUE NOT NULL COMMENT '管理员姓名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `book_name` varchar(32) NOT NULL COMMENT '书名',
  `total_cnt` int NOT NULL COMMENT '总库存',
  `desc` varchar(256) DEFAULT NULL COMMENT '简介',
  `pic_url` varchar(32) DEFAULT NULL COMMENT '图片链接',
  `place` varchar(32) DEFAULT NULL COMMENT '图书位置',
  `price` varchar(32) DEFAULT NULL COMMENT '图书价格',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ;

-- 配置表，可以缓存起来
DROP TABLE IF EXISTS `confs`;
CREATE TABLE `confs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `is_selected` tinyint(1) default null COMMENT '同一时间只有一个配置生效，存在于缓存',
  `desc` VARCHAR(16) DEFAULT NULL COMMENT '该时间的描述',
  `busy_period` varchar(32) DEFAULT NULL COMMENT '每一天的繁忙时间段',
  PRIMARY KEY (`id`)
) ;

DROP TABLE IF EXISTS `deal`;
CREATE TABLE `deal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NOT NULL,
  `book_id` bigint(20) NOT NULL,
  `stu_id` bigint(20) NOT NULL COMMENT '学生号',
  `username` varchar(32) NOT NULL,
  `book_name` varchar(32) NOT NULL,
  `status` int NOT NULL DEFAULT 0 COMMENT '1 2 3',
  `created_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `uid_status_idx`(`user_id`, `status`),
  KEY `bid_status_idx`(`book_id`, `status`)
) ;


DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `book_id` bigint(20) not null,
  `cnt` int DEFAULT 0 COMMENT '数量',
  PRIMARY KEY (`id`),
  KEY `b_idx`(`book_id`)
) ;
