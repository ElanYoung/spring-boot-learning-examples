/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : lin-cms

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 18/05/2023 11:29:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 用户基本信息表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`          int UNSIGNED NOT NULL AUTO_INCREMENT,
    `username`    varchar(24)  NOT NULL COMMENT '用户名（唯一）',
    `nickname`    varchar(24)  NULL     DEFAULT NULL COMMENT '用户昵称',
    `avatar`      varchar(500) NULL     DEFAULT NULL COMMENT '头像',
    `email`       varchar(100) NULL     DEFAULT NULL COMMENT '邮箱',
    `create_time` datetime(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` datetime(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `is_deleted`  int          NULL     DEFAULT 0 COMMENT '删除状态',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_username` (`username`, `is_deleted`),
    UNIQUE INDEX `uk_email` (`email`, `is_deleted`)
);